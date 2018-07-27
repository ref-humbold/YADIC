package ref_humbold.di_container.auxiliary.setters;

import ref_humbold.di_container.annotation.DependencySetter;
import ref_humbold.di_container.auxiliary.basics.TestInterfaceBasic;
import ref_humbold.di_container.auxiliary.basics.TestInterfaceWithString;

public class TestClassWithMultipleDependencySetters
    implements TestInterfaceMultipleDependencySetters
{
    private TestInterfaceBasic basicObject;
    private TestInterfaceWithString stringObject;

    public TestClassWithMultipleDependencySetters()
    {
    }

    @Override
    public TestInterfaceBasic getBasicObject()
    {
        return basicObject;
    }

    @Override
    @DependencySetter
    public void setBasicObject(TestInterfaceBasic basicObject)
    {
        this.basicObject = basicObject;
    }

    @Override
    public TestInterfaceWithString getStringObject()
    {
        return stringObject;
    }

    @Override
    @DependencySetter
    public void setStringObject(TestInterfaceWithString stringObject)
    {
        this.stringObject = stringObject;
    }
}
