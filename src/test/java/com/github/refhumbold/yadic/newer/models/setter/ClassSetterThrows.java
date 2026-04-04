package com.github.refhumbold.yadic.newer.models.setter;

import com.github.refhumbold.yadic.annotation.YadicDependency;

public class ClassSetterThrows
{
    @YadicDependency
    public void setValue(int i)
    {
        throw new UnsupportedOperationException();
    }
}
