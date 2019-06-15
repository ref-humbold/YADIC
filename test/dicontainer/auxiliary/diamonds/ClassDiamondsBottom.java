package dicontainer.auxiliary.diamonds;

import dicontainer.annotation.Dependency;

public class ClassDiamondsBottom
    implements InterfaceDiamondsBottom
{
    private InterfaceDiamonds1 diamond1;
    private InterfaceDiamonds2 diamond2;

    @Dependency
    public ClassDiamondsBottom(InterfaceDiamonds1 diamond1, InterfaceDiamonds2 diamond2)
    {
        this.diamond1 = diamond1;
        this.diamond2 = diamond2;
    }

    @Override
    public InterfaceDiamonds1 getDiamond1()
    {
        return diamond1;
    }

    @Override
    public InterfaceDiamonds2 getDiamond2()
    {
        return diamond2;
    }
}
