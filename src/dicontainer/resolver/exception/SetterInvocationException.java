package dicontainer.resolver.exception;

import dicontainer.DIException;

public class SetterInvocationException
        extends DIException
{
    private static final long serialVersionUID = 3175636363176361357L;

    public SetterInvocationException(String s, Throwable t)
    {
        super(s, t);
    }
}
