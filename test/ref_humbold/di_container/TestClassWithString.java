package ref_humbold.di_container;

class TestClassWithString
    implements TestInterfaceWithString
{
    private String text;

    public TestClassWithString()
    {
        this.text = "";
    }

    public TestClassWithString(String text)
    {
        this.text = text;
    }

    @Override
    public String getString()
    {
        return text;
    }
}
