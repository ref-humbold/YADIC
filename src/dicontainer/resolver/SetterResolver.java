package dicontainer.resolver;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import dicontainer.annotation.Dependency;
import dicontainer.exception.IncorrectDependencySetterException;
import dicontainer.exception.MissingDependenciesException;

class SetterResolver
{
    private static final String SETTER_NAME_REGEX = "^set[a-z]?[A-Z](\\w|\\d)*";
    private final DIResolver resolver;

    SetterResolver(DIResolver resolver)
    {
        this.resolver = resolver;
    }

    <T> T resolve(T object, Stack<Class<?>> path)
    {
        List<Method> dependencySetters = Arrays.stream(object.getClass().getMethods())
                                               .filter(this::hasAnnotation)
                                               .filter(this::validateSetter)
                                               .collect(Collectors.toList());

        for(Method setter : dependencySetters)
            invoke(object, setter, path);

        return object;
    }

    private <T> void invoke(T object, Method setter, Stack<Class<?>> path)
    {
        List<Object> parameters = new ArrayList<>();
        String typename = object.getClass().getName();

        for(Class<?> parameter : setter.getParameterTypes())
        {
            if(!resolver.dictionary.contains(parameter))
                throw new MissingDependenciesException(
                        String.format("No dependency for type %s found when resolving type %s",
                                      parameter.getName(), typename));

            parameters.add(resolver.resolve(parameter, path));
        }

        try
        {
            setter.invoke(object, parameters.toArray());
        }
        catch(Exception e)
        {
            throw new SetterInvocationException(
                    String.format("Could not invoke setter '%s' due to an error: %s",
                                  setter.getName(), e.getMessage()), e);
        }
    }

    private boolean hasAnnotation(Method setter)
    {
        return setter.isAnnotationPresent(Dependency.class);
    }

    private boolean validateSetter(Method method)
    {
        if(!isSetter(method))
            throw new IncorrectDependencySetterException(
                    "Dependency method must be a setter method");

        return true;
    }

    private boolean isSetter(Method method)
    {
        return method.getReturnType() == void.class && method.getName().matches(SETTER_NAME_REGEX)
                && method.getParameterCount() == 1;
    }
}
