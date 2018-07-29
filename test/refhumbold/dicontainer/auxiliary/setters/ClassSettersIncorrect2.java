package refhumbold.dicontainer.auxiliary.setters;

import refhumbold.dicontainer.annotation.Dependency;

public class ClassSettersIncorrect2
{
    public ClassSettersIncorrect2()
    {
    }

    @Dependency
    public void setMethod()
    {
        System.out.println("");
    }
}
