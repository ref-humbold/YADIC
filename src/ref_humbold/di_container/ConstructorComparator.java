package ref_humbold.di_container;

import java.lang.reflect.Constructor;
import java.util.Comparator;

import ref_humbold.di_container.annotation.DependencyConstructor;

final class ConstructorComparator
    implements Comparator<Constructor<?>>
{
    @Override
    public int compare(Constructor<?> ctor0, Constructor<?> ctor1)
    {
        boolean ctorAnnotated0 = ctor0.isAnnotationPresent(DependencyConstructor.class);
        boolean ctorAnnotated1 = ctor1.isAnnotationPresent(DependencyConstructor.class);

        if(ctorAnnotated0 && !ctorAnnotated1)
            return -1;

        if(ctorAnnotated1 && !ctorAnnotated0)
            return 1;

        int ctorParams0 = ctor0.getParameterCount();
        int ctorParams1 = ctor1.getParameterCount();

        return Integer.compare(ctorParams1, ctorParams0);
    }
}
