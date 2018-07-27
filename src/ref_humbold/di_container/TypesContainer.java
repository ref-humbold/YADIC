package ref_humbold.di_container;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

final class TypesContainer
{
    private Map<Class<?>, Optional<Object>> instances = new HashMap<>();
    private Map<Class<?>, Class<?>> subtypes = new HashMap<>();

    public TypesContainer()
    {
    }

    @SuppressWarnings("unchecked")
    public <T> Class<? extends T> getSubtype(Class<T> type)
    {
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
        return subtypes.containsKey(toRefType(type));
    }

    public boolean containsType(Class<?> type)
    {
        return containsInstance(type) || containsSubtype(type);
    }

    public <T> boolean updateInstance(Class<T> type, T instance)
    {
        if(!instances.containsKey(toRefType(type)))
            return false;

        addInstance(type, instance);
        return true;
    }

    @SuppressWarnings("unchecked")
    public <T> T getInstance(Class<?> type)
    {
        return containsRealInstance(type) ? (T)instances.get(toRefType(type)).get() : null;
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
