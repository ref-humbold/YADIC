package dicontainer.auxiliary.setters;

import dicontainer.annotation.Dependency;

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
