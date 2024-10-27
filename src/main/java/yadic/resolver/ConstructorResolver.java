package yadic.resolver;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import yadic.YadicException;
import yadic.registry.valuetypes.Instance;
import yadic.resolver.exception.CircularDependenciesException;
import yadic.resolver.exception.MissingDependenciesException;
import yadic.resolver.exception.NoInstanceCreatedException;

class ConstructorResolver
{
    private final TypesResolver resolver;

    ConstructorResolver(TypesResolver resolver)
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
        return resolver.registry.findInstance(type).extract(() -> createInstance(type, path));
    }

    private <T> T createInstance(Class<T> type, Stack<Class<?>> path)
    {
        Class<? extends T> subtype = resolver.registry.findType(type).type();
        T object = construct(new TypeConstructors<>(subtype), path);
        resolver.registry.addSingleton(type, object);
        return object;
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
            catch(YadicException e)
            {
                instance = Instance.none(e);
            }

        return instance.extract();
    }

    private <T> Instance<T> invoke(Constructor<T> constructor, Stack<Class<?>> path)
    {
        List<Object> parameters = new ArrayList<>();
        String typename = constructor.getDeclaringClass().getTypeName();

        for(Class<?> parameter : constructor.getParameterTypes())
        {
            if(path.contains(parameter))
                return Instance.none(new CircularDependenciesException(String.format(
                        "Dependencies resolving detected a cycle detected between %s and %s",
                        parameter.getTypeName(), typename)));

            if(!resolver.registry.contains(parameter))
                return Instance.none(new MissingDependenciesException(
                        String.format("No dependency for type %s found when resolving type %s",
                                      parameter.getTypeName(), typename)));

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
