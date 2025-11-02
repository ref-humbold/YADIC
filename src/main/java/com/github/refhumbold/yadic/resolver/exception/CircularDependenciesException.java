package com.github.refhumbold.yadic.resolver.exception;

import java.io.Serial;
import com.github.refhumbold.yadic.YadicException;

public class CircularDependenciesException
        extends YadicException
{
    @Serial private static final long serialVersionUID = 607229069481348756L;

    public CircularDependenciesException(String message)
    {
        super(message);
    }
}
