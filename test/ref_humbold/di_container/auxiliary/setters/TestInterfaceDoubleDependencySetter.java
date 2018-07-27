package ref_humbold.di_container.auxiliary.setters;

import ref_humbold.di_container.auxiliary.basics.TestInterfaceBasic;
import ref_humbold.di_container.auxiliary.basics.TestInterfaceWithString;

public interface TestInterfaceDoubleDependencySetter
{
    TestInterfaceBasic getBasicObject();

    TestInterfaceWithString getStringObject();

    void setObjects(TestInterfaceBasic basicObject, TestInterfaceWithString stringObject);
}
