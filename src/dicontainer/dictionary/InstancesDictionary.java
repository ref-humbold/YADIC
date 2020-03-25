package dicontainer.dictionary;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import dicontainer.commons.Instance;

public class InstancesDictionary
{
    private final Map<Class<?>, Instance<?>> instances = new HashMap<>();

    public InstancesDictionary()
    {
    }

    public <T> void insert(Class<T> type, T instance)
    {
        Objects.requireNonNull(instance);
        instances.put(type, Instance.make(instance));
    }

    @SuppressWarnings("unchecked")
    public <T> Instance<T> get(Class<T> type)
    {
        return Instance.cast((Instance<T>)instances.get(type));
    }
}
