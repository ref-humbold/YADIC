package yadic.models.register;

import yadic.ConstructionPolicy;
import yadic.annotation.Register;

@Register(value = ClassRegisterSingletonDerived.class, policy = ConstructionPolicy.SINGLETON)
public class ClassRegisterSingletonBase
        implements InterfaceRegisterSingleton
{
}
