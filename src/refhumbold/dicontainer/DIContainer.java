package refhumbold.dicontainer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import refhumbold.dicontainer.annotation.Dependency;
import refhumbold.dicontainer.exception.*;

public final class DIContainer
{
    private final TypesContainer typesContainer = new TypesContainer();

    public DIContainer()
    {
    }

    /**
     * Register concrete type class in the container.
     * @param type type class
     * @return {@code this} for method chaining
     * @throws AbstractTypeException if type is an abstract class or an interface
     */
    public <T> DIContainer registerType(Class<T> type)
        throws AbstractTypeException
    {
        return registerType(type, ConstructionPolicy.CONSTRUCT);
    }

    /**
     * Register concrete type class in the container with singleton specification.
     * @param type type class
     * @param policy construction policy of instances
     * @return {@code this} for method chaining
     * @throws AbstractTypeException if type is an abstract class or an interface
     */
    public <T> DIContainer registerType(Class<T> type, ConstructionPolicy policy)
        throws AbstractTypeException
    {
        if(TypesContainer.isAbstractType(type))
            throw new AbstractTypeException(
                String.format("Type %s is abstract, so it cannot be self registered.",
                              type.getSimpleName()));

        return registerType(type, type, policy);
    }

    /**
     * Register subtype class for its supertype.
     * @param supertype supertype class
     * @param subtype subtype class
     * @return {@code this} for method chaining
     */
    public <T> DIContainer registerType(Class<T> supertype, Class<? extends T> subtype)
    {
        return registerType(supertype, subtype, ConstructionPolicy.CONSTRUCT);
    }

    /**
     * Register subtype class for its supertype.
     * @param supertype supertype class
     * @param subtype subtype class
     * @param policy construction policy of instances
     * @return {@code this} for method chaining
     */
    public <T> DIContainer registerType(Class<T> supertype, Class<? extends T> subtype,
                                        ConstructionPolicy policy)
    {
        if(policy == ConstructionPolicy.SINGLETON)
            typesContainer.addSingletonSubtype(supertype, subtype);
        else
            typesContainer.addSubtype(supertype, subtype);

        return this;
    }

    /**
     * Register concrete instance of its type.
     * @param type type class
     * @param instance concrete instance
     * @return {@code this} for method chaining
     */
    public <T> DIContainer registerInstance(Class<T> type, T instance)
    {
        if(instance == null)
            throw new NullInstanceException(
                String.format("Given instance to register for %s is null.", type.getSimpleName()));

        typesContainer.addInstance(type, instance);

        return this;
    }

    /**
     * Resolve all depencencies and construct a new instance of given type using {@link Dependency}.
     * @param type type class
     * @return new instance
     * @throws DIException if type cannot be resolved
     */
    public <T> T resolve(Class<T> type)
        throws DIException
    {
        return resolveType(type, new Stack<>());
    }

    /**
     * Resolve and inject all dependencies to given object using {@link Dependency} on setters.
     * @param instance instance object
     * @return resolved instance
     * @throws DIException if instance cannot be built up
     */
    public <T> T buildUp(T instance)
        throws DIException
    {
        buildUpObject(instance, new Stack<>());

        return instance;
    }

    private boolean isSetter(Method method)
    {
        return method.getReturnType() == void.class && method.getName().startsWith("set")
            && method.getParameterCount() == 1;
    }

    private <T> T resolveType(Class<T> type, Stack<Class<?>> resolved)
        throws DIException
    {
        T object = typesContainer.getInstance(type);

        if(object == null)
            object = resolveConstructor(type, resolved);

        buildUpObject(object, resolved);

        return object;
    }

    private <T> T resolveConstructor(Class<T> type, Stack<Class<?>> resolved)
        throws DIException
    {
        resolved.push(type);

        Class<? extends T> mappedClass = findRegisteredConcreteType(type);
        Constructor<? extends T>[] constructors = getConstructors(mappedClass);

        Arrays.sort(constructors, new ConstructorComparator());

        if(constructors.length > 1)
            if(constructors[1].isAnnotationPresent(Dependency.class))
                throw new MultipleAnnotatedConstructorsException(
                    "Only one constructor can be annotated as dependency.");

        T object = null;
        DIException lastException = null;

        for(Constructor<? extends T> ctor : constructors)
        {
            try
            {
                object = createInstance(ctor, resolved);
            }
            catch(DIException e)
            {
                lastException = e;
            }

            if(object != null || ctor.isAnnotationPresent(Dependency.class))
                break;
        }

        resolved.pop();

        if(object == null)
            throw lastException != null ? lastException : new NoInstanceCreatedException(
                String.format(
                    "No instance produced for %s, although all possibilities have been checked.",
                    type.getSimpleName()));

        typesContainer.updateInstance(type, object);

        return object;
    }

    private <T> void resolveSetter(T object, Method setter, Stack<Class<?>> resolved)
        throws DIException
    {
        ArrayList<Object> paramObjects = new ArrayList<>();

        for(Class<?> p : setter.getParameterTypes())
            paramObjects.add(resolveType(p, resolved));

        try
        {
            setter.invoke(object, paramObjects.toArray());
        }
        catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
        {
            throw new DIException(e.getMessage(), e);
        }
    }

    private <T> void buildUpObject(T obj, Stack<Class<?>> resolved)
        throws DIException
    {
        ArrayList<Method> setters = new ArrayList<>();

        for(Method m : obj.getClass().getMethods())
            if(m.isAnnotationPresent(Dependency.class))
            {
                if(!isSetter(m))
                    throw new IncorrectDependencySetterException(
                        "Dependency method must have exactly one argument, void return type and name starting with \'set\'.");

                setters.add(m);
            }

        for(Method setter : setters)
            resolveSetter(obj, setter, resolved);
    }

    private <T> T createInstance(Constructor<? extends T> ctor, Stack<Class<?>> resolved)
        throws DIException
    {
        ArrayList<Object> params = new ArrayList<>();

        for(Class<?> type : ctor.getParameterTypes())
        {
            if(resolved.contains(type))
                throw new CircularDependenciesException("Dependencies resolving detected a cycle.");

            if(!typesContainer.containsType(type))
                throw new MissingDependenciesException("No dependency found when resolving.");

            params.add(resolveType(type, resolved));
        }

        try
        {
            return ctor.newInstance(params.toArray());
        }
        catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
        {
            throw new DIException(e.getMessage(), e);
        }
    }

    private <T> Class<? extends T> findRegisteredConcreteType(Class<T> type)
        throws DIException
    {
        Class<? extends T> subtype = type;

        do
        {
            if(!typesContainer.containsSubtype(subtype))
            {
                if(TypesContainer.isAbstractType(subtype))
                    throw new MissingDependenciesException(
                        String.format("Abstract type %s has no registered concrete subclass.",
                                      subtype.getSimpleName()));

                typesContainer.addSubtype(subtype);
            }

            subtype = typesContainer.getSubtype(subtype);
        } while(TypesContainer.isAbstractType(subtype));

        return subtype;
    }

    @SuppressWarnings("unchecked")
    private <T> Constructor<? extends T>[] getConstructors(Class<? extends T> type)
        throws NoSuitableConstructorException
    {
        Constructor<?>[] constructors;

        try
        {
            constructors = type.getConstructors();
        }
        catch(SecurityException e)
        {
            throw new NoSuitableConstructorException(
                String.format("No dependency constructor found for class %s", type.getSimpleName()),
                e);
        }

        if(constructors == null || constructors.length == 0)
            throw new NoSuitableConstructorException(
                String.format("No dependency constructor found for class %s",
                              type.getSimpleName()));

        return (Constructor<? extends T>[])constructors;
    }
}
