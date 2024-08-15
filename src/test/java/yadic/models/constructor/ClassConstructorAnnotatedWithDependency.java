package yadic.models.constructor;

import yadic.annotation.Dependency;
import yadic.models.basic.InterfaceBasicSimpleDependency;
import yadic.models.basic.InterfaceBasicStringGetter;
import yadic.models.diamond.InterfaceDiamondLeft;

public class ClassConstructorAnnotatedWithDependency
        implements InterfaceBasicSimpleDependency
{
    private InterfaceDiamondLeft firstObject;
    private InterfaceBasicStringGetter secondObject;

    @Dependency
    public ClassConstructorAnnotatedWithDependency(
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
