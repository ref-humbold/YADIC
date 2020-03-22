package dicontainer.auxiliary.constructor;

import dicontainer.annotation.Dependency;
import dicontainer.auxiliary.basic.InterfaceBasicSimpleDependency;
import dicontainer.auxiliary.basic.InterfaceBasicStringGetter;
import dicontainer.auxiliary.diamond.InterfaceDiamondLeft;

public class ClassConstructorAnnotatedWithDependency
        implements InterfaceBasicSimpleDependency
{
    private InterfaceDiamondLeft firstObject;
    private InterfaceBasicStringGetter secondObject;

    @Dependency
    public ClassConstructorAnnotatedWithDependency(InterfaceDiamondLeft firstObject,
                                                   InterfaceBasicStringGetter secondObject)
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
