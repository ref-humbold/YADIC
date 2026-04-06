package com.github.refhumbold.yadic.models.setter;

import com.github.refhumbold.yadic.models.dependencies.linear.ClassLinear;
import com.github.refhumbold.yadic.models.inheritance.ClassConcrete;

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
