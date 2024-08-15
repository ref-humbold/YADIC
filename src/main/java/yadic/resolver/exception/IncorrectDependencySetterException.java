package yadic.resolver.exception;

import yadic.DiException;

public class IncorrectDependencySetterException
        extends DiException
{
    private static final long serialVersionUID = 3707239480399538423L;

    public IncorrectDependencySetterException(String s)
    {
        super(s);
    }
}
