package dicontainer.resolver.exception;

import dicontainer.DiException;

public class NoInstanceCreatedException
        extends DiException
{
    private static final long serialVersionUID = 810031827582865450L;

    public NoInstanceCreatedException(String s)
    {
        super(s);
    }

    public NoInstanceCreatedException(String s, Throwable t)
    {
        super(s, t);
    }
}
