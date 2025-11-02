package com.github.refhumbold.yadic.registry.exception;

import java.io.Serial;
import com.github.refhumbold.yadic.YadicException;

public class AbstractTypeException
        extends YadicException
{
    @Serial private static final long serialVersionUID = 5573956461991224741L;

    public AbstractTypeException(String message)
    {
        super(message);
    }
}
