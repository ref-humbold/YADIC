package com.github.refhumbold.yadic.new_models.diamond;

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
