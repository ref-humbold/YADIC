package ref_humbold.di_container.auxiliary.setters;

import ref_humbold.di_container.auxiliary.basics.InterfaceBasics;
import ref_humbold.di_container.auxiliary.basics.InterfaceBasicsStringGetter;

public interface InterfaceSettersMultiple
{
    InterfaceBasics getBasicObject();

    void setBasicObject(InterfaceBasics basicObject);

    InterfaceBasicsStringGetter getStringObject();

    void setStringObject(InterfaceBasicsStringGetter stringObject);
}
