package yadic.resolver.exception;

import yadic.YadicException;

public class CircularDependenciesException
        extends YadicException
{
    private static final long serialVersionUID = 607229069481348756L;

    public CircularDependenciesException(String s)
    {
        super(s);
    }
}
