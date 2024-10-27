package yadic.resolver.exception;

import yadic.YadicException;

public class SetterInvocationException
        extends YadicException
{
    private static final long serialVersionUID = 3175636363176361357L;

    public SetterInvocationException(String s, Throwable t)
    {
        super(s, t);
    }
}
