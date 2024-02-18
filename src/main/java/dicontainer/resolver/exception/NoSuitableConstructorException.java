package dicontainer.resolver.exception;

import dicontainer.DiException;

public class NoSuitableConstructorException
        extends DiException
{
    private static final long serialVersionUID = 902034423730601801L;

    public NoSuitableConstructorException(String s)
    {
        super(s);
    }
}
