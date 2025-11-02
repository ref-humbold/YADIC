package com.github.refhumbold.yadic.resolver.exception;

import java.io.Serial;
import com.github.refhumbold.yadic.YadicException;

public class IncorrectDependencySetterException
        extends YadicException
{
    @Serial private static final long serialVersionUID = 3707239480399538423L;

    public IncorrectDependencySetterException(String message)
    {
        super(message);
    }
}
