package dicontainer.auxiliary.constructor;

import dicontainer.auxiliary.basic.InterfaceBasic;

public class ClassConstructorParameter
        implements InterfaceBasic
{
    private int number;

    public ClassConstructorParameter(int number)
    {
        this.number = number;
    }

    public int getNumber()
    {
        return number;
    }
}
