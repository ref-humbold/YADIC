package yadic.models.setter;

import yadic.annotation.YadicDependency;

public class ClassSetterIncorrectReturnType
{
    public ClassSetterIncorrectReturnType()
    {
    }

    @YadicDependency
    public int method(int number)
    {
        return number + 1;
    }
}
