package dicontainer.auxiliary.constructors;

import dicontainer.annotation.Dependency;
import dicontainer.auxiliary.basics.InterfaceBasicsSimpleDependency;
import dicontainer.auxiliary.basics.InterfaceBasicsStringGetter;
import dicontainer.auxiliary.diamonds.InterfaceDiamonds1;

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
