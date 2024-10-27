package yadic.registry;

import java.util.HashMap;
import java.util.Map;

import yadic.ConstructionPolicy;
import yadic.annotation.YadicRegister;
import yadic.annotation.YadicRegisterSelf;
import yadic.registry.exception.AbstractTypeException;
import yadic.registry.exception.AnnotatedTypeRegistrationException;
import yadic.registry.exception.MixingPoliciesException;
import yadic.registry.exception.NotDerivedTypeException;
import yadic.registry.valuetypes.Instance;
import yadic.registry.valuetypes.TypeConstruction;
import yadic.resolver.exception.MissingDependenciesException;
import yadic.utils.TypeUtils;

class TypesDictionary
{
    private final Map<Class<?>, TypeConstruction<?>> typesMap = new HashMap<>();
    private final Map<Class<?>, Instance<?>> singletonsMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    <T> void insert(Class<T> type, ConstructionPolicy policy)
    {
        validateAnnotation(type);

        if(type.isAnnotationPresent(YadicRegister.class))
        {
            YadicRegister annotation = type.getAnnotation(YadicRegister.class);

            doInsert(type, new TypeConstruction<>((Class<? extends T>)annotation.value(),
                                                  annotation.policy()));
        }
        else if(type.isAnnotationPresent(YadicRegisterSelf.class))
        {
            YadicRegisterSelf annotation = type.getAnnotation(YadicRegisterSelf.class);

            doInsert(type, new TypeConstruction<>(type, annotation.policy()));
        }
        else
        {
            if(TypeUtils.isAbstractReferenceType(type))
                throw new AbstractTypeException(
                        String.format("Cannot register abstract type %s", type.getTypeName()));

            doInsert(type, new TypeConstruction<>(type, policy));
        }
    }

    <T> void insert(Class<T> type, Class<? extends T> subtype, ConstructionPolicy policy)
    {
        if(TypeUtils.isAnnotatedType(type))
            throw new AnnotatedTypeRegistrationException(
                    String.format("Cannot register type for annotated type %s",
                                  type.getTypeName()));

        doInsert(type, new TypeConstruction<>(subtype, policy));
    }

    boolean contains(Class<?> type)
    {
        try
        {
            validateAnnotation(type);
        }
        catch(NotDerivedTypeException | AbstractTypeException e)
        {
            return false;
        }

        return TypeUtils.isAnnotatedType(type) || typesMap.containsKey(type);
    }

    <T> TypeConstruction<? extends T> find(Class<T> type)
    {
        TypeConstruction<? extends T> mapping = get(type);
        ConstructionPolicy desiredPolicy = mapping.policy();
        Class<?> supertype = type;

        while(TypeUtils.isAbstractReferenceType(mapping.type())
                || contains(mapping.type()) && !mapping.type().equals(supertype))
        {
            supertype = mapping.type();
            mapping = get(mapping.type());

            if(mapping.policy() != desiredPolicy)
                throw new MixingPoliciesException(String.format(
                        "Registered classes chain contains two different construction policies: expected %s, was %s",
                        desiredPolicy.toString(), mapping.policy().toString()));
        }

        return mapping;
    }

    <T> void insertSingleton(Class<T> type, T instance)
    {
        TypeConstruction<?> mapping = typesMap.get(type);

        if(mapping == null || mapping.policy() != ConstructionPolicy.SINGLETON)
            return;

        singletonsMap.putIfAbsent(type, Instance.of(instance));
    }

    <T> Instance<T> getSingleton(Class<T> type)
    {
        return Instance.cast(singletonsMap.get(type));
    }

    @SuppressWarnings("unchecked")
    private <T> TypeConstruction<? extends T> get(Class<T> type)
    {
        if(TypeUtils.isAnnotatedType(type) && !typesMap.containsKey(type))
            insert(type, null);

        TypeConstruction<? extends T> mapping = (TypeConstruction<? extends T>)typesMap.get(type);

        if(mapping != null)
            return mapping;

        if(TypeUtils.isAbstractReferenceType(type))
            throw new MissingDependenciesException(
                    String.format("Abstract type %s has no registered concrete subclass",
                                  type.getTypeName()));

        return new TypeConstruction<>(type, ConstructionPolicy.CONSTRUCTION);
    }

    private <T> void doInsert(Class<T> type, TypeConstruction<? extends T> mapping)
    {
        typesMap.put(type, mapping);
        singletonsMap.remove(type);
    }

    private void validateAnnotation(Class<?> type)
    {
        if(type.isAnnotationPresent(YadicRegister.class))
        {
            YadicRegister annotation = type.getAnnotation(YadicRegister.class);
            Class<?> subtype = annotation.value();

            if(!type.isAssignableFrom(subtype))
                throw new NotDerivedTypeException(
                        String.format("Type %s registered via %s is not derived type of %s",
                                      subtype.getTypeName(),
                                      TypeUtils.getAnnotationName(YadicRegister.class),
                                      type.getTypeName()));

            if(TypeUtils.isAbstractReferenceType(subtype))
                throw new AbstractTypeException(
                        String.format("Type %s registered via %s in %s is abstract",
                                      subtype.getTypeName(),
                                      TypeUtils.getAnnotationName(YadicRegister.class),
                                      type.getTypeName()));
        }
        else if(type.isAnnotationPresent(YadicRegisterSelf.class))
        {
            if(TypeUtils.isAbstractReferenceType(type))
                throw new AbstractTypeException(
                        String.format("Abstract type %s cannot be annotated with %s",
                                      type.getTypeName(),
                                      TypeUtils.getAnnotationName(YadicRegisterSelf.class)));
        }
    }
}
