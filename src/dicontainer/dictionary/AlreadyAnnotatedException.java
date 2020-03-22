package dicontainer.dictionary;

import dicontainer.exception.DIException;

public class AlreadyAnnotatedException
        extends DIException
{
    private static final long serialVersionUID = 4583310501511173747L;

    public AlreadyAnnotatedException(String s)
    {
        super(s);
    }
}
