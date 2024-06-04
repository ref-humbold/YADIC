package dicontainer.models.setter;

import dicontainer.annotation.Dependency;
import dicontainer.models.basic.InterfaceBasic;
import dicontainer.models.basic.InterfaceBasicStringGetter;

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
    @Dependency
    public void setObjects(InterfaceBasic basicObject, InterfaceBasicStringGetter stringObject)
    {
        this.basicObject = basicObject;
        this.stringObject = stringObject;
    }
}
