package ref_humbold.di_container.auxiliary.circulars;

import ref_humbold.di_container.auxiliary.basics.InterfaceBasicsStringGetter;

public interface InterfaceCircularsDependency
{
    InterfaceBasicsStringGetter getNonCircularObject();

    InterfaceCirculars1 getCircularObject();
}
