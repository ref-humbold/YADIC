package refhumbold.dicontainer;

public abstract class DIContainerProvider
{
    private final DIContainer container;

    public DIContainerProvider(DIContainer container)
    {
        this.container = container;
        configure(container);
    }

    public DIContainer getContainer()
    {
        return container;
    }

    protected abstract void configure(DIContainer container);
}
