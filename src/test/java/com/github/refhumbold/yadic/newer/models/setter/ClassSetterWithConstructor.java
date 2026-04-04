package com.github.refhumbold.yadic.newer.models.setter;

import com.github.refhumbold.yadic.annotation.YadicDependency;
import com.github.refhumbold.yadic.newer.models.dependencies.linear.ClassLinear;
import com.github.refhumbold.yadic.newer.models.inheritance.ClassConcrete;

public class ClassSetterWithConstructor
{
    private final ClassConcrete concrete;
    private ClassLinear linear;

    @YadicDependency
    public ClassSetterWithConstructor(ClassConcrete concrete)
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

    @YadicDependency
    public void setLinear(ClassLinear linear)
    {
        this.linear = linear;
    }
}
