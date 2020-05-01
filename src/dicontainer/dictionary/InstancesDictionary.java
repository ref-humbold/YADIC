package dicontainer.dictionary;

import java.util.HashMap;
import java.util.Map;

import dicontainer.commons.Instance;
import dicontainer.commons.TypesUtils;

class InstancesDictionary
{
    private final Map<Class<?>, Instance<?>> instances = new HashMap<>();

    InstancesDictionary()
    {
    }

    <T> void insert(Class<T> type, T instance)
    {
        TypesUtils.requireNonNull(instance);
        instances.put(type, Instance.make(instance));
    }

    boolean contains(Class<?> type)
    {
        return instances.containsKey(type);
    }

    @SuppressWarnings("unchecked")
    <T> Instance<T> get(Class<T> type)
    {
        return Instance.cast((Instance<T>)instances.get(type));
    }
}
