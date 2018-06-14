package ref_humbold.di_container;

public interface TestInterfaceMultipleDependencySetters
{
    TestInterfaceBasic getBasicObject();

    void setBasicObject(TestInterfaceBasic basicObject);

    TestInterfaceWithString getStringObject();

    void setStringObject(TestInterfaceWithString stringObject);
}
