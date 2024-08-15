package yadic.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import yadic.registry.valuetypes.Instance;

class InstancesDictionary
{
    private final Map<Class<?>, Instance<?>> instancesMap = new HashMap<>();

    <T> void insert(Class<T> type, T instance)
    {
        instancesMap.put(type, Instance.of(Objects.requireNonNull(instance)));
    }

    boolean contains(Class<?> type)
    {
        return instancesMap.containsKey(type);
    }

    <T> Instance<T> get(Class<T> type)
    {
        return Instance.cast(instancesMap.get(type));
    }
}
