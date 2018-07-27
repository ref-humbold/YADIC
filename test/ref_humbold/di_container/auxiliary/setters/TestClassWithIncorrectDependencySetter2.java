package ref_humbold.di_container.auxiliary.setters;

import ref_humbold.di_container.annotation.DependencySetter;

public class TestClassWithIncorrectDependencySetter2
{
    public TestClassWithIncorrectDependencySetter2()
    {
    }

    @DependencySetter
    public void setMethod()
    {
        System.out.println("");
    }
}
