package ref_humbold.di_container.auxiliary.setters;

import ref_humbold.di_container.annotation.DependencySetter;

public class TestClassWithIncorrectDependencySetter1
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
