package dicontainer.resolver;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

import dicontainer.annotation.Dependency;
import dicontainer.exception.MultipleAnnotatedConstructorsException;

public class TypeConstructors
{
    public final Constructor<?> annotatedConstructor;
    public final List<Constructor<?>> parameterizedConstructors;

    public TypeConstructors(Class<?> type)
    {
        Constructor<?>[] constructors = type.getConstructors();

        Arrays.sort(constructors, new ConstructorComparator());
        parameterizedConstructors = Arrays.asList(constructors);

        if(constructors[0].isAnnotationPresent(Dependency.class))
        {
            if(constructors[1].isAnnotationPresent(Dependency.class))
                throw new MultipleAnnotatedConstructorsException(String.format(
                        "Type %s has more than one constructor with @Dependency annotation",
                        type.getName()));

            annotatedConstructor = constructors[0];
            parameterizedConstructors.remove(0);
        }
        else
        {
            annotatedConstructor = null;
        }
    }
}
