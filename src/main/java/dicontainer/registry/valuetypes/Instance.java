package dicontainer.registry.valuetypes;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Supplier;

public final class Instance<T>
{
    private final T instance;
    private final RuntimeException exception;

    private Instance(T instance, RuntimeException exception)
    {
        this.instance = instance;
        this.exception = exception;
    }

    public static <T> Instance<T> of(T instance)
    {
        return instance == null ? none() : new Instance<>(instance, null);
    }

    public static <T> Instance<T> of(T instance, RuntimeException exception)
    {
        return instance == null ? none(exception) : new Instance<>(instance, null);
    }

    public static <T> Instance<T> none()
    {
        return none(new NoSuchElementException("No instance found"));
    }

    public static <T> Instance<T> none(RuntimeException exception)
    {
        return new Instance<>(null, Objects.requireNonNull(exception));
    }

    @SuppressWarnings("unchecked")
    public static <T> Instance<T> cast(Instance<?> mapping)
    {
        return mapping == null ? none() : (Instance<T>)mapping;
    }

    public boolean exists()
    {
        return instance != null;
    }

    public Instance<T> or(Supplier<Instance<T>> supplier)
    {
        return exists() ? this : supplier.get();
    }

    public T extract()
    {
        return extract(() -> { throw exception; });
    }

    public T extract(Supplier<T> supplier)
    {
        return exists() ? instance : supplier.get();
    }
}
