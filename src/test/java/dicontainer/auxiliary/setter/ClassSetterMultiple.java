package dicontainer.auxiliary.setter;

import dicontainer.annotation.Dependency;
import dicontainer.auxiliary.basic.InterfaceBasic;
import dicontainer.auxiliary.basic.InterfaceBasicStringGetter;

public class ClassSetterMultiple
        implements InterfaceSetterMultiple
{
    private InterfaceBasic basicObject;
    private InterfaceBasicStringGetter stringObject;

    public ClassSetterMultiple()
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

    @Override
    public InterfaceBasicStringGetter getStringObject()
    {
        return stringObject;
    }

    @Override
    @Dependency
    public void setStringObject(InterfaceBasicStringGetter stringObject)
    {
        this.stringObject = stringObject;
    }
}
