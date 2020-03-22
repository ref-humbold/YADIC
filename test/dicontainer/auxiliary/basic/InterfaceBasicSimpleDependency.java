package dicontainer.auxiliary.basic;

import dicontainer.auxiliary.diamond.InterfaceDiamondLeft;

public interface InterfaceBasicSimpleDependency
{
    InterfaceDiamondLeft getFirstObject();

    InterfaceBasicStringGetter getSecondObject();
}
