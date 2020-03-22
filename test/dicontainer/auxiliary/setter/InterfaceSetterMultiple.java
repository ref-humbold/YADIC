package dicontainer.auxiliary.setter;

import dicontainer.auxiliary.basic.InterfaceBasic;
import dicontainer.auxiliary.basic.InterfaceBasicStringGetter;

public interface InterfaceSetterMultiple
{
    InterfaceBasic getBasicObject();

    void setBasicObject(InterfaceBasic basicObject);

    InterfaceBasicStringGetter getStringObject();

    void setStringObject(InterfaceBasicStringGetter stringObject);
}
