package com.github.refhumbold.yadic.resolver.exception;

import java.io.Serial;
import com.github.refhumbold.yadic.YadicException;

public class NoInstanceCreatedException
        extends YadicException
{
    @Serial private static final long serialVersionUID = 810031827582865450L;

    public NoInstanceCreatedException(String message)
    {
        super(message);
    }

    public NoInstanceCreatedException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
