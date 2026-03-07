package com.github.refhumbold.yadic.new_models.circular;

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
