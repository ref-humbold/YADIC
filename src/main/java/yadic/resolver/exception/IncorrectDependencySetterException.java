package yadic.resolver.exception;

import yadic.YadicException;

public class IncorrectDependencySetterException
        extends YadicException
{
    private static final long serialVersionUID = 3707239480399538423L;

    public IncorrectDependencySetterException(String s)
    {
        super(s);
    }
}
