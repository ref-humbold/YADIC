package dicontainer.auxiliary.constructor;

import dicontainer.annotation.Dependency;

public class ClassConstructorMultipleAnnotated
{
    private String text;

    @Dependency
    public ClassConstructorMultipleAnnotated()
    {
    }

    @Dependency
    public ClassConstructorMultipleAnnotated(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return text;
    }
}
