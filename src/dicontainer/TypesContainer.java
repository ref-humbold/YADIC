package dicontainer;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import dicontainer.annotation.Register;
import dicontainer.annotation.SelfRegister;
import dicontainer.exception.AbstractTypeException;
import dicontainer.exception.NotDerivedTypeException;

final class TypesContainer
{
    private Map<Class<?>, Optional<Object>> instances = new HashMap<>();
    private Map<Class<?>, Class<?>> subtypes = new HashMap<>();

    public TypesContainer()
    {
    }

    public static boolean isAbstractType(Class<?> type)
    {
        return type.isInterface() || Modifier.isAbstract(type.getModifiers());
    }

    @SuppressWarnings("unchecked")
    public <T> Class<? extends T> getSubtype(Class<T> type)
    {
        if(type.isAnnotationPresent(Register.class))
        {
            Class<?> subtype = type.getAnnotation(Register.class).value();

            if(!type.isAssignableFrom(subtype))
                throw new NotDerivedTypeException(
                        String.format("Type registered in @Register is not derived type of %s",
                                      type.getSimpleName()));

            if(TypesContainer.isAbstractType(type) && Objects.equals(type, subtype))
                throw new AbstractTypeException(
                        String.format("Type registered in @Register in %s is abstract.",
                                      type.getSimpleName()));

            return (Class<? extends T>)subtype;
        }

        if(type.isAnnotationPresent(SelfRegister.class))
        {
            if(TypesContainer.isAbstractType(type))
                throw new AbstractTypeException(
                        String.format("Abstract type %s is annotated with @SelfRegister.",
                                      type.getSimpleName()));

            return type;
        }

        return (Class<? extends T>)subtypes.get(toRefType(type));
    }

    public <T> void addSubtype(Class<T> type)
    {
        addSubtype(type, type);
    }

    public <T> void addSubtype(Class<T> supertype, Class<? extends T> type)
    {
        subtypes.put(toRefType(supertype), toRefType(type));
        removeInstance(supertype);
    }

    public <T> void addSingletonSubtype(Class<T> supertype, Class<? extends T> type)
    {
        addSubtype(supertype, type);
        makeSingleton(supertype);
    }

    public <T> void addInstance(Class<T> type, T instance)
    {
        instances.put(toRefType(type), Optional.of(instance));
    }

    public boolean containsSubtype(Class<?> type)
    {
        return isAnnotatedType(type) || subtypes.containsKey(toRefType(type));
    }

    public boolean containsType(Class<?> type)
    {
        return containsInstance(type) || containsSubtype(type);
    }

    public <T> void updateInstance(Class<T> type, T instance)
    {
        if(instances.containsKey(toRefType(type)) || isSingletonAnnotated(type))
            addInstance(type, instance);
    }

    @SuppressWarnings("unchecked")
    public <T> T getInstance(Class<?> type)
    {
        if(!containsRealInstance(type))
            return null;

        Optional<Object> optional = instances.get(toRefType(type));

        if(optional.isEmpty())
            return null;

        return (T)optional.get();
    }

    private boolean isAnnotatedType(Class<?> type)
    {
        return type.isAnnotationPresent(Register.class) || type.isAnnotationPresent(
                SelfRegister.class);
    }

    private boolean isSingletonAnnotated(Class<?> type)
    {
        return type.isAnnotationPresent(Register.class)
                && type.getAnnotation(Register.class).policy() == ConstructionPolicy.SINGLETON
                || type.isAnnotationPresent(SelfRegister.class)
                && type.getAnnotation(SelfRegister.class).policy() == ConstructionPolicy.SINGLETON;
    }

    private boolean containsInstance(Class<?> type)
    {
        return instances.containsKey(toRefType(type));
    }

    private boolean containsRealInstance(Class<?> type)
    {
        return instances.containsKey(toRefType(type)) && instances.get(toRefType(type)).isPresent();
    }

    private void removeInstance(Class<?> type)
    {
        instances.remove(toRefType(type));
    }

    private void makeSingleton(Class<?> type)
    {
        instances.put(toRefType(type), Optional.empty());
    }

    private Class<?> toRefType(Class<?> type)
    {
        if(type == byte.class)
            return Byte.class;

        if(type == short.class)
            return Short.class;

        if(type == int.class)
            return Integer.class;

        if(type == long.class)
            return Long.class;

        if(type == float.class)
            return Float.class;

        if(type == double.class)
            return Double.class;

        if(type == boolean.class)
            return Boolean.class;

        if(type == char.class)
            return Character.class;

        return type;
    }
}
