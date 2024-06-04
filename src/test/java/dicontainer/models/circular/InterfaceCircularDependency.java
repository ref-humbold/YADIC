package dicontainer.models.circular;

import dicontainer.models.basic.InterfaceBasicStringGetter;

public interface InterfaceCircularDependency
{
    InterfaceBasicStringGetter getNonCircularObject();

    InterfaceCircularLeft getCircularObject();
}
