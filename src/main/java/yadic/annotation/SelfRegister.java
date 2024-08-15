package yadic.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import yadic.ConstructionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SelfRegister
{
    ConstructionPolicy policy() default ConstructionPolicy.CONSTRUCTION;
}
