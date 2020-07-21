package dicontainer.resolver.exception;

import dicontainer.DIException;

public class IncorrectDependencySetterException
        extends DIException
{
    private static final long serialVersionUID = 3707239480399538423L;

    public IncorrectDependencySetterException(String s)
    {
        super(s);
    }
}
