package dicontainer.resolver;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import dicontainer.DIException;
import dicontainer.dictionary.valuetypes.Instance;
import dicontainer.resolver.exception.CircularDependenciesException;
import dicontainer.resolver.exception.MissingDependenciesException;
import dicontainer.resolver.exception.NoInstanceCreatedException;

class ConstructorResolver
{
    private final DIResolver resolver;

    ConstructorResolver(DIResolver resolver)
    {
        this.resolver = resolver;
    }

    <T> T resolve(Class<T> type, Stack<Class<?>> path)
    {
        path.push(type);

        T object = resolveType(type, path);

        path.pop();
        return object;
    }

    private <T> T resolveType(Class<T> type, Stack<Class<?>> path)
    {
        return resolver.dictionary.findInstance(type).extract(() -> {
            Class<? extends T> subtype = resolver.dictionary.findType(type).subtype;
            T object = construct(new TypeConstructors<>(subtype), path);
            resolver.dictionary.addSingleton(type, object);
            return object;
        });
    }

    private <T> T construct(TypeConstructors<T> constructors, Stack<Class<?>> path)
    {
        if(constructors.annotatedConstructor != null)
        {
            Instance<T> instance = invoke(constructors.annotatedConstructor, path);

            try
            {
                return instance.extract();
            }
            catch(Exception e)
            {
                throw new NoInstanceCreatedException(String.format(
                        "Dependency constructor could not produce an instance for type %s",
                        constructors.typename), e);
            }
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

            if(!resolver.dictionary.contains(parameter))
                return Instance.none(new MissingDependenciesException(
                        String.format("No dependency for type %s found when resolving type %s",
                                      parameter.getName(), typename)));

            parameters.add(resolver.resolve(parameter, path));
        }

        try
        {
            return Instance.of(constructor.newInstance(parameters.toArray()),
                               new NoInstanceCreatedException(String.format(
                                       "Constructor could not produce an instance of type %s",
                                       typename)));
        }
        catch(Exception e)
        {
            return Instance.none(new NoInstanceCreatedException(
                    String.format("Could not invoke constructor due to an error: %s",
                                  e.getMessage()), e));
        }
    }
}
