package dicontainer.models.register;

import dicontainer.ConstructionPolicy;
import dicontainer.annotation.Register;

@Register(value = ClassRegisterSingletonDerived.class, policy = ConstructionPolicy.SINGLETON)
public class ClassRegisterSingletonBase
        implements InterfaceRegisterSingleton
{
}
