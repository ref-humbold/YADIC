package com.github.refhumbold.yadic.models.annotations.register;

import com.github.refhumbold.yadic.ConstructionPolicy;
import com.github.refhumbold.yadic.annotation.YadicRegister;

@YadicRegister(value = ClassDerivedFromRegisterSingleton.class,
               policy = ConstructionPolicy.SINGLETON)
public class ClassRegisterSingleton
{
}
