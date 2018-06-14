package ref_humbold.di_container;

class TestClassCircularDependency
    implements TestInterfaceCircularDependency
{
    TestInterfaceCircular1 circular;
    TestInterfaceWithString nonCircular;

    public TestClassCircularDependency(TestInterfaceCircular1 circular,
                                       TestInterfaceWithString nonCircular)
    {
        this.circular = circular;
        this.nonCircular = nonCircular;
    }

    public TestClassCircularDependency(TestInterfaceWithString nonCircular)
    {
        this.nonCircular = nonCircular;
    }

    @Override
    public TestInterfaceWithString getNonCircularObject()
    {
        return nonCircular;
    }

    @Override
    public TestInterfaceCircular1 getCircularObject()
    {
        return circular;
    }
}
