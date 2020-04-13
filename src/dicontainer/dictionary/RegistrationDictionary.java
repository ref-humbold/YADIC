package dicontainer.dictionary;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import dicontainer.ConstructionPolicy;
import dicontainer.annotation.Register;
import dicontainer.annotation.SelfRegister;
import dicontainer.commons.Instance;
import dicontainer.commons.TypesUtils;
import dicontainer.exception.AbstractTypeException;
import dicontainer.exception.MissingDependenciesException;
import dicontainer.exception.NotDerivedTypeException;

public class RegistrationDictionary
{
    private final Map<Class<?>, SubtypeMapping<?>> subtypes = new HashMap<>();
    private final Map<Class<?>, Instance<?>> instances = new HashMap<>();

    public RegistrationDictionary()
    {
    }

    @SuppressWarnings("unchecked")
    public <T> void insertType(Class<T> type, ConstructionPolicy policy)
    {
        validateAnnotation(type);

        if(type.isAnnotationPresent(Register.class))
        {
            Register annotation = type.getAnnotation(Register.class);

            doInsertType(type, (Class<? extends T>)annotation.value(), annotation.policy());
        }
        else if(type.isAnnotationPresent(SelfRegister.class))
        {
            SelfRegister annotation = type.getAnnotation(SelfRegister.class);

            doInsertType(type, type, annotation.policy());
        }
        else
            doInsertType(type, type, policy);
    }

    public <T> void insertType(Class<T> type, Class<? extends T> subtype, ConstructionPolicy policy)
    {
        if(TypesUtils.isAnnotatedType(type))
            throw new ChangingAnnotatedRegistrationException(
                    String.format("Cannot change registration from annotation for type %s",
                                  type.getName()));

        doInsertType(type, subtype, policy);
    }

    public <T> void insertInstance(Class<T> type, T instance)
    {
        Objects.requireNonNull(instance);
        insertType(type, type, ConstructionPolicy.SINGLETON);
        instances.put(type, Instance.make(instance));
    }

    public boolean containsType(Class<?> type)
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

    public boolean containsInstance(Class<?> type)
    {
        return instances.containsKey(type);
    }

    @SuppressWarnings("unchecked")
    public <T> SubtypeMapping<? extends T> getType(Class<T> type)
    {
        if(TypesUtils.isAnnotatedType(type) && !subtypes.containsKey(type))
            insertType(type, ConstructionPolicy.getDefault());

        SubtypeMapping<? extends T> mapping = (SubtypeMapping<? extends T>)subtypes.get(type);

        if(mapping != null)
            return mapping;

        if(TypesUtils.isAbstractReferenceType(type))
            throw new MissingDependenciesException(
                    String.format("Abstract type %s has no registered concrete subclass",
                                  type.getName()));

        return new SubtypeMapping<>(type, ConstructionPolicy.getDefault());
    }

    @SuppressWarnings("unchecked")
    public <T> Instance<T> getInstance(Class<T> type)
    {
        return Instance.cast((Instance<T>)instances.get(type));
    }

    public <T> SubtypeMapping<? extends T> findType(Class<T> type)
    {
        SubtypeMapping<? extends T> mapping = getType(type);
        ConstructionPolicy desiredPolicy = mapping.policy;
        Class<?> supertype = type;

        while(TypesUtils.isAbstractReferenceType(mapping.subtype)
                || containsType(mapping.subtype) && !mapping.subtype.equals(supertype))
        {
            supertype = mapping.subtype;
            mapping = getType(mapping.subtype);

            if(mapping.policy != desiredPolicy)
                throw new MixingPoliciesException(String.format(
                        "Registered classes chain contains two different construction policies: expected %s, was %s",
                        desiredPolicy.toString(), mapping.policy.toString()));
        }

        return mapping;
    }

    private <T> void doInsertType(Class<T> type, Class<? extends T> subtype,
                                  ConstructionPolicy policy)
    {
        subtypes.put(type, new SubtypeMapping<>(subtype, policy));
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
