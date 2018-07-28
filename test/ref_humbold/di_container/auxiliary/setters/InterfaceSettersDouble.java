package ref_humbold.di_container.auxiliary.setters;

import ref_humbold.di_container.auxiliary.basics.InterfaceBasics;
import ref_humbold.di_container.auxiliary.basics.InterfaceBasicsStringGetter;

public interface InterfaceSettersDouble
{
    InterfaceBasics getBasicObject();

    InterfaceBasicsStringGetter getStringObject();

    void setObjects(InterfaceBasics basicObject, InterfaceBasicsStringGetter stringObject);
}
