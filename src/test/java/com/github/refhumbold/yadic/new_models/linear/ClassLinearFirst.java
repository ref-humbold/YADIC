package com.github.refhumbold.yadic.new_models.linear;

public class ClassLinearFirst
        implements InterfaceLinearFirst
{
    private final InterfaceLinearSecond second;

    public ClassLinearFirst(InterfaceLinearSecond second)
    {
        this.second = second;
    }

    @Override
    public InterfaceLinearSecond getSecond()
    {
        return second;
    }
}
