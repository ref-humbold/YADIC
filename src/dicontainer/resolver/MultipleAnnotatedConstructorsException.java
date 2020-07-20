package dicontainer.resolver;

import dicontainer.exception.DIException;

public class MultipleAnnotatedConstructorsException
        extends DIException
{
    private static final long serialVersionUID = 9149838622398855405L;

    public MultipleAnnotatedConstructorsException(String s)
    {
        super(s);
    }
}
