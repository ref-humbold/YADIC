package com.github.refhumbold.yadic.models.dependencies.circular;

public class ClassCircular
{
    private final ClassCircularLeft circular;

    public ClassCircular(ClassCircularLeft circular)
    {
        this.circular = circular;
    }

    public ClassCircularLeft getCircular()
    {
        return circular;
    }
}
