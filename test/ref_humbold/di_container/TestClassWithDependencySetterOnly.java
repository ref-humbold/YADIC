package ref_humbold.di_container;

class TestClassWithDependencySetterOnly
    implements TestInterfaceDependencySetter
{
    TestInterfaceBasic basicObject;

    public TestClassWithDependencySetterOnly()
    {
    }

    @Override
    public TestInterfaceBasic getBasicObject()
    {
        return basicObject;
    }

    @Override
    @DependencyMethod
    public void setBasicObject(TestInterfaceBasic basicObject)
    {
        this.basicObject = basicObject;
    }
}
