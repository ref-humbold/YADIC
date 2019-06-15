package dicontainer.auxiliary.register;

import dicontainer.ConstructionPolicy;
import dicontainer.annotation.Register;

@Register(value = ClassRegisterSelfSingleton2.class, policy = ConstructionPolicy.SINGLETON)
public class ClassRegisterSelfSingleton2
{
}
