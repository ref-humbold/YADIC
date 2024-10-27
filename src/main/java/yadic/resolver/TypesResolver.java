package yadic.resolver;

import java.util.Stack;

import yadic.YadicResolver;
import yadic.registry.DependencyRegistry;

public class TypesResolver
        implements YadicResolver
{
    final DependencyRegistry registry;
    private final ConstructorResolver constructorResolver = new ConstructorResolver(this);
    private final SetterResolver setterResolver = new SetterResolver(this);

    public TypesResolver(DependencyRegistry registry)
    {
        this.registry = registry;
    }

    @Override
    public <T> T resolve(Class<T> type)
    {
        return resolve(type, new Stack<>());
    }

    <T> T resolve(Class<T> type, Stack<Class<?>> path)
    {
        T object = constructorResolver.resolve(type, path);

        object = setterResolver.resolve(object, path);
        return object;
    }
}
