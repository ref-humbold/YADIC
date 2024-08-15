package yadic.registry.exception;

import java.io.Serial;

import yadic.DiException;

public class RegistrationException
        extends DiException
{
    @Serial private static final long serialVersionUID = 2937758897207647321L;

    public RegistrationException(String message)
    {
        super(message);
    }
}
