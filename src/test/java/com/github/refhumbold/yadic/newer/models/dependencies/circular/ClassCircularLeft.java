package com.github.refhumbold.yadic.newer.models.dependencies.circular;

public class ClassCircularLeft
{
    private final ClassCircularRight object;

    public ClassCircularLeft(ClassCircularRight object)
    {
        this.object = object;
    }

    public ClassCircularRight getObject()
    {
        return object;
    }
}
