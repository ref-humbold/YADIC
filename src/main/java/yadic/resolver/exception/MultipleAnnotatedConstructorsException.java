package yadic.resolver.exception;

import yadic.DiException;

public class MultipleAnnotatedConstructorsException
        extends DiException
{
    private static final long serialVersionUID = 9149838622398855405L;

    public MultipleAnnotatedConstructorsException(String s)
    {
        super(s);
    }
}
