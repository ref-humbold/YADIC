package com.github.refhumbold.yadic.models.dependencies.linear;

public class ClassLinearFirst
{
    private final ClassLinearSecond second;

    public ClassLinearFirst(ClassLinearSecond second)
    {
        this.second = second;
    }

    public ClassLinearSecond getSecond()
    {
        return second;
    }
}
