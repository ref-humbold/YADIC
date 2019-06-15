package dicontainer.auxiliary.diamonds;

public class ClassDiamonds2
    implements InterfaceDiamonds2
{
    private InterfaceDiamondsTop object;

    public ClassDiamonds2(InterfaceDiamondsTop object)
    {
        this.object = object;
    }

    @Override
    public InterfaceDiamondsTop getObject()
    {
        return object;
    }
}
