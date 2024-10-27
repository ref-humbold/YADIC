package yadic.models.register;

import yadic.ConstructionPolicy;
import yadic.annotation.YadicRegister;

@YadicRegister(value = ClassRegisterSingletonBase.class, policy = ConstructionPolicy.SINGLETON)
public interface InterfaceRegisterSingleton
{
}
