package refhumbold.dicontainer.auxiliary.setters;

import refhumbold.dicontainer.annotation.Dependency;

public class ClassSettersIncorrect1
{
    public ClassSettersIncorrect1()
    {
    }

    @Dependency
    public int method(int number)
    {
        return number + 1;
    }
}
