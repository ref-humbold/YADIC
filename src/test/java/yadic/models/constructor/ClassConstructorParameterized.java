package yadic.models.constructor;

import yadic.models.basic.InterfaceBasic;

public class ClassConstructorParameterized
        implements InterfaceBasic
{
    private int number;

    public ClassConstructorParameterized(int number)
    {
        this.number = number;
    }

    public int getNumber()
    {
        return number;
    }
}
