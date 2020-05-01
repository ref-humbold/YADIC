package dicontainer.dictionary;

import dicontainer.ConstructionPolicy;
import dicontainer.commons.Instance;
import dicontainer.commons.TypesUtils;

public class DIDictionary
{
    private final TypesDictionary typesDictionary = new TypesDictionary();
    private final InstancesDictionary instancesDictionary = new InstancesDictionary();

    public <T> void addType(Class<T> type)
    {
        validateRegisterType(type);
        typesDictionary.insert(type, ConstructionPolicy.getDefault());
    }

    public <T> void addType(Class<T> type, ConstructionPolicy policy)
    {
        validateRegisterType(type);
        typesDictionary.insert(type, policy);
    }

    public <T> void addType(Class<T> type, Class<? extends T> subtype)
    {
        validateRegisterType(type);
        typesDictionary.insert(type, subtype, ConstructionPolicy.getDefault());
    }

    public <T> void addType(Class<T> type, Class<? extends T> subtype, ConstructionPolicy policy)
    {
        validateRegisterType(type);
        typesDictionary.insert(type, subtype, policy);
    }

    public <T> void addInstance(Class<T> type, T instance)
    {
        validateRegisterInstance(type);
        instancesDictionary.insert(type, instance);
    }

    public <T> SubtypeMapping<? extends T> findType(Class<T> type)
    {
        if(instancesDictionary.contains(type))
            return new SubtypeMapping<>(type, ConstructionPolicy.SINGLETON);

        return typesDictionary.find(type);
    }

    public <T> Instance<T> findInstance(Class<T> type)
    {
        Instance<T> instance = instancesDictionary.get(type);

        return instance.exists() ? instance : typesDictionary.getSingleton(type);
    }

    public boolean contains(Class<?> type)
    {
        return typesDictionary.contains(type) || instancesDictionary.contains(type);
    }

    public <T> void addSingleton(Class<T> type, T instance)
    {
        typesDictionary.insertSingleton(type, instance);
    }

    private <T> void validateRegisterType(Class<T> type)
    {
        if(type.isPrimitive())
            throw new RegistrationException("Cannot register a primitive type");

        if(instancesDictionary.contains(type))
            throw new RegistrationException(
                    String.format("Type %s was registered with an instance", type.getSimpleName()));
    }

    private <T> void validateRegisterInstance(Class<T> type)
    {
        if(TypesUtils.isAnnotatedType(type))
            throw new RegistrationException(
                    String.format("Cannot register instance for annotated type %s",
                                  type.getSimpleName()));

        if(typesDictionary.contains(type))
            throw new RegistrationException(
                    String.format("Type %s was registered with another type",
                                  type.getSimpleName()));
    }
}
