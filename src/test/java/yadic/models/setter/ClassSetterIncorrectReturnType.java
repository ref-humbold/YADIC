package yadic.models.setter;

import yadic.annotation.Dependency;

public class ClassSetterIncorrectReturnType
{
    public ClassSetterIncorrectReturnType()
    {
    }

    @Dependency
    public int method(int number)
    {
        return number + 1;
    }
}
