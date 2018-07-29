package refhumbold.dicontainer.auxiliary.register;

import refhumbold.dicontainer.ConstructionPolicy;
import refhumbold.dicontainer.annotation.Register;

@Register(value = ClassRegisterSingletonDerived.class, policy = ConstructionPolicy.SINGLETON)
public class ClassRegisterSingletonBase
    implements InterfaceRegisterSingleton
{
}
