package dicontainer.exception;

public class NoInstanceCreatedException
        extends DIException
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
