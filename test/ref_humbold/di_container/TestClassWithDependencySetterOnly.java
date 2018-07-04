package ref_humbold.di_container;

import ref_humbold.di_container.annotation.DependencySetter;

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
    @DependencySetter
    public void setBasicObject(TestInterfaceBasic basicObject)
    {
        this.basicObject = basicObject;
    }
}
