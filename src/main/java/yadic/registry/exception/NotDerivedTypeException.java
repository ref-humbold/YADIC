package yadic.registry.exception;

import java.io.Serial;

import yadic.DiException;

public class NotDerivedTypeException
        extends DiException
{
    @Serial private static final long serialVersionUID = -3180961583302361880L;

    public NotDerivedTypeException(String s)
    {
        super(s);
    }
}
