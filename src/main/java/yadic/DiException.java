package yadic;

import java.io.Serial;

public class DiException
        extends RuntimeException
{
    @Serial private static final long serialVersionUID = -3019200382390630637L;

    public DiException(String message)
    {
        super(message);
    }

    public DiException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
