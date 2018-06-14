package ref_humbold.di_container;

class TestClassSimpleDependencyWithoutAnnotation
    implements TestInterfaceSimpleDependency
{
    private TestInterfaceDiamond1 firstObject;
    private TestInterfaceWithString secondObject;

    public TestClassSimpleDependencyWithoutAnnotation(TestInterfaceDiamond1 firstObject)
    {
        this.firstObject = firstObject;
        this.secondObject = null;
    }

    public TestClassSimpleDependencyWithoutAnnotation(TestInterfaceDiamond1 firstObject,
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
}
