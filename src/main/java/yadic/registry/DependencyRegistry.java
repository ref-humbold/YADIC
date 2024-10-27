package yadic.registry;

import yadic.ConstructionPolicy;
import yadic.registry.exception.RegistrationException;
import yadic.registry.valuetypes.Instance;
import yadic.registry.valuetypes.TypeConstruction;
import yadic.utils.TypeUtils;

public class DependencyRegistry
{
    private final TypesDictionary typesDictionary = new TypesDictionary();
    private final InstancesDictionary instancesDictionary = new InstancesDictionary();

    public <T> void addType(Class<T> type, ConstructionPolicy policy)
    {
        validateRegisteredType(type);
        typesDictionary.insert(type, policy);
    }

    public <T> void addType(Class<T> type, Class<? extends T> subtype, ConstructionPolicy policy)
    {
        validateRegisteredType(type);
        typesDictionary.insert(type, subtype, policy);
    }

    public <T> void addInstance(Class<T> type, T instance)
    {
        validateRegisteredInstance(type);
        instancesDictionary.insert(type, instance);
    }

    public <T> TypeConstruction<? extends T> findType(Class<T> type)
    {
        return instancesDictionary.contains(type)
               ? new TypeConstruction<>(type, ConstructionPolicy.SINGLETON)
               : typesDictionary.find(type);
    }

    public <T> Instance<T> findInstance(Class<T> type)
    {
        return instancesDictionary.get(type).or(() -> typesDictionary.getSingleton(type));
    }

    public boolean contains(Class<?> type)
    {
        return typesDictionary.contains(type) || instancesDictionary.contains(type);
    }

    public <T> void addSingleton(Class<T> type, T instance)
    {
        typesDictionary.insertSingleton(type, instance);
    }

    private <T> void validateRegisteredType(Class<T> type)
    {
        if(type.isPrimitive())
            throw new RegistrationException("Cannot register a primitive type");

        if(instancesDictionary.contains(type))
            throw new RegistrationException(
                    String.format("Type %s was registered with an instance", type.getSimpleName()));
    }

    private <T> void validateRegisteredInstance(Class<T> type)
    {
        if(TypeUtils.isAnnotatedType(type))
            throw new RegistrationException(
                    String.format("Cannot register instance for annotated type %s",
                                  type.getSimpleName()));

        if(typesDictionary.contains(type))
            throw new RegistrationException(
                    String.format("Type %s was registered with another type",
                                  type.getSimpleName()));
    }
}
