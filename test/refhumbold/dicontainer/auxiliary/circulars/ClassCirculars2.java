package refhumbold.dicontainer.auxiliary.circulars;

public class ClassCirculars2
    implements InterfaceCirculars2
{
    private InterfaceCirculars1 object;

    public ClassCirculars2(InterfaceCirculars1 object)
    {
        this.object = object;
    }

    @Override
    public InterfaceCirculars1 getObject()
    {
        return object;
    }
}
