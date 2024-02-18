package dicontainer.resolver.exception;

import dicontainer.DiException;

public class MissingDependenciesException
        extends DiException
{
    private static final long serialVersionUID = 7265031467533657162L;

    public MissingDependenciesException(String s)
    {
        super(s);
    }
}
