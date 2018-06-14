package ref_humbold.di_container;

import ref_humbold.di_container.DependencyConstructor;

class TestClassSimpleDependencyWithAnnotation
    implements TestInterfaceSimpleDependency
{
    private TestInterfaceDiamond1 firstObject;
    private TestInterfaceWithString secondObject;

    @DependencyConstructor
    public TestClassSimpleDependencyWithAnnotation(TestInterfaceDiamond1 firstObject,
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
