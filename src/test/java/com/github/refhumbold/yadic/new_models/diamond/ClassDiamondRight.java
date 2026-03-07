package com.github.refhumbold.yadic.new_models.diamond;

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
