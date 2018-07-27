package ref_humbold.di_container.auxiliary.setters;

import ref_humbold.di_container.auxiliary.basics.TestInterfaceBasic;

public interface TestInterfaceDependencySetter
{
    TestInterfaceBasic getBasicObject();

    void setBasicObject(TestInterfaceBasic basicObject);
}
