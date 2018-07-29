package refhumbold.dicontainer;

public abstract class DIContainerProvider
{
    public DIContainerProvider()
    {
    }

    public abstract DIContainer getContainer();

    protected abstract void configureContainer(DIContainer container);
}
