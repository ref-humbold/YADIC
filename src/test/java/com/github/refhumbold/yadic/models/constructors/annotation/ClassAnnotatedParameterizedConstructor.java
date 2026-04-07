package com.github.refhumbold.yadic.models.constructors.annotation;

import com.github.refhumbold.yadic.annotation.YadicDependency;

public class ClassAnnotatedParameterizedConstructor
{
    private final String string;

    public ClassAnnotatedParameterizedConstructor()
    {
        string = "";
    }

    @YadicDependency
    public ClassAnnotatedParameterizedConstructor(String string)
    {
        this.string = string;
    }

    public String getString()
    {
        return string;
    }
}
