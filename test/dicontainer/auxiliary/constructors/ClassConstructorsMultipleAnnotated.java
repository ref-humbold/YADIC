package dicontainer.auxiliary.constructors;

import dicontainer.annotation.Dependency;

public class ClassConstructorsMultipleAnnotated
{
    private String text;

    @Dependency
    public ClassConstructorsMultipleAnnotated()
    {
    }

    @Dependency
    public ClassConstructorsMultipleAnnotated(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return text;
    }
}
