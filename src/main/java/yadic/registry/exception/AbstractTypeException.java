package yadic.registry.exception;

import java.io.Serial;

import yadic.DiException;

public class AbstractTypeException
        extends DiException
{
    @Serial private static final long serialVersionUID = 5573956461991224741L;

    public AbstractTypeException(String s)
    {
        super(s);
    }
}
