package com.github.refhumbold.yadic.models.constructor;

import com.github.refhumbold.yadic.models.basic.InterfaceBasicSimpleDependency;
import com.github.refhumbold.yadic.models.basic.InterfaceBasicStringGetter;
import com.github.refhumbold.yadic.models.diamond.InterfaceDiamondLeft;

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
