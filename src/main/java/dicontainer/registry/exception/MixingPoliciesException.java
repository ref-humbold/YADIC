package dicontainer.registry.exception;

import java.io.Serial;

import dicontainer.DiException;

public class MixingPoliciesException
        extends DiException
{
    @Serial private static final long serialVersionUID = 7409446718002438943L;

    public MixingPoliciesException(String message)
    {
        super(message);
    }
}
