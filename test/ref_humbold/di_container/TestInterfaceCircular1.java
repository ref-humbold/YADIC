package ref_humbold.di_container;

interface TestInterfaceCircular1
{
    TestInterfaceCircular2 getObject();
}
