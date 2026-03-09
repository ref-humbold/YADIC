package com.github.refhumbold.yadic.newer.models.dependencies.diamond;

public class ClassDiamondRight
        implements InterfaceDiamondRight
{
    private final InterfaceDiamondTop top;

    public ClassDiamondRight(InterfaceDiamondTop top)
    {
        this.top = top;
    }

    @Override
    public InterfaceDiamondTop getTop()
    {
        return top;
    }
}
