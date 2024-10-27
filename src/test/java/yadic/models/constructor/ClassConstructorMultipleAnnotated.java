package yadic.models.constructor;

import yadic.annotation.YadicDependency;

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
