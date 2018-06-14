package ref_humbold.di_container;

public final class DIServiceLocator
{
    private static DIContainerProvider containerProvider = null;

    public static void setContainerProvider(DIContainerProvider provider)
    {
        containerProvider = provider;
    }

    public static boolean isProviderPresent()
    {
        return containerProvider != null;
    }

    public static <T> T getObject(Class<T> cls)
        throws DIException
    {
        if(containerProvider == null)
            throw new EmptyContainerProviderException("Container provider is empty.");

        if(cls == DIContainer.class)
            return (T)containerProvider.getContainer();

        return containerProvider.getContainer().resolve(cls);
    }
}
