package com.github.refhumbold.yadic.models.setter;

import com.github.refhumbold.yadic.annotation.YadicDependency;

public class ClassSetterBoxed
{
    private Integer number;

    public Integer getNumber()
    {
        return number;
    }

    @YadicDependency
    public void setNumber(Integer number)
    {
        this.number = number;
    }
}
