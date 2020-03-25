package dicontainer.dictionary;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InstancesDictionary
{
    private final Map<Class<?>, InstanceMapping<?>> instances = new HashMap<>();

    public InstancesDictionary()
    {
    }

    public <T> void insert(Class<T> type, T instance)
    {
        Objects.requireNonNull(instance);
        instances.put(type, new InstanceMapping<>(instance));
    }

    @SuppressWarnings("unchecked")
    public <T> InstanceMapping<T> get(Class<T> type)
    {
        return (InstanceMapping<T>)instances.get(type);
    }

    public void remove(Class<?> type)
    {
        instances.remove(type);
    }
}
