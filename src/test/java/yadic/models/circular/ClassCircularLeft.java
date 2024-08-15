package yadic.models.circular;

public class ClassCircularLeft
        implements InterfaceCircularLeft
{
    private InterfaceCircularRight object;

    public ClassCircularLeft(InterfaceCircularRight object)
    {
        this.object = object;
    }

    @Override
    public InterfaceCircularRight getObject()
    {
        return object;
    }
}
