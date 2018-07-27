package ref_humbold.di_container.auxiliary.constructors;

import ref_humbold.di_container.auxiliary.basics.TestInterfaceBasic;

public class TestClassWithParameterConstructorOnly
    implements TestInterfaceBasic
{
    private int number;

    public TestClassWithParameterConstructorOnly(int number)
    {
        this.number = number;
    }

    public int getNumber()
    {
        return number;
    }
}
