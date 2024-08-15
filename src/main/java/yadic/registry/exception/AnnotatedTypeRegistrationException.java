package yadic.registry.exception;

import java.io.Serial;

import yadic.DiException;

public class AnnotatedTypeRegistrationException
        extends DiException
{
    @Serial private static final long serialVersionUID = 4583310501511173747L;

    public AnnotatedTypeRegistrationException(String message)
    {
        super(message);
    }
}
