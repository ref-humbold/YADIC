package ref_humbold.di_container;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DIServiceLocatorTest
{
    private DIContainer container;
    private TestProvider provider;

    @Before
    public void setUp()
        throws Exception
    {
        container = new DIContainer();
        provider = new TestProvider(container);
    }

    @After
    public void tearDown()
        throws Exception
    {
        container = null;
        provider = null;
    }

    @Test
    public void testIsProviderPresentWhenProviderSet()
    {
        DIServiceLocator.setContainerProvider(provider);

        boolean result = DIServiceLocator.isProviderPresent();

        Assert.assertTrue(result);
    }

    @Test
    public void testIsProviderPresentWhenProviderNotSet()
    {
        DIServiceLocator.setContainerProvider(null);

        boolean result = DIServiceLocator.isProviderPresent();

        Assert.assertFalse(result);
    }

    @Test
    public void testGetInstanceWhenAllDependenciesArePresent()
    {
        DIServiceLocator.setContainerProvider(provider);

        TestInterfaceComplexDependency cls = null;

        try
        {
            cls = DIServiceLocator.getObject(TestInterfaceComplexDependency.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
        Assert.assertNotNull(cls.getBasicObject());
        Assert.assertNotNull(cls.getFirstObject());
        Assert.assertNotNull(cls.getSecondObject());
        Assert.assertNotNull(cls.getFirstObject().getObject());
        Assert.assertNotNull(cls.getSecondObject().getString());
        Assert.assertEquals("string", cls.getSecondObject().getString());
        Assert.assertTrue(cls instanceof TestClassComplexDependency);
    }

    @Test(expected = MissingDependenciesException.class)
    public void testGetInstanceWhenMissingDependencies()
        throws DIException
    {
        DIServiceLocator.setContainerProvider(provider);

        DIServiceLocator.getObject(TestInterfaceDiamondBase.class);
    }

    @Test(expected = EmptyContainerProviderException.class)
    public void testGetInstanceWhenContainerProviderIsNull()
        throws DIException
    {
        DIServiceLocator.setContainerProvider(null);

        DIServiceLocator.getObject(TestInterfaceComplexDependency.class);
    }

    @Test
    public void testGetInstanceWhenContainerType()
    {
        DIServiceLocator.setContainerProvider(provider);

        DIContainer cnt = null;

        try
        {
            cnt = DIServiceLocator.getObject(DIContainer.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cnt);
        Assert.assertSame(container, cnt);
    }
}
