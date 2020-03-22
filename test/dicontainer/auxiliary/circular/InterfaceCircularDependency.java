package dicontainer.auxiliary.circular;

import dicontainer.auxiliary.basic.InterfaceBasicStringGetter;

public interface InterfaceCircularDependency
{
    InterfaceBasicStringGetter getNonCircularObject();

    InterfaceCircularLeft getCircularObject();
}
