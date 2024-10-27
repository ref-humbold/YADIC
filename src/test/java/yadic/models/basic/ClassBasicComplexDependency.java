package yadic.models.basic;

import yadic.annotation.YadicDependency;
import yadic.models.diamond.InterfaceDiamondLeft;

public class ClassBasicComplexDependency
        implements InterfaceBasicComplexDependency
{
    private InterfaceDiamondLeft firstObject;
    private InterfaceBasicStringGetter secondObject;
    private InterfaceBasic basicObject;

    @YadicDependency
    public ClassBasicComplexDependency(
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

    @Override
    public InterfaceBasic getBasicObject()
    {
        return basicObject;
    }

    @Override
    @YadicDependency
    public void setBasicObject(InterfaceBasic basicObject)
    {
        this.basicObject = basicObject;
    }
}
