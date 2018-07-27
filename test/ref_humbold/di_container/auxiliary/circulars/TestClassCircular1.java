package ref_humbold.di_container.auxiliary.circulars;

public class TestClassCircular1
    implements TestInterfaceCircular1
{
    private TestInterfaceCircular2 object;

    public TestClassCircular1(TestInterfaceCircular2 object)
    {
        this.object = object;
    }

    @Override
    public TestInterfaceCircular2 getObject()
    {
        return object;
    }
}
