package yadic.models.diamond;

import yadic.annotation.YadicDependency;

public class ClassDiamondBottom
        implements InterfaceDiamondBottom
{
    private InterfaceDiamondLeft diamond1;
    private InterfaceDiamondRight diamond2;

    @YadicDependency
    public ClassDiamondBottom(InterfaceDiamondLeft diamond1, InterfaceDiamondRight diamond2)
    {
        this.diamond1 = diamond1;
        this.diamond2 = diamond2;
    }

    @Override
    public InterfaceDiamondLeft getDiamond1()
    {
        return diamond1;
    }

    @Override
    public InterfaceDiamondRight getDiamond2()
    {
        return diamond2;
    }
}
