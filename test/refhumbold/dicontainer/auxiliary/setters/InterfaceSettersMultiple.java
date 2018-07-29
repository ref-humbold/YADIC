package refhumbold.dicontainer.auxiliary.setters;

import refhumbold.dicontainer.auxiliary.basics.InterfaceBasics;
import refhumbold.dicontainer.auxiliary.basics.InterfaceBasicsStringGetter;

public interface InterfaceSettersMultiple
{
    InterfaceBasics getBasicObject();

    void setBasicObject(InterfaceBasics basicObject);

    InterfaceBasicsStringGetter getStringObject();

    void setStringObject(InterfaceBasicsStringGetter stringObject);
}
