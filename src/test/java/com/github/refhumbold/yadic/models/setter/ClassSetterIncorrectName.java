package com.github.refhumbold.yadic.models.setter;

import com.github.refhumbold.yadic.annotation.YadicDependency;

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
