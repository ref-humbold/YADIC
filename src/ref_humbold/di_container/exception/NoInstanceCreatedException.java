package ref_humbold.di_container.exception;

public class NoInstanceCreatedException
    extends DIException
{
    private static final long serialVersionUID = 810031827582865450L;

    public NoInstanceCreatedException(String s)
    {
        super(s);
    }
}
