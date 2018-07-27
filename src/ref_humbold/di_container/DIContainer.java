package ref_humbold.di_container;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import ref_humbold.di_container.annotation.DependencyConstructor;
import ref_humbold.di_container.annotation.DependencySetter;
import ref_humbold.di_container.exception.*;

public final class DIContainer
{
    private TypesContainer typesContainer = new TypesContainer();

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
        if(isAbstractType(type))
            throw new AbstractTypeException("Type " + type.getSimpleName() + " is abstract.");

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
     */
    public <T> DIContainer registerInstance(Class<T> type, T instance)
    {
        if(instance == null)
            throw new NullInstanceException(
                "Given instance of type " + type.getSimpleName() + "is null.");

        typesContainer.addInstance(type, instance);

        return this;
    }

    /**
     * Resolve all depencencies of given type using {@link DependencyConstructor} and {@link DependencySetter}.
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
     * Inject all dependencies to given object using {@link DependencySetter}.
     * @param obj instance object
     * @throws DIException if instance cannot be built up
     */
    public <T> DIContainer buildUp(T obj)
        throws DIException
    {
        buildUpObject(obj, new Stack<>());

        return this;
    }

    private boolean isAbstractType(Class<?> type)
    {
        return type.isInterface() || Modifier.isAbstract(type.getModifiers());
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

        Class<? extends T> mappedClass = findRegisteredConcreteClass(type);
        Constructor<? extends T>[] constructors = getConstructors(mappedClass);

        Arrays.sort(constructors, new ConstructorComparator());

        if(constructors.length > 1)
            if(constructors[1].isAnnotationPresent(DependencyConstructor.class))
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

            if(object != null || ctor.isAnnotationPresent(DependencyConstructor.class))
                break;
        }

        resolved.pop();

        if(object == null)
            throw lastException != null ? lastException : new NoInstanceCreatedException(
                "No instance produced for " + type.getSimpleName()
                    + ", though all possibilities have been checked.");

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
            if(m.isAnnotationPresent(DependencySetter.class))
            {
                if(!isSetter(m))
                    throw new IncorrectDependencySetterException(
                        "Dependency method must have exactly one argument and void return type.");

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

    private <T> Class<? extends T> findRegisteredConcreteClass(Class<T> type)
        throws AbstractTypeException
    {
        Class<? extends T> mappedClass = type;

        do
        {
            if(!typesContainer.containsSubtype(mappedClass))
                if(isAbstractType(mappedClass))
                    throw new AbstractTypeException(
                        "Type " + mappedClass.getSimpleName() + " is abstract.");
                else
                    typesContainer.addSubtype(mappedClass);

            mappedClass = typesContainer.getSubtype(mappedClass);
        } while(isAbstractType(mappedClass));

        return mappedClass;
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
                "No dependency constructor found for class " + type.getSimpleName(), e);
        }

        if(constructors == null || constructors.length == 0)
            throw new NoSuitableConstructorException(
                "No dependency constructor found for class " + type.getSimpleName());

        return (Constructor<? extends T>[])constructors;
    }
}
