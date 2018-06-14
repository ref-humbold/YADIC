package ref_humbold.di_container;

class TestClassDiamond2
    implements TestInterfaceDiamond2
{
    private TestInterfaceBasic object;

    public TestClassDiamond2(TestInterfaceBasic object)
    {
        this.object = object;
    }

    @Override
    public TestInterfaceBasic getObject()
    {
        return object;
    }
}
