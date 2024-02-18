package dicontainer.dictionary.valuetypes;

import dicontainer.DiException;

public class NullInstanceException
        extends DiException
{
    private static final long serialVersionUID = -2453276089439956670L;

    public NullInstanceException(String s)
    {
        super(s);
    }
}
