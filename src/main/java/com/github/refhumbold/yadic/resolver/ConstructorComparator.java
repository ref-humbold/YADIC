package com.github.refhumbold.yadic.resolver;

import java.lang.reflect.Constructor;
import java.util.Comparator;
import com.github.refhumbold.yadic.annotation.YadicDependency;

class ConstructorComparator
        implements Comparator<Constructor<?>>
{
    @Override
    public int compare(Constructor<?> constructor0, Constructor<?> constructor1)
    {
        boolean isAnnotated0 = constructor0.isAnnotationPresent(YadicDependency.class);
        boolean isAnnotated1 = constructor1.isAnnotationPresent(YadicDependency.class);

        return isAnnotated0 && !isAnnotated1
               ? -1
               : isAnnotated1 && !isAnnotated0
                 ? 1
                 : Integer.compare(constructor1.getParameterCount(),
                         constructor0.getParameterCount());
    }
}
