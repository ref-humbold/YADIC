package dicontainer.auxiliary.setter;

import dicontainer.auxiliary.basic.InterfaceBasic;
import dicontainer.auxiliary.basic.InterfaceBasicStringGetter;

public interface InterfaceSetterMultipleParameters
{
    InterfaceBasic getBasicObject();

    InterfaceBasicStringGetter getStringObject();

    void setObjects(InterfaceBasic basicObject, InterfaceBasicStringGetter stringObject);
}
