package ref_humbold.di_container.auxiliary.setters;

import ref_humbold.di_container.annotation.Dependency;
import ref_humbold.di_container.auxiliary.basics.InterfaceBasics;

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
