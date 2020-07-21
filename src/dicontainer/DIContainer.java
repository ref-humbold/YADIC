package dicontainer;

import dicontainer.annotation.Dependency;
import dicontainer.dictionary.DIDictionary;
import dicontainer.exception.DIException;
import dicontainer.resolver.DIResolver;

public final class DIContainer
{
    private final DIDictionary diDictionary;
    private final DIResolver diResolver;

    public DIContainer()
    {
        diDictionary = new DIDictionary();
        diResolver = new DIResolver(diDictionary);
    }

    /**
     * Register concrete type class in the container.
     * @param type type class
     * @return {@code this} for method chaining
     */
    public <T> DIContainer registerType(Class<T> type)
    {
        diDictionary.addType(type);
        return this;
    }

    /**
     * Register concrete type class in the container with singleton specification.
     * @param type type class
     * @param policy construction policy of instances
     * @return {@code this} for method chaining
     */
    public <T> DIContainer registerType(Class<T> type, ConstructionPolicy policy)
    {
        diDictionary.addType(type, policy);
        return this;
    }

    /**
     * Register subtype class for its supertype.
     * @param supertype supertype class
     * @param subtype subtype class
     * @return {@code this} for method chaining
     */
    public <T> DIContainer registerType(Class<T> supertype, Class<? extends T> subtype)
    {
        diDictionary.addType(supertype, subtype);
        return this;
    }

    /**
     * Register subtype class for its supertype.
     * @param supertype supertype class
     * @param subtype subtype class
     * @param policy construction policy of instances
     * @return {@code this} for method chaining
     */
    public <T> DIContainer registerType(Class<T> supertype, Class<? extends T> subtype,
                                        ConstructionPolicy policy)
    {
        diDictionary.addType(supertype, subtype, policy);
        return this;
    }

    /**
     * Register concrete instance of its type.
     * @param type type class
     * @param instance concrete instance
     * @return {@code this} for method chaining
     */
    public <T> DIContainer registerInstance(Class<T> type, T instance)
    {
        diDictionary.addInstance(type, instance);
        return this;
    }

    /**
     * Resolve all depencencies and construct a new instance of given type using {@link Dependency}.
     * @param type type class
     * @return new instance
     * @throws DIException if type cannot be resolved
     */
    public <T> T resolve(Class<T> type)
    {
        return diResolver.construct(type);
    }

    /**
     * Resolve and inject all dependencies to given object using {@link Dependency} on setters.
     * @param instance instance object
     * @return resolved instance
     * @throws DIException if instance cannot be built up
     */
    public <T> T buildUp(T instance)
    {
        return diResolver.inject(instance);
    }
}
