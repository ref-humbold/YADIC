package dicontainer.dictionary.valuetypes;

import java.util.Objects;

public final class Instance<T>
{
    private final T instance;
    private final RuntimeException exception;

    private Instance(T instance, RuntimeException exception)
    {
        this.instance = instance;
        this.exception = exception;
    }

    public static <T> Instance<T> none()
    {
        return none(new NullInstanceException("No instance found"));
    }

    public static <T> Instance<T> none(RuntimeException exception)
    {
        Objects.requireNonNull(exception);
        return new Instance<>(null, exception);
    }

    public static <T> Instance<T> of(T instance)
    {
        return instance == null ? none() : new Instance<>(instance, null);
    }

    public static <T> Instance<T> of(T instance, RuntimeException exception)
    {
        return instance == null ? none(exception) : new Instance<>(instance, null);
    }

    @SuppressWarnings("unchecked")
    public static <T> Instance<T> cast(Instance<?> mapping)
    {
        return mapping == null ? none() : (Instance<T>)mapping;
    }

    public RuntimeException getException()
    {
        return exception;
    }

    public boolean exists()
    {
        return instance != null;
    }

    public T extract()
    {
        if(!exists())
            throw exception;

        return instance;
    }
}
