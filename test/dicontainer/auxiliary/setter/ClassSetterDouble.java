package dicontainer.auxiliary.setter;

import dicontainer.annotation.Dependency;
import dicontainer.auxiliary.basic.InterfaceBasic;
import dicontainer.auxiliary.basic.InterfaceBasicStringGetter;

public class ClassSetterDouble
        implements InterfaceSetterDouble
{
    private InterfaceBasic basicObject;
    private InterfaceBasicStringGetter stringObject;

    public ClassSetterDouble()
    {
    }

    @Override
    public InterfaceBasic getBasicObject()
    {
        return basicObject;
    }

    @Override
    public InterfaceBasicStringGetter getStringObject()
    {
        return stringObject;
    }

    @Override
    @Dependency
    public void setObjects(InterfaceBasic basicObject, InterfaceBasicStringGetter stringObject)
    {
        this.basicObject = basicObject;
        this.stringObject = stringObject;
    }
}
