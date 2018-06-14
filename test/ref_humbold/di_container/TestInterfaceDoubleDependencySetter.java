package ref_humbold.di_container;

public interface TestInterfaceDoubleDependencySetter
{
    TestInterfaceBasic getBasicObject();

    TestInterfaceWithString getStringObject();

    void setObjects(TestInterfaceBasic basicObject, TestInterfaceWithString stringObject);
}
