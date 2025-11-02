package com.github.refhumbold.yadic.models.setter;

import com.github.refhumbold.yadic.annotation.YadicDependency;
import com.github.refhumbold.yadic.models.basic.InterfaceBasic;

public class ClassSetterConstructor
        implements InterfaceSetter
{
    private InterfaceBasic basicObject;

    public ClassSetterConstructor(InterfaceBasic basicObject)
    {
        this.basicObject = basicObject;
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
