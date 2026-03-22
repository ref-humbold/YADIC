package com.github.refhumbold.yadic.newer.models.dependencies.diamond;

public class ClassDiamondRight
{
    private final ClassDiamondTop top;

    public ClassDiamondRight(ClassDiamondTop top)
    {
        this.top = top;
    }

    public ClassDiamondTop getTop()
    {
        return top;
    }
}
