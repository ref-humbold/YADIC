package dicontainer.auxiliary.setters;

import dicontainer.annotation.Dependency;

public class ClassSettersIncorrect3
{
    private int number;

    public ClassSettersIncorrect3()
    {
    }

    @Dependency
    public void method(int number)
    {
        this.number = number;
    }
}
