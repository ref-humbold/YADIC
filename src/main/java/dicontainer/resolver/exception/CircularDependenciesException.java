package dicontainer.resolver.exception;

import dicontainer.DIException;

public class CircularDependenciesException
        extends DIException
{
    private static final long serialVersionUID = 607229069481348756L;

    public CircularDependenciesException(String s)
    {
        super(s);
    }
}
