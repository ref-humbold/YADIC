package ref_humbold.di_container.auxiliary.constructors;

import ref_humbold.di_container.auxiliary.basics.InterfaceBasics;

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
