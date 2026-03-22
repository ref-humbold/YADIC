package com.github.refhumbold.yadic.newer.models.dependencies.diamond;

public class ClassDiamondLeft
{
    private final ClassDiamondTop top;

    public ClassDiamondLeft(ClassDiamondTop top)
    {
        this.top = top;
    }

    public ClassDiamondTop getTop()
    {
        return top;
    }
}
