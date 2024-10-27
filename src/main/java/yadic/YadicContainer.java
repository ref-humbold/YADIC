package yadic;

import yadic.registry.DependencyRegistry;
import yadic.resolver.TypesResolver;

public final class YadicContainer
        implements YadicResolver
{
    private final DependencyRegistry registry;
    private final TypesResolver resolver;

    public YadicContainer()
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
    public <T> YadicContainer registerType(Class<T> type, ConstructionPolicy policy)
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
    public <T> YadicContainer registerType(
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
    public <T> YadicContainer registerInstance(Class<T> type, T instance)
    {
        registry.addInstance(type, instance);
        return this;
    }

    @Override
    public <T> T resolve(Class<T> type)
    {
        return resolver.resolve(type);
    }
}
