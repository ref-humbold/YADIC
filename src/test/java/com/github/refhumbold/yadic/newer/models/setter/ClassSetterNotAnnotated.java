package com.github.refhumbold.yadic.newer.models.setter;

import com.github.refhumbold.yadic.newer.models.dependencies.linear.ClassLinear;
import com.github.refhumbold.yadic.newer.models.inheritance.ClassConcrete;

public class ClassSetterNotAnnotated
{
    private final ClassConcrete concrete;
    private ClassLinear linear;

    public ClassSetterNotAnnotated(ClassConcrete concrete)
    {
        this.concrete = concrete;
    }

    public ClassConcrete getConcrete()
    {
        return concrete;
    }

    public ClassLinear getLinear()
    {
        return linear;
    }

    public void setLinear(ClassLinear linear)
    {
        this.linear = linear;
    }
}
