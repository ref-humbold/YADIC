package ref_humbold.di_container.auxiliary.constructors;

import ref_humbold.di_container.annotation.Dependency;
import ref_humbold.di_container.auxiliary.basics.InterfaceBasicsSimpleDependency;
import ref_humbold.di_container.auxiliary.basics.InterfaceBasicsStringGetter;
import ref_humbold.di_container.auxiliary.diamonds.InterfaceDiamonds1;

public class ClassConstructorsAnnotationDependency
    implements InterfaceBasicsSimpleDependency
{
    private InterfaceDiamonds1 firstObject;
    private InterfaceBasicsStringGetter secondObject;

    @Dependency
    public ClassConstructorsAnnotationDependency(InterfaceDiamonds1 firstObject,
                                                 InterfaceBasicsStringGetter secondObject)
    {
        this.firstObject = firstObject;
        this.secondObject = secondObject;
    }

    @Override
    public InterfaceDiamonds1 getFirstObject()
    {
        return firstObject;
    }

    @Override
    public InterfaceBasicsStringGetter getSecondObject()
    {
        return secondObject;
    }
}
