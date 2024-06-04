package dicontainer.models.constructor;

import dicontainer.annotation.Dependency;
import dicontainer.models.basic.InterfaceBasic;

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
