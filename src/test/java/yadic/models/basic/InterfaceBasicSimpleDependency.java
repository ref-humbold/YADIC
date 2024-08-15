package yadic.models.basic;

import yadic.models.diamond.InterfaceDiamondLeft;

public interface InterfaceBasicSimpleDependency
{
    InterfaceDiamondLeft getFirstObject();

    InterfaceBasicStringGetter getSecondObject();
}
