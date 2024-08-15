package yadic;

import yadic.annotation.Dependency;

public interface DiResolver
{
    /**
     * Resolve all dependencies and construct a new instance of given type using {@link Dependency}.
     * @param type type class
     * @return new instance
     * @throws DiException if type cannot be resolved
     */
    <T> T resolve(Class<T> type);

    /**
     * Resolve all dependencies and construct a new instance of given type using {@link Dependency}.
     * @param type type class
     * @return new instance, or {@code null} if type cannot be resolved.
     */
    default <T> T resolveOrNull(Class<T> type)
    {
        try
        {
            return resolve(type);
        }
        catch(DiException ex)
        {
            return null;
        }
    }
}
