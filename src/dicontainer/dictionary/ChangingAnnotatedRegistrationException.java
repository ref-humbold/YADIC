package dicontainer.dictionary;

import dicontainer.exception.DIException;

public class ChangingAnnotatedRegistrationException
        extends DIException
{
    private static final long serialVersionUID = 4583310501511173747L;

    public ChangingAnnotatedRegistrationException(String message)
    {
        super(message);
    }
}
