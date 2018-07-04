package ref_humbold.di_container;

import ref_humbold.di_container.annotation.DependencySetter;

class TestClassWithDependencySetterAndConstructor
    implements TestInterfaceDependencySetter
{
    private TestInterfaceBasic basicObject;

    public TestClassWithDependencySetterAndConstructor(TestInterfaceBasic basicObject)
    {
        this.basicObject = basicObject;
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
