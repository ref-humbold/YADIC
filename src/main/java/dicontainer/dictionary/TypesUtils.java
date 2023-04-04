package dicontainer.dictionary;

import java.lang.reflect.Modifier;

import dicontainer.annotation.Register;
import dicontainer.annotation.SelfRegister;
import dicontainer.dictionary.valuetypes.NullInstanceException;

final class TypesUtils
{
    static boolean isAbstractReferenceType(Class<?> type)
    {
        if(type.isPrimitive())
            return false;

        return type.isInterface() || Modifier.isAbstract(type.getModifiers());
    }

    static boolean isAnnotatedType(Class<?> type)
    {
        return type.isAnnotationPresent(Register.class) || type.isAnnotationPresent(
                SelfRegister.class);
    }

    static <T> void requireNonNull(T instance)
    {
        if(instance == null)
            throw new NullInstanceException("Instance is null");
    }
}
