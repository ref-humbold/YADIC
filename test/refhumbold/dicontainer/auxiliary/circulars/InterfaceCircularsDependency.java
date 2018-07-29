package refhumbold.dicontainer.auxiliary.circulars;

import refhumbold.dicontainer.auxiliary.basics.InterfaceBasicsStringGetter;

public interface InterfaceCircularsDependency
{
    InterfaceBasicsStringGetter getNonCircularObject();

    InterfaceCirculars1 getCircularObject();
}
