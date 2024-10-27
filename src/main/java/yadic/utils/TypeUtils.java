package yadic.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;

import yadic.annotation.YadicRegister;
import yadic.annotation.YadicRegisterSelf;

public final class TypeUtils
{
    public static boolean isAbstractReferenceType(Class<?> type)
    {
        return !type.isPrimitive() && (type.isInterface() || Modifier.isAbstract(
                type.getModifiers()));
    }

    public static boolean isAnnotatedType(Class<?> type)
    {
        return type.isAnnotationPresent(YadicRegister.class) || type.isAnnotationPresent(
                YadicRegisterSelf.class);
    }

    public static <A extends Annotation> String getAnnotationName(Class<A> annotationType)
    {
        return String.format("@%s", annotationType.getSimpleName());
    }
}
