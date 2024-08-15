package yadic.models.setter;

import yadic.models.basic.InterfaceBasic;
import yadic.models.basic.InterfaceBasicStringGetter;

public interface InterfaceSetterMultipleParameters
{
    InterfaceBasic getBasicObject();

    InterfaceBasicStringGetter getStringObject();

    void setObjects(InterfaceBasic basicObject, InterfaceBasicStringGetter stringObject);
}
