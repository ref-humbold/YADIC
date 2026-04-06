package com.github.refhumbold.yadic.models.constructors.annotation;

import com.github.refhumbold.yadic.annotation.YadicDependency;
import com.github.refhumbold.yadic.models.dependencies.circular.ClassCircular;
import com.github.refhumbold.yadic.models.dependencies.linear.ClassLinear;

public class ClassAnnotatedConstructorOptionalCircular
{
    private final ClassLinear linear;
    private ClassCircular circular;

    @YadicDependency
    public ClassAnnotatedConstructorOptionalCircular(ClassLinear linear)
    {
        this.linear = linear;
    }

    public ClassAnnotatedConstructorOptionalCircular(ClassLinear linear, ClassCircular circular)
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
