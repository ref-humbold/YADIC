package yadic.resolver.exception;

import yadic.YadicException;

public class MultipleAnnotatedConstructorsException
        extends YadicException
{
    private static final long serialVersionUID = 9149838622398855405L;

    public MultipleAnnotatedConstructorsException(String s)
    {
        super(s);
    }
}
