package com.github.refhumbold.yadic.newer.models.dependencies.circular;

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
