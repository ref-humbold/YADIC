package refhumbold.dicontainer.auxiliary.constructors;

import refhumbold.dicontainer.annotation.Dependency;
import refhumbold.dicontainer.auxiliary.basics.InterfaceBasicsSimpleDependency;
import refhumbold.dicontainer.auxiliary.basics.InterfaceBasicsStringGetter;
import refhumbold.dicontainer.auxiliary.diamonds.InterfaceDiamonds1;

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
