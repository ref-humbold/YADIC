package com.github.refhumbold.yadic.models.setter;

import com.github.refhumbold.yadic.models.basic.InterfaceBasic;
import com.github.refhumbold.yadic.models.basic.InterfaceBasicStringGetter;

public interface InterfaceSetterMultipleParameters
{
    InterfaceBasic getBasicObject();

    InterfaceBasicStringGetter getStringObject();

    void setObjects(InterfaceBasic basicObject, InterfaceBasicStringGetter stringObject);
}
