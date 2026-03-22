package com.github.refhumbold.yadic.newer.models.dependencies.diamond;

public class ClassDiamond
{
    private final ClassDiamondLeft left;
    private final ClassDiamondRight right;

    public ClassDiamond(ClassDiamondLeft left, ClassDiamondRight right)
    {
        this.left = left;
        this.right = right;
    }

    public ClassDiamondLeft getLeft()
    {
        return left;
    }

    public ClassDiamondRight getRight()
    {
        return right;
    }
}
