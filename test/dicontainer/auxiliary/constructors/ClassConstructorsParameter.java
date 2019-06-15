package dicontainer.auxiliary.constructors;

import dicontainer.auxiliary.basics.InterfaceBasics;

public class ClassConstructorsParameter
        implements InterfaceBasics
{
    private int number;

    public ClassConstructorsParameter(int number)
    {
        this.number = number;
    }

    public int getNumber()
    {
        return number;
    }
}
