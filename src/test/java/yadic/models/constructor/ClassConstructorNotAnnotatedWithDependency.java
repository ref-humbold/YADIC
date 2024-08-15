package yadic.models.constructor;

import yadic.models.basic.InterfaceBasicSimpleDependency;
import yadic.models.basic.InterfaceBasicStringGetter;
import yadic.models.diamond.InterfaceDiamondLeft;

public class ClassConstructorNotAnnotatedWithDependency
        implements InterfaceBasicSimpleDependency
{
    private InterfaceDiamondLeft firstObject;
    private InterfaceBasicStringGetter secondObject;

    public ClassConstructorNotAnnotatedWithDependency(InterfaceDiamondLeft firstObject)
    {
        this.firstObject = firstObject;
        secondObject = null;
    }

    public ClassConstructorNotAnnotatedWithDependency(
            InterfaceDiamondLeft firstObject, InterfaceBasicStringGetter secondObject)
    {
        this.firstObject = firstObject;
        this.secondObject = secondObject;
    }

    @Override
    public InterfaceDiamondLeft getFirstObject()
    {
        return firstObject;
    }

    @Override
    public InterfaceBasicStringGetter getSecondObject()
    {
        return secondObject;
    }
}
