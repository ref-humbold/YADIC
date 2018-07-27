package ref_humbold.di_container.auxiliary.basics;

import ref_humbold.di_container.auxiliary.diamonds.TestInterfaceDiamond1;

public interface TestInterfaceSimpleDependency
{
    TestInterfaceDiamond1 getFirstObject();

    TestInterfaceWithString getSecondObject();
}
