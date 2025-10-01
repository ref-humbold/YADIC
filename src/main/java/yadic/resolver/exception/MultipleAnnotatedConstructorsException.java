package yadic.resolver.exception;

import java.io.Serial;

import yadic.YadicException;

public class MultipleAnnotatedConstructorsException
        extends YadicException
{
    @Serial private static final long serialVersionUID = 9149838622398855405L;

    public MultipleAnnotatedConstructorsException(String message)
    {
        super(message);
    }
}
