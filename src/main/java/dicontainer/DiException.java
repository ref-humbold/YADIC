package dicontainer;

public class DiException
        extends RuntimeException
{
    private static final long serialVersionUID = -3019200382390630637L;

    public DiException(String s)
    {
        super(s);
    }

    public DiException(String s, Throwable t)
    {
        super(s, t);
    }
}
