package dicontainer.auxiliary.constructors;

import dicontainer.auxiliary.basics.InterfaceBasicsSimpleDependency;
import dicontainer.auxiliary.basics.InterfaceBasicsStringGetter;
import dicontainer.auxiliary.diamonds.InterfaceDiamonds1;

public class ClassConstructorsNoAnnotationDependency
        implements InterfaceBasicsSimpleDependency
{
    private InterfaceDiamonds1 firstObject;
    private InterfaceBasicsStringGetter secondObject;

    public ClassConstructorsNoAnnotationDependency(InterfaceDiamonds1 firstObject)
    {
        this.firstObject = firstObject;
        secondObject = null;
    }

    public ClassConstructorsNoAnnotationDependency(InterfaceDiamonds1 firstObject,
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
