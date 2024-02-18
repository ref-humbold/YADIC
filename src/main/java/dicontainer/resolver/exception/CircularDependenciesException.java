package dicontainer.resolver.exception;

import dicontainer.DiException;

public class CircularDependenciesException
        extends DiException
{
    private static final long serialVersionUID = 607229069481348756L;

    public CircularDependenciesException(String s)
    {
        super(s);
    }
}
