package dicontainer.auxiliary.constructor;

import dicontainer.annotation.Dependency;
import dicontainer.auxiliary.basic.InterfaceBasic;

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
