package dicontainer.commons;

import dicontainer.DIException;

public class NullInstanceException
        extends DIException
{
    private static final long serialVersionUID = -2453276089439956670L;

    public NullInstanceException(String s)
    {
        super(s);
    }
}
