package dicontainer.dictionary.exception;

import dicontainer.DiException;

public class NotDerivedTypeException
        extends DiException
{
    private static final long serialVersionUID = -3180961583302361880L;

    public NotDerivedTypeException(String s)
    {
        super(s);
    }
}
