package com.github.refhumbold.yadic.models.setter;

import com.github.refhumbold.yadic.annotation.YadicDependency;

public class ClassSetterInvalidReturnType
{
    @YadicDependency
    public String setString(String s)
    {
        return s;
    }
}
