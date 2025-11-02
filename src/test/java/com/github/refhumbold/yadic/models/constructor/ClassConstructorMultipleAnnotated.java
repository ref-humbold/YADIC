package com.github.refhumbold.yadic.models.constructor;

import com.github.refhumbold.yadic.annotation.YadicDependency;

public class ClassConstructorMultipleAnnotated
{
    private String text;

    @YadicDependency
    public ClassConstructorMultipleAnnotated()
    {
    }

    @YadicDependency
    public ClassConstructorMultipleAnnotated(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return text;
    }
}
