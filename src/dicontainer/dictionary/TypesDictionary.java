package dicontainer.dictionary;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import dicontainer.ConstructionPolicy;
import dicontainer.annotation.Register;
import dicontainer.annotation.SelfRegister;
import dicontainer.exception.AbstractTypeException;
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
        if(type.isAnnotationPresent(Register.class))
        {
            Register annotation = type.getAnnotation(Register.class);
            Class<?> subtype = annotation.value();

            if(!type.isAssignableFrom(subtype))
                throw new NotDerivedTypeException(
                        String.format("Type %s registered via @Register is not derived type of %s",
                                      subtype.getName(), type.getName()));

            if(isAbstractType(type) && Objects.equals(type, subtype))
                throw new AbstractTypeException(
                        String.format("Type %s registered via @Register in %s is abstract",
                                      subtype.getName(), type.getName()));

            insert(type, (Class<? extends T>)subtype, annotation.policy(), true);
        }
        else if(type.isAnnotationPresent(SelfRegister.class))
        {
            if(isAbstractType(type))
                throw new AbstractTypeException(
                        String.format("Abstract type %s cannot be annotated with @SelfRegister",
                                      type.getName()));

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
        if(isAnnotatedType(type))
            throw new AlreadyAnnotatedException(
                    String.format("Cannot changed registration from annotation for type %s", type));

        insert(type, subtype, policy, false);
    }

    @SuppressWarnings("unchecked")
    public <T> SubtypeMapping<? extends T> get(Class<T> type)
    {
        return (SubtypeMapping<? extends T>)subtypes.get(type);
    }

    private <T> void insert(Class<T> type, Class<? extends T> subtype, ConstructionPolicy policy,
                            boolean isFromAnnotation)
    {
        subtypes.put(type, new SubtypeMapping<>(subtype, isFromAnnotation, policy));
    }

    private boolean isAbstractType(Class<?> type)
    {
        return type.isInterface() || Modifier.isAbstract(type.getModifiers());
    }

    private boolean isAnnotatedType(Class<?> type)
    {
        return type.isAnnotationPresent(Register.class) || type.isAnnotationPresent(
                SelfRegister.class);
    }
}
