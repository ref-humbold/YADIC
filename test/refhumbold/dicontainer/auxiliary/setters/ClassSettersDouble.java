package refhumbold.dicontainer.auxiliary.setters;

import refhumbold.dicontainer.annotation.Dependency;
import refhumbold.dicontainer.auxiliary.basics.InterfaceBasics;
import refhumbold.dicontainer.auxiliary.basics.InterfaceBasicsStringGetter;

public class ClassSettersDouble
    implements InterfaceSettersDouble
{
    private InterfaceBasics basicObject;
    private InterfaceBasicsStringGetter stringObject;

    public ClassSettersDouble()
    {
    }

    @Override
    public InterfaceBasics getBasicObject()
    {
        return basicObject;
    }

    @Override
    public InterfaceBasicsStringGetter getStringObject()
    {
        return stringObject;
    }

    @Override
    @Dependency
    public void setObjects(InterfaceBasics basicObject, InterfaceBasicsStringGetter stringObject)
    {
        this.basicObject = basicObject;
        this.stringObject = stringObject;
    }
}
