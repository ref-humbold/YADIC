package dicontainer.auxiliary.setters;

import dicontainer.auxiliary.basics.InterfaceBasics;
import dicontainer.auxiliary.basics.InterfaceBasicsStringGetter;

public interface InterfaceSettersDouble
{
    InterfaceBasics getBasicObject();

    InterfaceBasicsStringGetter getStringObject();

    void setObjects(InterfaceBasics basicObject, InterfaceBasicsStringGetter stringObject);
}
