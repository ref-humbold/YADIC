package ref_humbold.di_container;

class TestClassComplexDependency
    implements TestInterfaceComplexDependency
{
    private TestInterfaceDiamond1 firstObject;
    private TestInterfaceWithString secondObject;
    private TestInterfaceBasic basicObject;

    @DependencyConstructor
    public TestClassComplexDependency(TestInterfaceDiamond1 firstObject,
                                      TestInterfaceWithString secondObject)
    {
        this.firstObject = firstObject;
        this.secondObject = secondObject;
    }

    @Override
    public TestInterfaceDiamond1 getFirstObject()
    {
        return firstObject;
    }

    @Override
    public TestInterfaceWithString getSecondObject()
    {
        return secondObject;
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
