package refhumbold.dicontainer.auxiliary.setters;

import refhumbold.dicontainer.auxiliary.basics.InterfaceBasics;
import refhumbold.dicontainer.auxiliary.basics.InterfaceBasicsStringGetter;

public interface InterfaceSettersDouble
{
    InterfaceBasics getBasicObject();

    InterfaceBasicsStringGetter getStringObject();

    void setObjects(InterfaceBasics basicObject, InterfaceBasicsStringGetter stringObject);
}
