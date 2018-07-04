package ref_humbold.di_container;

import ref_humbold.di_container.annotation.DependencyMethod;

class TestClassWithIncorrectDependencyMethod2
{
    public TestClassWithIncorrectDependencyMethod2()
    {
    }

    @DependencyMethod
    public void method()
    {
        System.out.println("");
    }
}
