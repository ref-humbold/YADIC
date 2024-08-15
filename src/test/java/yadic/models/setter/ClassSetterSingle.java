package yadic.models.setter;

import yadic.annotation.Dependency;
import yadic.models.basic.InterfaceBasic;

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
