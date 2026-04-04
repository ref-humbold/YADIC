package com.github.refhumbold.yadic.newer.models.setter;

import com.github.refhumbold.yadic.annotation.YadicDependency;
import com.github.refhumbold.yadic.newer.models.inheritance.ClassConcrete;

public class ClassSetterConcreteClass
{
    private ClassConcrete concrete;

    public ClassConcrete getConcrete()
    {
        return concrete;
    }

    @YadicDependency
    public void setConcrete(ClassConcrete concrete)
    {
        this.concrete = concrete;
    }
}
