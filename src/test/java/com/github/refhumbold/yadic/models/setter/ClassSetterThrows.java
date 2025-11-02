package com.github.refhumbold.yadic.models.setter;

import com.github.refhumbold.yadic.annotation.YadicDependency;

public class ClassSetterThrows
{
    @YadicDependency
    public void setString(String string)
            throws Exception
    {
        throw new Exception();
    }
}
