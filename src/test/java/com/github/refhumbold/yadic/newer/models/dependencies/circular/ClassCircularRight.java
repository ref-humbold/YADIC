package com.github.refhumbold.yadic.newer.models.dependencies.circular;

public class ClassCircularRight
        implements InterfaceCircularRight
{
    private final InterfaceCircularLeft object;

    public ClassCircularRight(InterfaceCircularLeft object)
    {
        this.object = object;
    }

    @Override
    public InterfaceCircularLeft getObject()
    {
        return object;
    }
}
