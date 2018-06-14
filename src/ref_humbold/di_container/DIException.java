package ref_humbold.di_container;

public class DIException
    extends Exception
{
    private static final long serialVersionUID = -3019200382390630637L;

    public DIException(String s)
    {
        super(s);
    }

    public DIException(String s, Throwable t)
    {
        super(s, t);
    }
}
