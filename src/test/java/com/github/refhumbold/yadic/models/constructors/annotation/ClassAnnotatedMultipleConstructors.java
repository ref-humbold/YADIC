package com.github.refhumbold.yadic.models.constructors.annotation;

import com.github.refhumbold.yadic.annotation.YadicDependency;

public class ClassAnnotatedMultipleConstructors
{
    private final String string;

    @YadicDependency
    public ClassAnnotatedMultipleConstructors()
    {
        string = "";
    }

    @YadicDependency
    public ClassAnnotatedMultipleConstructors(String string)
    {
        this.string = string;
    }

    public String getString()
    {
        return string;
    }
}
