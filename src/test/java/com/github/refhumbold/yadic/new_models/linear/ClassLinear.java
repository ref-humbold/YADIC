package com.github.refhumbold.yadic.new_models.linear;

public class ClassLinear
        implements InterfaceLinear
{
    private final InterfaceLinearFirst first;

    public ClassLinear(InterfaceLinearFirst first)
    {
        this.first = first;
    }

    @Override
    public InterfaceLinearFirst getFirst()
    {
        return first;
    }
}
