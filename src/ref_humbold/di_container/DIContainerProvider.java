package ref_humbold.di_container;

public abstract class DIContainerProvider
{
    public DIContainerProvider()
    {
    }

    public abstract DIContainer getContainer();

    protected abstract void configureContainer(DIContainer container);
}
