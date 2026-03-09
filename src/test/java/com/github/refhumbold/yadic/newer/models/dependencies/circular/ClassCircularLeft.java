package com.github.refhumbold.yadic.newer.models.dependencies.circular;

public class ClassCircularLeft
        implements InterfaceCircularLeft
{
    private final InterfaceCircularRight object;

    public ClassCircularLeft(InterfaceCircularRight object)
    {
        this.object = object;
    }

    @Override
    public InterfaceCircularRight getObject()
    {
        return object;
    }
}
