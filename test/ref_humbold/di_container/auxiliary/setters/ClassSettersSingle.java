package ref_humbold.di_container.auxiliary.setters;

import ref_humbold.di_container.annotation.Dependency;
import ref_humbold.di_container.auxiliary.basics.InterfaceBasics;

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
