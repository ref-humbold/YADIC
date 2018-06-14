package ref_humbold.di_container;

public abstract class DIContainerBaseProvider
    extends DIContainerProvider
{
    private DIContainer container;

    public DIContainerBaseProvider(DIContainer container)
    {
        super();
        this.container = container;
        configureContainer(this.container);
    }

    @Override
    public DIContainer getContainer()
    {
        return this.container;
    }
}
