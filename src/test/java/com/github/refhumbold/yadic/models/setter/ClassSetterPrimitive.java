package com.github.refhumbold.yadic.models.setter;

import com.github.refhumbold.yadic.annotation.YadicDependency;

public class ClassSetterPrimitive
{
    private int number;

    public int getNumber()
    {
        return number;
    }

    @YadicDependency
    public void setNumber(int number)
    {
        this.number = number;
    }
}
