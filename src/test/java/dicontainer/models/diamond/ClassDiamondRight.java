package dicontainer.models.diamond;

public class ClassDiamondRight
        implements InterfaceDiamondRight
{
    private InterfaceDiamondTop object;

    public ClassDiamondRight(InterfaceDiamondTop object)
    {
        this.object = object;
    }

    @Override
    public InterfaceDiamondTop getObject()
    {
        return object;
    }
}
