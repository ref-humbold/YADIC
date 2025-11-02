package com.github.refhumbold.yadic.models.setter;

import com.github.refhumbold.yadic.annotation.YadicDependency;

public class ClassSetterWithoutParameters
{
    public ClassSetterWithoutParameters()
    {
    }

    @YadicDependency
    public void setMethod()
    {
        System.out.println();
    }
}
