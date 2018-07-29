package ref_humbold.di_container;

import ref_humbold.di_container.exception.DIException;
import ref_humbold.di_container.exception.EmptyContainerProviderException;

public final class DIServiceLocator
{
    private static DIContainerProvider containerProvider = null;

    public static void setContainerProvider(DIContainerProvider provider)
    {
        DIServiceLocator.containerProvider = provider;
    }

    public static boolean isProviderPresent()
    {
        return DIServiceLocator.containerProvider != null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T resolve(Class<T> type)
        throws DIException
    {
        if(DIServiceLocator.containerProvider == null)
            throw new EmptyContainerProviderException("Container provider is empty.");

        return type == DIContainer.class ? (T)DIServiceLocator.containerProvider.getContainer()
                                         : DIServiceLocator.containerProvider.getContainer()
                                                                             .resolve(type);
    }

    public static <T> T buildUp(T instance)
        throws DIException
    {
        if(DIServiceLocator.containerProvider == null)
            throw new EmptyContainerProviderException("Container provider is empty.");

        return DIServiceLocator.containerProvider.getContainer().buildUp(instance);
    }
}
