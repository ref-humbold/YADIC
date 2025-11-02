package com.github.refhumbold.yadic.models.constructor;

import com.github.refhumbold.yadic.annotation.YadicDependency;
import com.github.refhumbold.yadic.models.basic.InterfaceBasic;

public class ClassConstructorExceptionThrown
        implements InterfaceBasic
{
    @YadicDependency
    public ClassConstructorExceptionThrown()
            throws Exception
    {
        throw new Exception("Cannot construct");
    }
}
