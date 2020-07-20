package dicontainer.resolver;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dicontainer.annotation.Dependency;

class TypeConstructors<T>
{
    final String typename;
    final Constructor<T> annotatedConstructor;
    final List<Constructor<T>> parameterizedConstructors;

    @SuppressWarnings("unchecked")
    TypeConstructors(Class<T> type)
    {
        typename = type.getName();

        Constructor<T>[] constructors = (Constructor<T>[])type.getConstructors();

        if(constructors.length == 0)
            throw new NoSuitableConstructorException(
                    String.format("No public constructors found for type %s", type.getName()));

        Arrays.sort(constructors, new ConstructorComparator());
        parameterizedConstructors = new ArrayList<>(Arrays.asList(constructors));

        if(constructors[0].isAnnotationPresent(Dependency.class))
        {
            if(constructors.length > 1 && constructors[1].isAnnotationPresent(Dependency.class))
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
