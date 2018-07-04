package ref_humbold.di_container;

import ref_humbold.di_container.annotation.DependencySetter;

class TestClassWithDoubleDependencySetter
    implements TestInterfaceDoubleDependencySetter
{
    private TestInterfaceBasic basicObject;
    private TestInterfaceWithString stringObject;

    public TestClassWithDoubleDependencySetter()
    {
    }

    @Override
    public TestInterfaceBasic getBasicObject()
    {
        return basicObject;
    }

    @Override
    public TestInterfaceWithString getStringObject()
    {
        return stringObject;
    }

    @Override
    @DependencySetter
    public void setObjects(TestInterfaceBasic basicObject, TestInterfaceWithString stringObject)
    {
        this.basicObject = basicObject;
        this.stringObject = stringObject;
    }
}
