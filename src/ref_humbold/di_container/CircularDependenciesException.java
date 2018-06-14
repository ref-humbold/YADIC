package ref_humbold.di_container;

public class CircularDependenciesException
    extends DIException
{
    private static final long serialVersionUID = 607229069481348756L;

    public CircularDependenciesException(String s)
    {
        super(s);
    }

    public CircularDependenciesException(String s, Throwable t)
    {
        super(s, t);
    }
}
