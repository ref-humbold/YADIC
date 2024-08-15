package yadic.models.diamond;

public class ClassDiamondLeft
        implements InterfaceDiamondLeft
{
    private InterfaceDiamondTop object;

    public ClassDiamondLeft()
    {
        object = null;
    }

    public ClassDiamondLeft(InterfaceDiamondTop object)
    {
        this.object = object;
    }

    @Override
    public InterfaceDiamondTop getObject()
    {
        return object;
    }
}
