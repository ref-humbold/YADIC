package dicontainer.models.setter;

import dicontainer.models.basic.InterfaceBasic;
import dicontainer.models.basic.InterfaceBasicStringGetter;

public interface InterfaceSetterMultiple
{
    InterfaceBasic getBasicObject();

    void setBasicObject(InterfaceBasic basicObject);

    InterfaceBasicStringGetter getStringObject();

    void setStringObject(InterfaceBasicStringGetter stringObject);
}
