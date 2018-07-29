package refhumbold.dicontainer.auxiliary.register;

import refhumbold.dicontainer.ConstructionPolicy;
import refhumbold.dicontainer.annotation.Register;

@Register(value = ClassRegisterSelfSingleton2.class, policy = ConstructionPolicy.SINGLETON)
public class ClassRegisterSelfSingleton2
{
}
