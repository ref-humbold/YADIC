package com.github.refhumbold.yadic.newer.models.dependencies.linear;

public class ClassLinearSecond
        implements InterfaceLinearSecond
{
    private final InterfaceLinearThird third;

    public ClassLinearSecond(InterfaceLinearThird third)
    {
        this.third = third;
    }

    @Override
    public InterfaceLinearThird getThird()
    {
        return third;
    }
}
