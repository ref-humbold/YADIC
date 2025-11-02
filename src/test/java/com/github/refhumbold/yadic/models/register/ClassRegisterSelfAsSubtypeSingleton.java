package com.github.refhumbold.yadic.models.register;

import com.github.refhumbold.yadic.ConstructionPolicy;
import com.github.refhumbold.yadic.annotation.YadicRegister;

@YadicRegister(value = ClassRegisterSelfAsSubtypeSingleton.class,
               policy = ConstructionPolicy.SINGLETON)
public class ClassRegisterSelfAsSubtypeSingleton
{
}
