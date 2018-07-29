package ref_humbold.di_container.auxiliary.register;

import ref_humbold.di_container.ConstructionPolicy;
import ref_humbold.di_container.annotation.Register;

@Register(value = ClassRegisterSingletonBase.class, policy = ConstructionPolicy.SINGLETON)
public interface InterfaceRegisterSingleton
{
}
