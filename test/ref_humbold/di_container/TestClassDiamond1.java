package ref_humbold.di_container;

class TestClassDiamond1
    implements TestInterfaceDiamond1
{
    private TestInterfaceBasic object;

    public TestClassDiamond1()
    {
        this.object = null;
    }

    public TestClassDiamond1(TestInterfaceBasic object)
    {
        this.object = object;
    }

    @Override
    public TestInterfaceBasic getObject()
    {
        return object;
    }
}
