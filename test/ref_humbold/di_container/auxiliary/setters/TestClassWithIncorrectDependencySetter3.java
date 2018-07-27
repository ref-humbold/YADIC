package ref_humbold.di_container.auxiliary.setters;

import ref_humbold.di_container.annotation.DependencySetter;

public class TestClassWithIncorrectDependencySetter3
{
    private int number;

    public TestClassWithIncorrectDependencySetter3()
    {
    }

    @DependencySetter
    public void method(int number)
    {
        this.number = number;
    }
}
