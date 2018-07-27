package ref_humbold.di_container.auxiliary.constructors;

import ref_humbold.di_container.annotation.DependencyConstructor;
import ref_humbold.di_container.auxiliary.basics.TestInterfaceSimpleDependency;
import ref_humbold.di_container.auxiliary.basics.TestInterfaceWithString;
import ref_humbold.di_container.auxiliary.diamonds.TestInterfaceDiamond1;

public class TestClassSimpleDependencyWithAnnotation
    implements TestInterfaceSimpleDependency
{
    private TestInterfaceDiamond1 firstObject;
    private TestInterfaceWithString secondObject;

    @DependencyConstructor
    public TestClassSimpleDependencyWithAnnotation(TestInterfaceDiamond1 firstObject,
                                                   TestInterfaceWithString secondObject)
    {
        this.firstObject = firstObject;
        this.secondObject = secondObject;
    }

    @Override
    public TestInterfaceDiamond1 getFirstObject()
    {
        return firstObject;
    }

    @Override
    public TestInterfaceWithString getSecondObject()
    {
        return secondObject;
    }
}
