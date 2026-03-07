package com.github.refhumbold.yadic.new_models.diamond;

public class ClassDiamond
        implements InterfaceDiamond
{
    private final InterfaceDiamondLeft left;
    private final InterfaceDiamondRight right;

    public ClassDiamond(InterfaceDiamondLeft left, InterfaceDiamondRight right)
    {
        this.left = left;
        this.right = right;
    }

    @Override
    public InterfaceDiamondLeft getLeft()
    {
        return left;
    }

    @Override
    public InterfaceDiamondRight getRight()
    {
        return right;
    }
}
