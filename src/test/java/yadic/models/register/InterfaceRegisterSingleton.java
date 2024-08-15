package yadic.models.register;

import yadic.ConstructionPolicy;
import yadic.annotation.Register;

@Register(value = ClassRegisterSingletonBase.class, policy = ConstructionPolicy.SINGLETON)
public interface InterfaceRegisterSingleton
{
}
