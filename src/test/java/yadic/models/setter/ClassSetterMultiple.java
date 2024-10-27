package yadic.models.setter;

import yadic.annotation.YadicDependency;
import yadic.models.basic.InterfaceBasic;
import yadic.models.basic.InterfaceBasicStringGetter;

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
    @YadicDependency
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
    @YadicDependency
    public void setStringObject(InterfaceBasicStringGetter stringObject)
    {
        this.stringObject = stringObject;
    }
}
