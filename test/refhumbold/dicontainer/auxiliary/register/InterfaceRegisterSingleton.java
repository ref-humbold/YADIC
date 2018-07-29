package refhumbold.dicontainer.auxiliary.register;

import refhumbold.dicontainer.ConstructionPolicy;
import refhumbold.dicontainer.annotation.Register;

@Register(value = ClassRegisterSingletonBase.class, policy = ConstructionPolicy.SINGLETON)
public interface InterfaceRegisterSingleton
{
}
