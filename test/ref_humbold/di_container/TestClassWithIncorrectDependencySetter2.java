package ref_humbold.di_container;

import ref_humbold.di_container.annotation.DependencySetter;

class TestClassWithIncorrectDependencySetter2
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
