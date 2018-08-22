package refhumbold.dicontainer.annotation;

import java.lang.annotation.*;

import refhumbold.dicontainer.ConstructionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Register
{
    Class<?> value();

    ConstructionPolicy policy() default ConstructionPolicy.CONSTRUCT;
}
