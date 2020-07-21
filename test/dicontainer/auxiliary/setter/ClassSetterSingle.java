package dicontainer.auxiliary.setter;

import dicontainer.annotation.Dependency;
import dicontainer.auxiliary.basic.InterfaceBasic;

public class ClassSetterSingle
        implements InterfaceSetter
{
    private InterfaceBasic basicObject;

    public ClassSetterSingle()
    {
    }

    @Override
    public InterfaceBasic getBasicObject()
    {
        return basicObject;
    }

    @Override
    @Dependency
    public void setBasicObject(InterfaceBasic basicObject)
    {
        this.basicObject = basicObject;
    }
}
