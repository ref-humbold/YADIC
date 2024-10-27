package yadic.models.setter;

import yadic.annotation.YadicDependency;
import yadic.models.basic.InterfaceBasic;
import yadic.models.basic.InterfaceBasicStringGetter;

public class ClassSetterMultipleParameters
        implements InterfaceSetterMultipleParameters
{
    private InterfaceBasic basicObject;
    private InterfaceBasicStringGetter stringObject;

    public ClassSetterMultipleParameters()
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
    @YadicDependency
    public void setObjects(InterfaceBasic basicObject, InterfaceBasicStringGetter stringObject)
    {
        this.basicObject = basicObject;
        this.stringObject = stringObject;
    }
}
