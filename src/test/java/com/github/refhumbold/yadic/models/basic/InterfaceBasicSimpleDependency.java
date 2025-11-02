package com.github.refhumbold.yadic.models.basic;

import com.github.refhumbold.yadic.models.diamond.InterfaceDiamondLeft;

public interface InterfaceBasicSimpleDependency
{
    InterfaceDiamondLeft getFirstObject();

    InterfaceBasicStringGetter getSecondObject();
}
