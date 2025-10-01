package yadic.resolver.exception;

import java.io.Serial;

import yadic.YadicException;

public class SetterInvocationException
        extends YadicException
{
    @Serial private static final long serialVersionUID = 3175636363176361357L;

    public SetterInvocationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
