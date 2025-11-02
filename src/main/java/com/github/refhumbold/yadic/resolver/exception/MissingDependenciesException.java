package com.github.refhumbold.yadic.resolver.exception;

import java.io.Serial;
import com.github.refhumbold.yadic.YadicException;

public class MissingDependenciesException
        extends YadicException
{
    @Serial private static final long serialVersionUID = 7265031467533657162L;

    public MissingDependenciesException(String message)
    {
        super(message);
    }
}
