package ref_humbold.di_container.exception;

public class CircularDependenciesException
    extends DIException
{
    private static final long serialVersionUID = 607229069481348756L;

    public CircularDependenciesException(String s)
    {
        super(s);
    }
}
