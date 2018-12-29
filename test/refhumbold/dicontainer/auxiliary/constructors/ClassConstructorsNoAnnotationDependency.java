package refhumbold.dicontainer.auxiliary.constructors;

import refhumbold.dicontainer.auxiliary.basics.InterfaceBasicsSimpleDependency;
import refhumbold.dicontainer.auxiliary.basics.InterfaceBasicsStringGetter;
import refhumbold.dicontainer.auxiliary.diamonds.InterfaceDiamonds1;

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
