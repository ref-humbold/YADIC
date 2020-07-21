package dicontainer.dictionary;

import java.util.HashMap;
import java.util.Map;

import dicontainer.ConstructionPolicy;
import dicontainer.annotation.Register;
import dicontainer.annotation.SelfRegister;
import dicontainer.commons.Instance;
import dicontainer.commons.TypesUtils;
import dicontainer.exception.AbstractTypeException;
import dicontainer.exception.MissingDependenciesException;
import dicontainer.exception.NotDerivedTypeException;

class TypesDictionary
{
    private final Map<Class<?>, SubtypeMapping<?>> subtypes = new HashMap<>();
    private final Map<Class<?>, Instance<?>> singletons = new HashMap<>();

    TypesDictionary()
    {
    }

    @SuppressWarnings("unchecked")
    <T> void insert(Class<T> type, ConstructionPolicy policy)
    {
        validateAnnotation(type);

        if(type.isAnnotationPresent(Register.class))
        {
            Register annotation = type.getAnnotation(Register.class);

            doInsert(type, new SubtypeMapping<>((Class<? extends T>)annotation.value(),
                                                annotation.policy()));
        }
        else if(type.isAnnotationPresent(SelfRegister.class))
        {
            SelfRegister annotation = type.getAnnotation(SelfRegister.class);

            doInsert(type, new SubtypeMapping<>(type, annotation.policy()));
        }
        else
        {
            if(TypesUtils.isAbstractReferenceType(type))
                throw new AbstractTypeException(
                        String.format("Cannot register abstract type %s", type.getName()));

            doInsert(type, new SubtypeMapping<>(type, policy));
        }
    }

    <T> void insert(Class<T> type, Class<? extends T> subtype, ConstructionPolicy policy)
    {
        if(TypesUtils.isAnnotatedType(type))
            throw new AnnotatedTypeRegistrationException(
                    String.format("Cannot register type for annotated type %s", type.getName()));

        doInsert(type, new SubtypeMapping<>(subtype, policy));
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

        return TypesUtils.isAnnotatedType(type) || subtypes.containsKey(type);
    }

    @SuppressWarnings("unchecked")
    <T> SubtypeMapping<? extends T> get(Class<T> type)
    {
        if(TypesUtils.isAnnotatedType(type) && !subtypes.containsKey(type))
            insert(type, ConstructionPolicy.getDefault());

        SubtypeMapping<? extends T> mapping = (SubtypeMapping<? extends T>)subtypes.get(type);

        if(mapping != null)
            return mapping;

        if(TypesUtils.isAbstractReferenceType(type))
            throw new MissingDependenciesException(
                    String.format("Abstract type %s has no registered concrete subclass",
                                  type.getName()));

        return new SubtypeMapping<>(type, ConstructionPolicy.getDefault());
    }

    <T> SubtypeMapping<? extends T> find(Class<T> type)
    {
        SubtypeMapping<? extends T> mapping = get(type);
        ConstructionPolicy desiredPolicy = mapping.policy;
        Class<?> supertype = type;

        while(TypesUtils.isAbstractReferenceType(mapping.subtype)
                || contains(mapping.subtype) && !mapping.subtype.equals(supertype))
        {
            supertype = mapping.subtype;
            mapping = get(mapping.subtype);

            if(mapping.policy != desiredPolicy)
                throw new MixingPoliciesException(String.format(
                        "Registered classes chain contains two different construction policies: expected %s, was %s",
                        desiredPolicy.toString(), mapping.policy.toString()));
        }

        return mapping;
    }

    <T> void insertSingleton(Class<T> type, T instance)
    {
        SubtypeMapping<?> mapping = subtypes.get(type);

        if(mapping == null || mapping.policy != ConstructionPolicy.SINGLETON)
            return;

        singletons.putIfAbsent(type, Instance.make(instance));
    }

    @SuppressWarnings("unchecked")
    <T> Instance<T> getSingleton(Class<T> type)
    {
        return (Instance<T>)Instance.cast(singletons.get(type));
    }

    private <T> void doInsert(Class<T> type, SubtypeMapping<? extends T> mapping)
    {
        subtypes.put(type, mapping);
        singletons.remove(type);
    }

    private void validateAnnotation(Class<?> type)
    {
        if(type.isAnnotationPresent(Register.class))
        {
            Register annotation = type.getAnnotation(Register.class);
            Class<?> subtype = annotation.value();

            if(!type.isAssignableFrom(subtype))
                throw new NotDerivedTypeException(
                        String.format("Type %s registered via @Register is not derived type of %s",
                                      subtype.getName(), type.getName()));

            if(TypesUtils.isAbstractReferenceType(subtype))
                throw new AbstractTypeException(
                        String.format("Type %s registered via @Register in %s is abstract",
                                      subtype.getName(), type.getName()));
        }
        else if(type.isAnnotationPresent(SelfRegister.class))
        {
            if(TypesUtils.isAbstractReferenceType(type))
                throw new AbstractTypeException(
                        String.format("Abstract type %s cannot be annotated with @SelfRegister",
                                      type.getName()));
        }
    }
}
