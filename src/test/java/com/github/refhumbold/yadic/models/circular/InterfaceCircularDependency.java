package com.github.refhumbold.yadic.models.circular;

import com.github.refhumbold.yadic.models.basic.InterfaceBasicStringGetter;

public interface InterfaceCircularDependency
{
    InterfaceBasicStringGetter getNonCircularObject();

    InterfaceCircularLeft getCircularObject();
}
