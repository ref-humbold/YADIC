package dicontainer.resolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import dicontainer.commons.Instance;
import dicontainer.dictionary.DIDictionary;
import dicontainer.exception.CircularDependenciesException;
import dicontainer.exception.DIException;
import dicontainer.exception.MissingDependenciesException;
import dicontainer.exception.NoInstanceCreatedException;

public class ConstructorResolver
{
    private final DIDictionary dictionary;

    public ConstructorResolver(DIDictionary dictionary)
    {
        this.dictionary = dictionary;
    }

    public <T> T resolve(Class<T> type)
    {
        return resolveWithPath(type, new Stack<>());
    }

    private <T> T resolveWithPath(Class<T> type, Stack<Class<?>> path)
    {
        path.push(type);

        T object = resolveType(type, path);

        path.pop();
        return object;
    }

    private <T> T resolveType(Class<T> type, Stack<Class<?>> path)
    {
        Instance<T> instance = dictionary.findInstance(type);

        if(instance.exists())
            return instance.extract();

        Class<? extends T> subtype = dictionary.findType(type).subtype;
        T object = construct(new TypeConstructors<>(subtype), path);
        dictionary.addSingleton(type, object);

        return object;
    }

    private <T> T construct(TypeConstructors<T> constructors, Stack<Class<?>> path)
    {
        if(constructors.annotatedConstructor != null)
        {
            Instance<T> instance = invoke(constructors.annotatedConstructor, path);

            if(!instance.exists())
                throw new NoInstanceCreatedException(String.format(
                        "Dependency constructor could not produce an instance for type %s",
                        constructors.typename), instance.getException());

            return instance.extract();
        }

        Instance<T> instance = Instance.none();

        for(int i = 0; i < constructors.parameterizedConstructors.size() && !instance.exists(); ++i)
            try
            {
                instance = invoke(constructors.parameterizedConstructors.get(i), path);
            }
            catch(DIException e)
            {
                instance = Instance.none(e);
            }

        return instance.extract();
    }

    private <T> Instance<T> invoke(Constructor<T> constructor, Stack<Class<?>> path)
    {
        List<Object> parameters = new ArrayList<>();
        String typename = constructor.getDeclaringClass().getName();

        for(Class<?> parameter : constructor.getParameterTypes())
        {
            if(path.contains(parameter))
                return Instance.none(new CircularDependenciesException(String.format(
                        "Dependencies resolving detected a cycle detected between %s and %s",
                        parameter.getName(), typename)));

            if(!dictionary.contains(parameter))
                return Instance.none(new MissingDependenciesException(
                        String.format("No dependency for type %s found when resolving type %s",
                                      parameter.getName(), typename)));

            parameters.add(resolveWithPath(parameter, path));
        }

        try
        {
            return Instance.make(constructor.newInstance(parameters.toArray()),
                                 new NoInstanceCreatedException(String.format(
                                         "Constructor could not produce an instance of type %s",
                                         typename)));
        }
        catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
        {
            return Instance.none(new NoInstanceCreatedException(e.getMessage(), e));
        }
    }
}
