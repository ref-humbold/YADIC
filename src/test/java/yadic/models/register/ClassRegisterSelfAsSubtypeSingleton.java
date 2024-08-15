package yadic.models.register;

import yadic.ConstructionPolicy;
import yadic.annotation.Register;

@Register(value = ClassRegisterSelfAsSubtypeSingleton.class, policy = ConstructionPolicy.SINGLETON)
public class ClassRegisterSelfAsSubtypeSingleton
{
}
