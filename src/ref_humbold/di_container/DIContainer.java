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
    private Map<Class<?>, Object> instances = new HashMap<>();
    private Map<Class<?>, Class<?>> classes = new HashMap<>();

    public DIContainer()
    {
    }

    public <T> void registerType(Class<T> cls)
        throws AbstractTypeException
    {
        registerType(cls, false);
    }

    public <T> void registerType(Class<T> cls, boolean isSingleton)
        throws AbstractTypeException
    {
        if(isAbstractType(cls))
            throw new AbstractTypeException("Type " + cls.getSimpleName() + " is abstract.");

        registerType(cls, cls, isSingleton);
    }

    public <T> void registerType(Class<T> supercls, Class<? extends T> cls)
    {
        registerType(supercls, cls, false);
    }

    public <T> void registerType(Class<T> supercls, Class<? extends T> cls, boolean isSingleton)
    {
        classes.put(changeToReferenceType(supercls), changeToReferenceType(cls));

        if(isSingleton)
            instances.put(changeToReferenceType(supercls), null);
        else if(instances.containsKey(changeToReferenceType(supercls)))
            instances.remove(changeToReferenceType(supercls));
    }

    public <T> void registerInstance(Class<T> cls, T instance)
    {
        if(instance == null)
            throw new NullInstanceException(
                "Given instance of type " + cls.getSimpleName() + "is null.");

        instances.put(changeToReferenceType(cls), instance);
    }

    public <T> T resolve(Class<T> cls)
        throws DIException
    {
        return resolveType(cls, new ArrayDeque<>());
    }

    public <T> void buildUp(T obj)
        throws DIException
    {
        buildUpObject(obj, new ArrayDeque<>());
    }

    private boolean isAbstractType(Class<?> cls)
    {
        return cls.isInterface() || Modifier.isAbstract(cls.getModifiers());
    }

    private boolean isCorrectAnnotatedSetter(Method setter)
    {
        return setter.getReturnType() == void.class && setter.getName().startsWith("set")
            && setter.getParameterCount() > 0;
    }

    private Class<?> changeToReferenceType(Class<?> cls)
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
    private <T> T resolveType(Class<T> cls, ArrayDeque<Class<?>> resolved)
        throws DIException
    {
        T object;

        if(instances.containsKey(changeToReferenceType(cls))
            && instances.get(changeToReferenceType(cls)) != null)
            object = (T)instances.get(changeToReferenceType(cls));
        else
            object = resolveConstructor(cls, resolved);

        buildUpObject(object, resolved);

        return object;
    }

    private <T> T resolveConstructor(Class<T> cls, ArrayDeque<Class<?>> resolved)
        throws DIException
    {
        resolved.addFirst(cls);

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

        if(object == null)
        {
            resolved.removeFirst();

            throw lastException != null ? lastException : new NoInstanceCreatedException(
                "No instance produced for " + cls.getSimpleName()
                    + ", though all possibilities have been checked.");
        }

        if(instances.containsKey(changeToReferenceType(cls)))
            instances.put(changeToReferenceType(cls), object);

        resolved.removeFirst();

        return object;
    }

    private <T> void resolveMethod(T object, Method method, ArrayDeque<Class<?>> resolved)
        throws DIException
    {
        ArrayList<Object> paramObjects = new ArrayList<>();

        for(Class<?> p : method.getParameterTypes())
            paramObjects.add(resolveType(p, resolved));

        try
        {
            method.invoke(object, paramObjects.toArray());
        }
        catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
        {
            throw new DIException(e.getMessage(), e);
        }
    }

    private <T> void buildUpObject(T obj, ArrayDeque<Class<?>> resolved)
        throws DIException
    {
        ArrayList<Method> setters = new ArrayList<>();

        for(Method m : obj.getClass().getMethods())
            if(m.isAnnotationPresent(DependencySetter.class))
            {
                if(!isCorrectAnnotatedSetter(m))
                    throw new IncorrectDependencySetterException(
                        "Dependency method must have at least one argument and void return type.");

                setters.add(m);
            }

        for(Method s : setters)
            resolveMethod(obj, s, resolved);
    }

    private <T> T createInstance(Constructor<? extends T> ctor, ArrayDeque<Class<?>> resolved)
        throws DIException
    {
        ArrayList<Object> params = new ArrayList<>();

        for(Class<?> cls : ctor.getParameterTypes())
        {
            if(resolved.contains(cls))
                throw new CircularDependenciesException("Dependencies resolving detected a cycle.");

            if(!instances.containsKey(changeToReferenceType(cls)) && !classes.containsKey(
                changeToReferenceType(cls)))
                throw new MissingDependenciesException("No dependency found when resolving.");

            params.add(resolveType(changeToReferenceType(cls), resolved));
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
            if(!classes.containsKey(changeToReferenceType(mappedClass)))
                if(isAbstractType(mappedClass))
                    throw new AbstractTypeException(
                        "Type " + mappedClass.getSimpleName() + " is abstract.");
                else
                    classes.put(changeToReferenceType(mappedClass),
                                changeToReferenceType(mappedClass));

            mappedClass = (Class<? extends T>)classes.get(changeToReferenceType(mappedClass));
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
