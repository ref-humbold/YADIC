package ref_humbold.di_container;

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
