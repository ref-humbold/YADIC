package com.github.refhumbold.yadic.registry.exception;

import java.io.Serial;
import com.github.refhumbold.yadic.YadicException;

public class NotDerivedTypeException
        extends YadicException
{
    @Serial private static final long serialVersionUID = -3180961583302361880L;

    public NotDerivedTypeException(String s)
    {
        super(s);
    }
}
