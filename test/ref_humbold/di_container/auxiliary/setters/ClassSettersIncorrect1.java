package ref_humbold.di_container.auxiliary.setters;

import ref_humbold.di_container.annotation.Dependency;

public class ClassSettersIncorrect1
{
    public ClassSettersIncorrect1()
    {
    }

    @Dependency
    public int method(int number)
    {
        return number + 1;
    }
}
