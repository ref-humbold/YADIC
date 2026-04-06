package com.github.refhumbold.yadic.models.constructors.annotation;

import com.github.refhumbold.yadic.annotation.YadicDependency;
import com.github.refhumbold.yadic.models.dependencies.diamond.ClassDiamond;
import com.github.refhumbold.yadic.models.dependencies.linear.ClassLinear;

public class ClassAnnotatedConstructor
{
    private ClassLinear linear;
    private ClassDiamond diamond;

    public ClassAnnotatedConstructor()
    {
    }

    @YadicDependency
    public ClassAnnotatedConstructor(ClassLinear linear)
    {
        this.linear = linear;
    }

    public ClassAnnotatedConstructor(ClassLinear linear, ClassDiamond diamond)
    {
        this.linear = linear;
        this.diamond = diamond;
    }

    public ClassLinear getLinear()
    {
        return linear;
    }

    public ClassDiamond getDiamond()
    {
        return diamond;
    }
}
