package dicontainer.models.register;

import dicontainer.ConstructionPolicy;
import dicontainer.annotation.Register;

@Register(value = ClassRegisterSingletonBase.class, policy = ConstructionPolicy.SINGLETON)
public interface InterfaceRegisterSingleton
{
}
