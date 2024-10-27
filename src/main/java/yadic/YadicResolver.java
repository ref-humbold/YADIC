package yadic;

public interface YadicResolver
{
    /**
     * Resolve all dependencies and construct a new instance of given type.
     * @param type type class
     * @return new instance
     * @throws YadicException if type cannot be resolved
     */
    <T> T resolve(Class<T> type);

    /**
     * Resolve all dependencies and construct a new instance of given type.
     * @param type type class
     * @return new instance, or {@code null} if type cannot be resolved.
     */
    default <T> T resolveOrNull(Class<T> type)
    {
        try
        {
            return resolve(type);
        }
        catch(YadicException ex)
        {
            return null;
        }
    }
}
