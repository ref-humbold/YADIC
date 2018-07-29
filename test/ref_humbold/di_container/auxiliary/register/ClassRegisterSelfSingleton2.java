package ref_humbold.di_container.auxiliary.register;

import ref_humbold.di_container.ConstructionPolicy;
import ref_humbold.di_container.annotation.Register;

@Register(value = ClassRegisterSelfSingleton2.class, policy = ConstructionPolicy.SINGLETON)
public class ClassRegisterSelfSingleton2
{
}
