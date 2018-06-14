package ref_humbold.di_container;

public class EmptyContainerProviderException
    extends DIException
{
    private static final long serialVersionUID = 6099774839770839995L;

    public EmptyContainerProviderException(String s)
    {
        super(s);
    }

    public EmptyContainerProviderException(String s, Throwable t)
    {
        super(s, t);
    }
}
