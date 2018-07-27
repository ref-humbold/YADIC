package ref_humbold.di_container;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import ref_humbold.di_container.annotation.DependencyConstructor;
import ref_humbold.di_container.annotation.DependencySetter;
import ref_humbold.di_container.exception.*;

public final class DIContainer
{
    private Map<Class<?>, Optional<Object>> instances = new HashMap<>();
    private Map<Class<?>, Class<?>> classes = new HashMap<>();

    public DIContainer()
    {
    }

    /**
     * Register concrete type class in the container.
     * @param cls type class
     * @return {@code this} for method chaining
     * @throws AbstractTypeException if type is an abstract class or an interface
     */
    public <T> DIContainer registerType(Class<T> cls)
        throws AbstractTypeException
    {
        return registerType(cls, ConstructionPolicy.CONSTRUCT);
    }

    /**
     * Register concrete type class in the container with singleton specification.
     * @param cls type class
     * @param policy construction policy of instances
     * @return {@code this} for method chaining
     * @throws AbstractTypeException if type is an abstract class or an interface
     */
    public <T> DIContainer registerType(Class<T> cls, ConstructionPolicy policy)
        throws AbstractTypeException
    {
        if(isAbstractType(cls))
            throw new AbstractTypeException("Type " + cls.getSimpleName() + " is abstract.");

        return registerType(cls, cls, policy);
    }

    /**
     * Register type class for its supertype.
     * @param supercls supertype class
     * @param cls type class
     * @return {@code this} for method chaining
     */
    public <T> DIContainer registerType(Class<T> supercls, Class<? extends T> cls)
    {
        return registerType(supercls, cls, ConstructionPolicy.CONSTRUCT);
    }

    /**
     * Register type class for its supertype.
     * @param supercls supertype class
     * @param cls type class
     * @param policy construction policy of instances
     * @return {@code this} for method chaining
     */
    public <T> DIContainer registerType(Class<T> supercls, Class<? extends T> cls,
                                        ConstructionPolicy policy)
    {
        classes.put(toReferenceType(supercls), toReferenceType(cls));

        if(policy == ConstructionPolicy.SINGLETON)
            instances.put(toReferenceType(supercls), Optional.empty());
        else if(instances.containsKey(toReferenceType(supercls)))
            instances.remove(toReferenceType(supercls));

        return this;
    }

    /**
     * Register concrete instance of its type.
     * @param cls type class
     * @param instance concrete instance
     */
    public <T> DIContainer registerInstance(Class<T> cls, T instance)
    {
        if(instance == null)
            throw new NullInstanceException(
                "Given instance of type " + cls.getSimpleName() + "is null.");

        instances.put(toReferenceType(cls), Optional.of(instance));

        return this;
    }

    /**
     * Resolve all depencencies of given type using {@link DependencyConstructor} and {@link DependencySetter}.
     * @param cls type class
     * @return new instance
     * @throws DIException if type cannot be resolved
     */
    public <T> T resolve(Class<T> cls)
        throws DIException
    {
        return resolveType(cls, new Stack<>());
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

    private boolean isAbstractType(Class<?> cls)
    {
        return cls.isInterface() || Modifier.isAbstract(cls.getModifiers());
    }

    private boolean isSetter(Method method)
    {
        return method.getReturnType() == void.class && method.getName().startsWith("set")
            && method.getParameterCount() == 1;
    }

    private Class<?> toReferenceType(Class<?> cls)
    {
        if(cls == byte.class)
            return Byte.class;

        if(cls == short.class)
            return Short.class;

        if(cls == int.class)
            return Integer.class;

        if(cls == long.class)
            return Long.class;

        if(cls == float.class)
            return Float.class;

        if(cls == double.class)
            return Double.class;

        if(cls == boolean.class)
            return Boolean.class;

        if(cls == char.class)
            return Character.class;

        return cls;
    }

    @SuppressWarnings("unchecked")
    private <T> T resolveType(Class<T> cls, Stack<Class<?>> resolved)
        throws DIException
    {
        T object;

        if(instances.containsKey(toReferenceType(cls)) && instances.get(toReferenceType(cls))
                                                                   .isPresent())
            object = (T)instances.get(toReferenceType(cls)).get();
        else
            object = resolveConstructor(cls, resolved);

        buildUpObject(object, resolved);

        return object;
    }

    private <T> T resolveConstructor(Class<T> cls, Stack<Class<?>> resolved)
        throws DIException
    {
        resolved.push(cls);

        Class<? extends T> mappedClass = findRegisteredConcreteClass(cls);
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
                "No instance produced for " + cls.getSimpleName()
                    + ", though all possibilities have been checked.");

        if(instances.containsKey(toReferenceType(cls)))
            instances.put(toReferenceType(cls), Optional.of(object));

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

        for(Class<?> cls : ctor.getParameterTypes())
        {
            if(resolved.contains(cls))
                throw new CircularDependenciesException("Dependencies resolving detected a cycle.");

            if(!instances.containsKey(toReferenceType(cls)) && !classes.containsKey(
                toReferenceType(cls)))
                throw new MissingDependenciesException("No dependency found when resolving.");

            params.add(resolveType(toReferenceType(cls), resolved));
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

    @SuppressWarnings("unchecked")
    private <T> Class<? extends T> findRegisteredConcreteClass(Class<T> cls)
        throws AbstractTypeException
    {
        Class<? extends T> mappedClass = cls;

        do
        {
            if(!classes.containsKey(toReferenceType(mappedClass)))
                if(isAbstractType(mappedClass))
                    throw new AbstractTypeException(
                        "Type " + mappedClass.getSimpleName() + " is abstract.");
                else
                    classes.put(toReferenceType(mappedClass), toReferenceType(mappedClass));

            mappedClass = (Class<? extends T>)classes.get(toReferenceType(mappedClass));
        } while(isAbstractType(mappedClass));

        return mappedClass;
    }

    @SuppressWarnings("unchecked")
    private <T> Constructor<? extends T>[] getConstructors(Class<? extends T> cls)
        throws NoSuitableConstructorException
    {
        Constructor<?>[] constructors;

        try
        {
            constructors = cls.getConstructors();
        }
        catch(SecurityException e)
        {
            throw new NoSuitableConstructorException(
                "No dependency constructor found for class " + cls.getSimpleName(), e);
        }

        if(constructors == null || constructors.length == 0)
            throw new NoSuitableConstructorException(
                "No dependency constructor found for class " + cls.getSimpleName());

        return (Constructor<? extends T>[])constructors;
    }
}
