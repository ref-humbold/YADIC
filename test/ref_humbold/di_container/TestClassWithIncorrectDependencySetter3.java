package ref_humbold.di_container;

import ref_humbold.di_container.annotation.DependencySetter;

class TestClassWithIncorrectDependencySetter3
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
