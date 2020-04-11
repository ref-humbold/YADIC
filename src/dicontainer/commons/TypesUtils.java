package dicontainer.commons;

import java.lang.reflect.Modifier;

import dicontainer.annotation.Register;
import dicontainer.annotation.SelfRegister;

public final class TypesUtils
{
    public static boolean isAbstractReferenceType(Class<?> type)
    {
        if(type.isPrimitive())
            return false;

        return type.isInterface() || Modifier.isAbstract(type.getModifiers());
    }

    public static boolean isAnnotatedType(Class<?> type)
    {
        return type.isAnnotationPresent(Register.class) || type.isAnnotationPresent(
                SelfRegister.class);
    }
}
