package com.github.refhumbold.yadic.newer.models.dependencies.annotation;

import com.github.refhumbold.yadic.annotation.YadicDependency;
import com.github.refhumbold.yadic.newer.models.dependencies.diamond.ClassDiamond;
import com.github.refhumbold.yadic.newer.models.dependencies.linear.ClassLinear;

public class ClassDependencyAnnotation
{
    private ClassLinear linear;
    private ClassDiamond diamond;

    public ClassDependencyAnnotation()
    {
    }

    @YadicDependency
    public ClassDependencyAnnotation(ClassLinear linear)
    {
        this.linear = linear;
    }

    public ClassDependencyAnnotation(ClassLinear linear, ClassDiamond diamond)
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
