package ref_humbold.di_container.auxiliary.circulars;

import ref_humbold.di_container.auxiliary.basics.TestInterfaceWithString;

public interface TestInterfaceCircularDependency
{
    TestInterfaceWithString getNonCircularObject();

    TestInterfaceCircular1 getCircularObject();
}
