package dicontainer.dictionary;

import java.util.HashMap;
import java.util.Map;

import dicontainer.dictionary.valuetypes.Instance;

class InstancesDictionary
{
    private final Map<Class<?>, Instance<?>> instances = new HashMap<>();

    <T> void insert(Class<T> type, T instance)
    {
        TypesUtils.requireNonNull(instance);
        instances.put(type, Instance.of(instance));
    }

    boolean contains(Class<?> type)
    {
        return instances.containsKey(type);
    }

    <T> Instance<T> get(Class<T> type)
    {
        return Instance.cast(instances.get(type));
    }
}
