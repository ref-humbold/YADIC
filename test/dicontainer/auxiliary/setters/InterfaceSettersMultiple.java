package dicontainer.auxiliary.setters;

import dicontainer.auxiliary.basics.InterfaceBasics;
import dicontainer.auxiliary.basics.InterfaceBasicsStringGetter;

public interface InterfaceSettersMultiple
{
    InterfaceBasics getBasicObject();

    void setBasicObject(InterfaceBasics basicObject);

    InterfaceBasicsStringGetter getStringObject();

    void setStringObject(InterfaceBasicsStringGetter stringObject);
}
