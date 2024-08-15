package yadic.models.setter;

import yadic.models.basic.InterfaceBasic;
import yadic.models.basic.InterfaceBasicStringGetter;

public interface InterfaceSetterMultiple
{
    InterfaceBasic getBasicObject();

    void setBasicObject(InterfaceBasic basicObject);

    InterfaceBasicStringGetter getStringObject();

    void setStringObject(InterfaceBasicStringGetter stringObject);
}
