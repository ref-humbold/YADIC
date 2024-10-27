package yadic.models.register;

import yadic.ConstructionPolicy;
import yadic.annotation.YadicRegister;

@YadicRegister(value = ClassRegisterSelfAsSubtypeSingleton.class,
               policy = ConstructionPolicy.SINGLETON)
public class ClassRegisterSelfAsSubtypeSingleton
{
}
