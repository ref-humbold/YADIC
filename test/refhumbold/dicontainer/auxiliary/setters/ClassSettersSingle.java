package refhumbold.dicontainer.auxiliary.setters;

import refhumbold.dicontainer.annotation.Dependency;
import refhumbold.dicontainer.auxiliary.basics.InterfaceBasics;

public class ClassSettersSingle
    implements InterfaceSetters
{
    InterfaceBasics basicObject;

    public ClassSettersSingle()
    {
    }

    @Override
    public InterfaceBasics getBasicObject()
    {
        return basicObject;
    }

    @Override
    @Dependency
    public void setBasicObject(InterfaceBasics basicObject)
    {
        this.basicObject = basicObject;
    }
}
