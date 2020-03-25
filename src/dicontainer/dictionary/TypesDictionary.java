package dicontainer.dictionary;

import java.util.HashMap;
import java.util.Map;

import dicontainer.ConstructionPolicy;
import dicontainer.TypesUtils;
import dicontainer.annotation.Register;
import dicontainer.annotation.SelfRegister;
import dicontainer.exception.AbstractTypeException;
import dicontainer.exception.MissingDependenciesException;
import dicontainer.exception.NotDerivedTypeException;

public class TypesDictionary
{
    private final Map<Class<?>, SubtypeMapping<?>> subtypes = new HashMap<>();

    public TypesDictionary()
    {
    }

    @SuppressWarnings("unchecked")
    public <T> void insert(Class<T> type)
    {
        validateAnnotation(type);

        if(type.isAnnotationPresent(Register.class))
        {
            Register annotation = type.getAnnotation(Register.class);

            insert(type, (Class<? extends T>)annotation.value(), annotation.policy(), true);
        }
        else if(type.isAnnotationPresent(SelfRegister.class))
        {
            SelfRegister annotation = type.getAnnotation(SelfRegister.class);

            insert(type, type, annotation.policy(), true);
        }
        else
            insert(type, type, ConstructionPolicy.getDefault(), false);
    }

    public <T> void insert(Class<T> type, Class<? extends T> subtype)
    {
        insert(type, subtype, ConstructionPolicy.getDefault());
    }

    public <T> void insert(Class<T> type, Class<? extends T> subtype, ConstructionPolicy policy)
    {
        if(TypesUtils.isAnnotatedType(type))
            throw new ChangingAnnotatedRegistrationException(
                    String.format("Cannot change registration from annotation for type %s", type));

        insert(type, subtype, policy, false);
    }

    public boolean contains(Class<?> type)
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
    public <T> SubtypeMapping<? extends T> get(Class<T> type)
    {
        if(TypesUtils.isAnnotatedType(type) && !subtypes.containsKey(type))
            insert(type);

        SubtypeMapping<? extends T> mapping = (SubtypeMapping<? extends T>)subtypes.get(type);

        if(mapping != null)
            return mapping;

        if(TypesUtils.isAbstractType(type))
            throw new MissingDependenciesException(
                    String.format("Abstract type %s has no registered concrete subclass",
                                  type.getName()));

        return new SubtypeMapping<>(type, false, ConstructionPolicy.getDefault());
    }

    public <T> SubtypeMapping<? extends T> find(Class<T> type)
    {
        SubtypeMapping<? extends T> mapping = get(type);
        ConstructionPolicy desiredPolicy = mapping.policy;

        while(TypesUtils.isAbstractType(mapping.subtype) || contains(mapping.subtype))
        {
            mapping = get(mapping.subtype);

            if(mapping.policy != desiredPolicy)
                throw new MixingPoliciesException("");
        }

        return mapping;
    }

    private <T> void insert(Class<T> type, Class<? extends T> subtype, ConstructionPolicy policy,
                            boolean isFromAnnotation)
    {
        subtypes.put(type, new SubtypeMapping<>(subtype, isFromAnnotation, policy));
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

            if(TypesUtils.isAbstractType(subtype))
                throw new AbstractTypeException(
                        String.format("Type %s registered via @Register in %s is abstract",
                                      subtype.getName(), type.getName()));
        }
        else if(type.isAnnotationPresent(SelfRegister.class))
        {
            if(TypesUtils.isAbstractType(type))
                throw new AbstractTypeException(
                        String.format("Abstract type %s cannot be annotated with @SelfRegister",
                                      type.getName()));
        }
    }
}
