package dicontainer.resolver;

import java.util.Stack;

import dicontainer.dictionary.DIDictionary;

public class DIResolver
{
    final DIDictionary dictionary;
    private final ConstructorResolver constructorResolver = new ConstructorResolver(this);
    private final SetterResolver setterResolver = new SetterResolver(this);

    public DIResolver(DIDictionary dictionary)
    {
        this.dictionary = dictionary;
    }

    public <T> T construct(Class<T> type)
    {
        return resolve(type, new Stack<>());
    }

    public <T> T inject(T instance)
    {
        return setterResolver.resolve(instance, new Stack<>());
    }

    <T> T resolve(Class<T> type, Stack<Class<?>> path)
    {
        T object = constructorResolver.resolve(type, path);

        object = setterResolver.resolve(object, path);
        return object;
    }
}
