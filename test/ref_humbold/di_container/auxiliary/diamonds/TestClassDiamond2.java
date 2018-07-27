package ref_humbold.di_container.auxiliary.diamonds;

import ref_humbold.di_container.auxiliary.basics.TestInterfaceBasic;

public class TestClassDiamond2
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
