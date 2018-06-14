package ref_humbold.di_container;

import ref_humbold.di_container.DependencyConstructor;

class TestClassDiamondBase
    implements TestInterfaceDiamondBase
{
    private TestInterfaceDiamond1 diamond1;
    private TestInterfaceDiamond2 diamond2;

    @DependencyConstructor
    public TestClassDiamondBase(TestInterfaceDiamond1 diamond1, TestInterfaceDiamond2 diamond2)
    {
        this.diamond1 = diamond1;
        this.diamond2 = diamond2;
    }

    @Override
    public TestInterfaceDiamond1 getDiamond1()
    {
        return diamond1;
    }

    @Override
    public TestInterfaceDiamond2 getDiamond2()
    {
        return diamond2;
    }
}
