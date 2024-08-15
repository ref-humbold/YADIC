package yadic.models.constructor;

import yadic.annotation.Dependency;
import yadic.models.basic.InterfaceBasic;

public class ClassConstructorExceptionThrown
        implements InterfaceBasic
{
    @Dependency
    public ClassConstructorExceptionThrown()
            throws Exception
    {
        throw new Exception("Cannot construct");
    }
}
