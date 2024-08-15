package yadic.models.setter;

import yadic.annotation.Dependency;

public class ClassSetterIncorrectName
{
    private int number;

    public ClassSetterIncorrectName()
    {
    }

    @Dependency
    public void method(int number)
    {
        this.number = number;
    }
}
