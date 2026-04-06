package com.github.refhumbold.yadic.models.constructors.annotation;

import com.github.refhumbold.yadic.annotation.YadicDependency;

public class ClassAnnotatedDefaultConstructor
{
    private final String string;

    @YadicDependency
    public ClassAnnotatedDefaultConstructor()
    {
        string = "";
    }

    public ClassAnnotatedDefaultConstructor(String string)
    {
        this.string = string;
    }

    public String getString()
    {
        return string;
    }
}
