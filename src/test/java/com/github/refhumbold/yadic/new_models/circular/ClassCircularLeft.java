package com.github.refhumbold.yadic.new_models.circular;

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
