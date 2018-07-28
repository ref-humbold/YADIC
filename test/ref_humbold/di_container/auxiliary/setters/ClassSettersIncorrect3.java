package ref_humbold.di_container.auxiliary.setters;

import ref_humbold.di_container.annotation.Dependency;

public class ClassSettersIncorrect3
{
    private int number;

    public ClassSettersIncorrect3()
    {
    }

    @Dependency
    public void method(int number)
    {
        this.number = number;
    }
}
