package com.github.refhumbold.yadic.newer.models.setter;

import com.github.refhumbold.yadic.annotation.YadicDependency;
import com.github.refhumbold.yadic.newer.models.dependencies.linear.ClassLinear;
import com.github.refhumbold.yadic.newer.models.inheritance.ClassConcrete;

public class ClassSetterMultipleSetters
{
    private ClassConcrete concrete;
    private ClassLinear linear;

    public ClassConcrete getConcrete()
    {
        return concrete;
    }

    @YadicDependency
    public void setConcrete(ClassConcrete concrete)
    {
        this.concrete = concrete;
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
