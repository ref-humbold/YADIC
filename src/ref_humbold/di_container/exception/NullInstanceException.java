package ref_humbold.di_container.exception;

public class NullInstanceException
    extends RuntimeException
{
    private static final long serialVersionUID = -2453276089439956670L;

    public NullInstanceException(String s)
    {
        super(s);
    }

    public NullInstanceException(String s, Throwable t)
    {
        super(s, t);
    }
}
