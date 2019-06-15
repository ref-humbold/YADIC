package dicontainer.auxiliary.setters;

import dicontainer.annotation.Dependency;
import dicontainer.auxiliary.basics.InterfaceBasics;

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
