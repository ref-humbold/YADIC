package refhumbold.dicontainer.auxiliary.circulars;

public class ClassCirculars1
    implements InterfaceCirculars1
{
    private InterfaceCirculars2 object;

    public ClassCirculars1(InterfaceCirculars2 object)
    {
        this.object = object;
    }

    @Override
    public InterfaceCirculars2 getObject()
    {
        return object;
    }
}
