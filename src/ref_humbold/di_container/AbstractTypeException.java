package ref_humbold.di_container;

public class AbstractTypeException
    extends DIException
{
    private static final long serialVersionUID = 5573956461991224741L;

    public AbstractTypeException(String s)
    {
        super(s);
    }

    public AbstractTypeException(String s, Throwable t)
    {
        super(s, t);
    }
}
