package ref_humbold.di_container.auxiliary.setters;

import ref_humbold.di_container.annotation.Dependency;

public class ClassSettersIncorrect2
{
    public ClassSettersIncorrect2()
    {
    }

    @Dependency
    public void setMethod()
    {
        System.out.println("");
    }
}
