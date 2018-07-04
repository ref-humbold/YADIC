package ref_humbold.di_container.exception;

public class MissingDependenciesException
    extends DIException
{
    private static final long serialVersionUID = 7265031467533657162L;

    public MissingDependenciesException(String s)
    {
        super(s);
    }

    public MissingDependenciesException(String s, Throwable t)
    {
        super(s, t);
    }
}
