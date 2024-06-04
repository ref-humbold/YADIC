package dicontainer.models.basic;

import dicontainer.models.diamond.InterfaceDiamondLeft;

public interface InterfaceBasicSimpleDependency
{
    InterfaceDiamondLeft getFirstObject();

    InterfaceBasicStringGetter getSecondObject();
}
