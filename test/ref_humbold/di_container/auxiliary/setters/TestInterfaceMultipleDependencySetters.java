package ref_humbold.di_container.auxiliary.setters;

import ref_humbold.di_container.auxiliary.basics.TestInterfaceBasic;
import ref_humbold.di_container.auxiliary.basics.TestInterfaceWithString;

public interface TestInterfaceMultipleDependencySetters
{
    TestInterfaceBasic getBasicObject();

    void setBasicObject(TestInterfaceBasic basicObject);

    TestInterfaceWithString getStringObject();

    void setStringObject(TestInterfaceWithString stringObject);
}
