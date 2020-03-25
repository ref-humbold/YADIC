package dicontainer.commons;

import java.lang.reflect.Modifier;

import dicontainer.annotation.Register;
import dicontainer.annotation.SelfRegister;

public class TypesUtils
{
    public static boolean isAbstractType(Class<?> type)
    {
        return type.isInterface() || Modifier.isAbstract(type.getModifiers());
    }

    public static boolean isAnnotatedType(Class<?> type)
    {
        return type.isAnnotationPresent(Register.class) || type.isAnnotationPresent(
                SelfRegister.class);
    }
}
