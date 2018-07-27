package ref_humbold.di_container.auxiliary.circulars;

public class TestClassCircular2
    implements TestInterfaceCircular2
{
    private TestInterfaceCircular1 object;

    public TestClassCircular2(TestInterfaceCircular1 object)
    {
        this.object = object;
    }

    @Override
    public TestInterfaceCircular1 getObject()
    {
        return object;
    }
}
