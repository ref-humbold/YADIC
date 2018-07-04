package ref_humbold.di_container;

import ref_humbold.di_container.annotation.DependencySetter;

class TestClassWithIncorrectDependencySetter1
{
    public TestClassWithIncorrectDependencySetter1()
    {
    }

    @DependencySetter
    public int method(int number)
    {
        return number + 1;
    }
}
