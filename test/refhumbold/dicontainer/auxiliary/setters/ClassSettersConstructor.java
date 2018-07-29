package refhumbold.dicontainer.auxiliary.setters;

import refhumbold.dicontainer.annotation.Dependency;
import refhumbold.dicontainer.auxiliary.basics.InterfaceBasics;

public class ClassSettersConstructor
    implements InterfaceSetters
{
    private InterfaceBasics basicObject;

    public ClassSettersConstructor(InterfaceBasics basicObject)
    {
        this.basicObject = basicObject;
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
