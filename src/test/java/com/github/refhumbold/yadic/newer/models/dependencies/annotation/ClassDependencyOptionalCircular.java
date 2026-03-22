package com.github.refhumbold.yadic.newer.models.dependencies.annotation;

import com.github.refhumbold.yadic.annotation.YadicDependency;
import com.github.refhumbold.yadic.newer.models.dependencies.circular.ClassCircular;
import com.github.refhumbold.yadic.newer.models.dependencies.linear.ClassLinear;

public class ClassDependencyOptionalCircular
{
    private final ClassLinear linear;
    private ClassCircular circular;

    @YadicDependency
    public ClassDependencyOptionalCircular(ClassLinear linear)
    {
        this.linear = linear;
    }

    public ClassDependencyOptionalCircular(ClassLinear linear, ClassCircular circular)
    {
        this.linear = linear;
        this.circular = circular;
    }

    public ClassLinear getLinear()
    {
        return linear;
    }

    public ClassCircular getCircular()
    {
        return circular;
    }
}
