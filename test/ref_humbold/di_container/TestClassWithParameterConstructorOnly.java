package ref_humbold.di_container;

class TestClassWithParameterConstructorOnly
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
