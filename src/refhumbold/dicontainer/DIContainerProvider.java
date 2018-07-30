package refhumbold.dicontainer;

public abstract class DIContainerProvider
{
    private final DIContainer container;

    public DIContainerProvider(DIContainer container)
    {
        this.container = container;
        configure(this.container);
    }

    public DIContainer getContainer()
    {
        return this.container;
    }

    protected abstract void configure(DIContainer container);
}
