package dicontainer.models.register;

import dicontainer.ConstructionPolicy;
import dicontainer.annotation.SelfRegister;

@SelfRegister(policy = ConstructionPolicy.SINGLETON)
public class ClassRegisterSelfSingleton
{
}
