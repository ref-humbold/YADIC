package com.github.refhumbold.yadic.resolver.exception;

import java.io.Serial;
import com.github.refhumbold.yadic.YadicException;

public class MultipleAnnotatedConstructorsException
        extends YadicException
{
    @Serial private static final long serialVersionUID = 9149838622398855405L;

    public MultipleAnnotatedConstructorsException(String message)
    {
        super(message);
    }
}
