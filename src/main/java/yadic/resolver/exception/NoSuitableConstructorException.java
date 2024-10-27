package yadic.resolver.exception;

import yadic.YadicException;

public class NoSuitableConstructorException
        extends YadicException
{
    private static final long serialVersionUID = 902034423730601801L;

    public NoSuitableConstructorException(String s)
    {
        super(s);
    }
}
