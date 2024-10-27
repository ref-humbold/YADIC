package yadic;

import java.io.Serial;

public class YadicException
        extends RuntimeException
{
    @Serial private static final long serialVersionUID = -3019200382390630637L;

    public YadicException(String message)
    {
        super(message);
    }

    public YadicException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
