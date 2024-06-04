package dicontainer.models.setter;

import dicontainer.annotation.Dependency;
import dicontainer.models.basic.InterfaceBasic;

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
