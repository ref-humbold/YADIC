package ref_humbold.di_container;

import ref_humbold.di_container.exception.DIException;
import ref_humbold.di_container.exception.EmptyContainerProviderException;

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

    @SuppressWarnings("unchecked")
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
