package yadic.models.circular;

import yadic.models.basic.InterfaceBasicStringGetter;

public interface InterfaceCircularDependency
{
    InterfaceBasicStringGetter getNonCircularObject();

    InterfaceCircularLeft getCircularObject();
}
