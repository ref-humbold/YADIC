package com.github.refhumbold.yadic.models.annotations.register;

import com.github.refhumbold.yadic.ConstructionPolicy;
import com.github.refhumbold.yadic.annotation.YadicRegister;

@YadicRegister(value = ClassDerivedFromRegisterConstruction.class,
               policy = ConstructionPolicy.CONSTRUCTION)
public class ClassRegisterConstruction
{
}
