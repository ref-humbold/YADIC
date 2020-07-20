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

    public <T> T resolve(Class<T> type)
    {
        return resolveWithPath(type, new Stack<>());
    }

    <T> T resolveWithPath(Class<T> type, Stack<Class<?>> path)
    {
        T object = constructorResolver.resolve(type, path);
        setterResolver.resolve(object, path);

        return object;
    }
}
