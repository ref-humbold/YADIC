package yadic.resolver.exception;

import yadic.YadicException;

public class MissingDependenciesException
        extends YadicException
{
    private static final long serialVersionUID = 7265031467533657162L;

    public MissingDependenciesException(String s)
    {
        super(s);
    }
}
