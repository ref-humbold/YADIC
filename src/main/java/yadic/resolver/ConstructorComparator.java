package yadic.resolver;

import java.lang.reflect.Constructor;
import java.util.Comparator;

import yadic.annotation.YadicDependency;

public class ConstructorComparator
        implements Comparator<Constructor<?>>
{
    @Override
    public int compare(Constructor<?> constructor0, Constructor<?> constructor1)
    {
        boolean isAnnotated0 = constructor0.isAnnotationPresent(YadicDependency.class);
        boolean isAnnotated1 = constructor1.isAnnotationPresent(YadicDependency.class);

        if(isAnnotated0 && !isAnnotated1)
            return -1;

        if(isAnnotated1 && !isAnnotated0)
            return 1;

        return Integer.compare(constructor1.getParameterCount(), constructor0.getParameterCount());
    }
}
