package dicontainer;

import dicontainer.exception.EmptyContainerProviderException;

public final class DIServiceLocator
{
    private static DIContainerProvider provider = null;

    public static void setProvider(DIContainerProvider provider)
    {
        DIServiceLocator.provider = provider;
    }

    public static boolean hasProvider()
    {
        return provider != null;
    }

    public static DIContainer getContainer()
    {
        if(!hasProvider())
            throw new EmptyContainerProviderException("Container provider is empty.");

        return provider.getContainer();
    }

    public static <T> T resolve(Class<T> type)
    {
        if(!hasProvider())
            throw new EmptyContainerProviderException("Container provider is empty.");

        return getContainer().resolve(type);
    }

    public static <T> T buildUp(T instance)
    {
        if(!hasProvider())
            throw new EmptyContainerProviderException("Container provider is empty.");

        return getContainer().buildUp(instance);
    }
}
