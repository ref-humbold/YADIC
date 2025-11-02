package com.github.refhumbold.yadic.models.constructor;

import com.github.refhumbold.yadic.models.basic.InterfaceBasic;

public class ClassConstructorDefaultAndParameterized
        implements InterfaceBasic
{
    private String text;

    public ClassConstructorDefaultAndParameterized()
    {
        text = "";
    }

    public ClassConstructorDefaultAndParameterized(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return text;
    }
}
