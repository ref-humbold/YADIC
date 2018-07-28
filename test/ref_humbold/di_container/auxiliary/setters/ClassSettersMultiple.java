package ref_humbold.di_container.auxiliary.setters;

import ref_humbold.di_container.annotation.Dependency;
import ref_humbold.di_container.auxiliary.basics.InterfaceBasics;
import ref_humbold.di_container.auxiliary.basics.InterfaceBasicsStringGetter;

public class ClassSettersMultiple
    implements InterfaceSettersMultiple
{
    private InterfaceBasics basicObject;
    private InterfaceBasicsStringGetter stringObject;

    public ClassSettersMultiple()
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

    @Override
    public InterfaceBasicsStringGetter getStringObject()
    {
        return stringObject;
    }

    @Override
    @Dependency
    public void setStringObject(InterfaceBasicsStringGetter stringObject)
    {
        this.stringObject = stringObject;
    }
}
