package ref_humbold.di_container;

import ref_humbold.di_container.annotation.DependencyConstructor;

class TestClassWithMultipleAnnotatedConstructors
{
    private String text;

    @DependencyConstructor
    public TestClassWithMultipleAnnotatedConstructors()
    {
    }

    @DependencyConstructor
    public TestClassWithMultipleAnnotatedConstructors(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return text;
    }
}
