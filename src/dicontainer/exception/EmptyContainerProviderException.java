package dicontainer.exception;

public class EmptyContainerProviderException
        extends RuntimeException
{
    private static final long serialVersionUID = 6099774839770839995L;

    public EmptyContainerProviderException(String s)
    {
        super(s);
    }
}
