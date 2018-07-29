package refhumbold.dicontainer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import refhumbold.dicontainer.ConstructionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SelfRegister
{
    ConstructionPolicy policy() default ConstructionPolicy.CONSTRUCT;
}
