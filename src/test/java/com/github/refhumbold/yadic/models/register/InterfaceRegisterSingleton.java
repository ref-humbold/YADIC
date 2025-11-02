package com.github.refhumbold.yadic.models.register;

import com.github.refhumbold.yadic.ConstructionPolicy;
import com.github.refhumbold.yadic.annotation.YadicRegister;

@YadicRegister(value = ClassRegisterSingletonBase.class, policy = ConstructionPolicy.SINGLETON)
public interface InterfaceRegisterSingleton
{
}
