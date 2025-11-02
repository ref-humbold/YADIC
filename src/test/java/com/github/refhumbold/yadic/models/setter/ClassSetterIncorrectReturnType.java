package com.github.refhumbold.yadic.models.setter;

import com.github.refhumbold.yadic.annotation.YadicDependency;

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
