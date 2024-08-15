package yadic;

import yadic.annotation.Dependency;
import yadic.registry.DependencyRegistry;
import yadic.resolver.TypesResolver;

public final class DiContainer
        implements DiResolver
{
    private final DependencyRegistry registry;
    private final TypesResolver resolver;

    public DiContainer()
    {
        registry = new DependencyRegistry();
        resolver = new TypesResolver(registry);
    }

    /**
     * Register concrete type class in the container with singleton specification.
     * @param type type class
     * @param policy construction policy of instances
     * @return {@code this} for method chaining
     */
    public <T> DiContainer registerType(Class<T> type, ConstructionPolicy policy)
    {
        registry.addType(type, policy);
        return this;
    }

    /**
     * Register subtype class for its supertype.
     * @param supertype supertype class
     * @param subtype subtype class
     * @param policy construction policy of instances
     * @return {@code this} for method chaining
     */
    public <T> DiContainer registerType(
            Class<T> supertype, Class<? extends T> subtype, ConstructionPolicy policy)
    {
        registry.addType(supertype, subtype, policy);
        return this;
    }

    /**
     * Register concrete instance of its type.
     * @param type type class
     * @param instance concrete instance
     * @return {@code this} for method chaining
     */
    public <T> DiContainer registerInstance(Class<T> type, T instance)
    {
        registry.addInstance(type, instance);
        return this;
    }

    /**
     * Resolve all depencencies and construct a new instance of given type using {@link Dependency}.
     * @param type type class
     * @return new instance
     * @throws DiException if type cannot be resolved
     */
    @Override
    public <T> T resolve(Class<T> type)
    {
        return resolver.resolve(type);
    }
}
