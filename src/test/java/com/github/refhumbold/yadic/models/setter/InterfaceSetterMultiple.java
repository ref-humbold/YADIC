package com.github.refhumbold.yadic.models.setter;

import com.github.refhumbold.yadic.models.basic.InterfaceBasic;
import com.github.refhumbold.yadic.models.basic.InterfaceBasicStringGetter;

public interface InterfaceSetterMultiple
{
    InterfaceBasic getBasicObject();

    void setBasicObject(InterfaceBasic basicObject);

    InterfaceBasicStringGetter getStringObject();

    void setStringObject(InterfaceBasicStringGetter stringObject);
}
