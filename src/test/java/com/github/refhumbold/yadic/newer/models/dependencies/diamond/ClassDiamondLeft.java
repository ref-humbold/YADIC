package com.github.refhumbold.yadic.newer.models.dependencies.diamond;

public class ClassDiamondLeft
        implements InterfaceDiamondLeft
{
    private final InterfaceDiamondTop top;

    public ClassDiamondLeft(InterfaceDiamondTop top)
    {
        this.top = top;
    }

    @Override
    public InterfaceDiamondTop getTop()
    {
        return top;
    }
}
