package com.github.refhumbold.yadic.resolver.exception;

import java.io.Serial;
import com.github.refhumbold.yadic.YadicException;

public class NoSuitableConstructorException
        extends YadicException
{
    @Serial private static final long serialVersionUID = 902034423730601801L;

    public NoSuitableConstructorException(String message)
    {
        super(message);
    }
}
