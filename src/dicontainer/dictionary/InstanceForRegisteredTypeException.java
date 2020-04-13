package dicontainer.dictionary;

import dicontainer.exception.DIException;

public class InstanceForRegisteredTypeException
        extends DIException
{
    private static final long serialVersionUID = 2937758897207647321L;

    public InstanceForRegisteredTypeException(String message)
    {
        super(message);
    }
}
