package ref_humbold.di_container.auxiliary.register;

import ref_humbold.di_container.ConstructionPolicy;
import ref_humbold.di_container.annotation.Register;

@Register(value = ClassRegisterSingletonDerived.class, policy = ConstructionPolicy.SINGLETON)
public class ClassRegisterSingletonBase
    implements InterfaceRegisterSingleton
{
}
