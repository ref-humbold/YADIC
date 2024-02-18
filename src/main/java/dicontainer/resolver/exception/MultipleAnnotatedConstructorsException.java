package dicontainer.resolver.exception;

import dicontainer.DiException;

public class MultipleAnnotatedConstructorsException
        extends DiException
{
    private static final long serialVersionUID = 9149838622398855405L;

    public MultipleAnnotatedConstructorsException(String s)
    {
        super(s);
    }
}
