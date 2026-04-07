package com.github.refhumbold.yadic.models.dependencies.circular;

public class ClassCircularRight
{
    private final ClassCircularLeft object;

    public ClassCircularRight(ClassCircularLeft object)
    {
        this.object = object;
    }

    public ClassCircularLeft getObject()
    {
        return object;
    }
}
