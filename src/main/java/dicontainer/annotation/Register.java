package dicontainer.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dicontainer.ConstructionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Register
{
    Class<?> value();

    ConstructionPolicy policy() default ConstructionPolicy.CONSTRUCTION;
}
