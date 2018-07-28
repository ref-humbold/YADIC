package ref_humbold.di_container.auxiliary.constructors;

import ref_humbold.di_container.annotation.Dependency;

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
