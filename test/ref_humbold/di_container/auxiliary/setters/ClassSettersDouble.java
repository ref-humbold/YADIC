package ref_humbold.di_container.auxiliary.setters;

import ref_humbold.di_container.annotation.Dependency;
import ref_humbold.di_container.auxiliary.basics.InterfaceBasics;
import ref_humbold.di_container.auxiliary.basics.InterfaceBasicsStringGetter;

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
