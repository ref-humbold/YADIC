package dicontainer.commons;

import java.util.NoSuchElementException;
import java.util.Objects;

public final class Instance<T>
{
    public static final Instance<?> noMapping = new Instance<>(null);
    private final T instance;

    private Instance(T instance)
    {
        this.instance = instance;
    }

    @SuppressWarnings("unchecked")
    public static <T> Instance<T> none()
    {
        return (Instance<T>)noMapping;
    }

    public static <T> Instance<T> make(T instance)
    {
        Objects.requireNonNull(instance);
        return new Instance<>(instance);
    }

    public static <T> Instance<T> cast(Instance<T> mapping)
    {
        return mapping == null ? none() : mapping;
    }

    public boolean exists()
    {
        return instance != null;
    }

    public T extract()
    {
        if(!exists())
            throw new NoSuchElementException("No instance found");

        return instance;
    }
}
