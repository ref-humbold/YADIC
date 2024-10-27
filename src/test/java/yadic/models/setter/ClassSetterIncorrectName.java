package yadic.models.setter;

import yadic.annotation.YadicDependency;

public class ClassSetterIncorrectName
{
    private int number;

    public ClassSetterIncorrectName()
    {
    }

    @YadicDependency
    public void method(int number)
    {
        this.number = number;
    }
}
