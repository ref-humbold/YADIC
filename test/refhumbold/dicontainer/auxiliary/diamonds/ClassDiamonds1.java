package refhumbold.dicontainer.auxiliary.diamonds;

public class ClassDiamonds1
    implements InterfaceDiamonds1
{
    private InterfaceDiamondsTop object;

    public ClassDiamonds1()
    {
        object = null;
    }

    public ClassDiamonds1(InterfaceDiamondsTop object)
    {
        this.object = object;
    }

    @Override
    public InterfaceDiamondsTop getObject()
    {
        return object;
    }
}
