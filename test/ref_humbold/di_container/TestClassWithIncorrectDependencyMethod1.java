package ref_humbold.di_container;

import ref_humbold.di_container.annotation.DependencyMethod;

class TestClassWithIncorrectDependencyMethod1
{
    public TestClassWithIncorrectDependencyMethod1()
    {
    }

    @DependencyMethod
    public int method(int number)
    {
        return number + 1;
    }
}
