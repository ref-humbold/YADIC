package dicontainer;

public class DIException
        extends RuntimeException
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
