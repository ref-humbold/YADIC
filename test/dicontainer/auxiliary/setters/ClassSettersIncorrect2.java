package dicontainer.auxiliary.setters;

import dicontainer.annotation.Dependency;

public class ClassSettersIncorrect2
{
    public ClassSettersIncorrect2()
    {
    }

    @Dependency
    public void setMethod()
    {
        System.out.println();
    }
}
