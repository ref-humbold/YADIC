package ref_humbold.di_container;

interface TestInterfaceCircularDependency
{
    TestInterfaceWithString getNonCircularObject();

    TestInterfaceCircular1 getCircularObject();
}
