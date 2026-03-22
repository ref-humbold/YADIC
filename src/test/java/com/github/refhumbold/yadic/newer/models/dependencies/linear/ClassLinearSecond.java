package com.github.refhumbold.yadic.newer.models.dependencies.linear;

public class ClassLinearSecond
{
    private final ClassLinearThird third;

    public ClassLinearSecond(ClassLinearThird third)
    {
        this.third = third;
    }

    public ClassLinearThird getThird()
    {
        return third;
    }
}
