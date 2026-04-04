package com.github.refhumbold.yadic.newer.models.setter;

import com.github.refhumbold.yadic.annotation.YadicDependency;

public class ClassSetterString
{
    private String string;

    public String getString()
    {
        return string;
    }

    @YadicDependency
    public void setString(String string)
    {
        this.string = string;
    }
}
