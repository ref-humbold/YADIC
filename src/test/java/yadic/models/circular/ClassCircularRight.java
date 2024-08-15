package yadic.models.circular;

public class ClassCircularRight
        implements InterfaceCircularRight
{
    private InterfaceCircularLeft object;

    public ClassCircularRight(InterfaceCircularLeft object)
    {
        this.object = object;
    }

    @Override
    public InterfaceCircularLeft getObject()
    {
        return object;
    }
}
