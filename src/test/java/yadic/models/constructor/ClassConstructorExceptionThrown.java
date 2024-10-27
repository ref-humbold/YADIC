package yadic.models.constructor;

import yadic.annotation.YadicDependency;
import yadic.models.basic.InterfaceBasic;

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
