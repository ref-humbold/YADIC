package yadic.models.register;

import yadic.ConstructionPolicy;
import yadic.annotation.YadicRegister;

@YadicRegister(value = ClassRegisterSingletonDerived.class, policy = ConstructionPolicy.SINGLETON)
public class ClassRegisterSingletonBase
        implements InterfaceRegisterSingleton
{
}
