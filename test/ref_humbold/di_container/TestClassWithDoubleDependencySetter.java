package ref_humbold.di_container;

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
    @DependencyMethod
    public void setObjects(TestInterfaceBasic basicObject, TestInterfaceWithString stringObject)
    {
        this.basicObject = basicObject;
        this.stringObject = stringObject;
    }
}
