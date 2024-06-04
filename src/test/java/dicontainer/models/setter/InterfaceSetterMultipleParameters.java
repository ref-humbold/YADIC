package dicontainer.models.setter;

import dicontainer.models.basic.InterfaceBasic;
import dicontainer.models.basic.InterfaceBasicStringGetter;

public interface InterfaceSetterMultipleParameters
{
    InterfaceBasic getBasicObject();

    InterfaceBasicStringGetter getStringObject();

    void setObjects(InterfaceBasic basicObject, InterfaceBasicStringGetter stringObject);
}
