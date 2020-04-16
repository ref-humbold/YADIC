package dicontainer.dictionary;

import dicontainer.exception.DIException;

public class AnnotatedTypeRegistrationException
        extends DIException
{
    private static final long serialVersionUID = 4583310501511173747L;

    public AnnotatedTypeRegistrationException(String message)
    {
        super(message);
    }
}
