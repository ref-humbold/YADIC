package dicontainer.resolver.exception;

import dicontainer.DIException;

public class NoSuitableConstructorException
        extends DIException
{
    private static final long serialVersionUID = 902034423730601801L;

    public NoSuitableConstructorException(String s)
    {
        super(s);
    }
}
