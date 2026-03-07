package com.github.refhumbold.yadic.new_models.circular;

public class ClassCircular
        implements InterfaceCircular
{
    private final InterfaceCircularLeft circular;

    public ClassCircular(InterfaceCircularLeft circular)
    {
        this.circular = circular;
    }

    @Override
    public InterfaceCircularLeft getCircular()
    {
        return circular;
    }
}
