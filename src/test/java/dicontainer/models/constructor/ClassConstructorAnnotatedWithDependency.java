package dicontainer.models.constructor;

import dicontainer.annotation.Dependency;
import dicontainer.models.basic.InterfaceBasicSimpleDependency;
import dicontainer.models.basic.InterfaceBasicStringGetter;
import dicontainer.models.diamond.InterfaceDiamondLeft;

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
