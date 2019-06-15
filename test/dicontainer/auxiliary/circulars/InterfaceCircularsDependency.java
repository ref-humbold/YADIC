package dicontainer.auxiliary.circulars;

import dicontainer.auxiliary.basics.InterfaceBasicsStringGetter;

public interface InterfaceCircularsDependency
{
    InterfaceBasicsStringGetter getNonCircularObject();

    InterfaceCirculars1 getCircularObject();
}
