package dicontainer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dicontainer.auxiliary.CustomProvider;
import dicontainer.auxiliary.basics.ClassBasicsComplexDependency;
import dicontainer.auxiliary.basics.InterfaceBasicsComplexDependency;
import dicontainer.auxiliary.basics.InterfaceBasicsStringGetter;
import dicontainer.auxiliary.diamonds.InterfaceDiamonds1;
import dicontainer.auxiliary.diamonds.InterfaceDiamondsBottom;
import dicontainer.auxiliary.setters.ClassSettersDouble;
import dicontainer.auxiliary.setters.InterfaceSettersDouble;
import dicontainer.exception.DIException;
import dicontainer.exception.EmptyContainerProviderException;
import dicontainer.exception.IncorrectDependencySetterException;
import dicontainer.exception.MissingDependenciesException;

public class DIServiceLocatorTest
{
    private DIContainer container;
    private CustomProvider provider;

    @Before
    public void setUp()
    {
        container = new DIContainer();
        provider = new CustomProvider(container);
    }

    @After
    public void tearDown()
    {
        container = null;
        provider = null;
    }

    @Test
    public void hasProvider_WhenProviderSet_ThenTrue()
    {
        DIServiceLocator.setProvider(provider);

        boolean result = DIServiceLocator.hasProvider();

        Assert.assertTrue(result);
    }

    @Test
    public void hasProvider_WhenProviderNotSet_ThenFalse()
    {
        DIServiceLocator.setProvider(null);

        boolean result = DIServiceLocator.hasProvider();

        Assert.assertFalse(result);
    }

    @Test
    public void resolve_WhenAllDependenciesArePresent_ThenInstanceIsBuiltUp()
    {
        DIServiceLocator.setProvider(provider);

        InterfaceBasicsComplexDependency result = null;

        try
        {
            result = DIServiceLocator.resolve(InterfaceBasicsComplexDependency.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getBasicObject());
        Assert.assertNotNull(result.getFirstObject());
        Assert.assertNotNull(result.getSecondObject());
        Assert.assertNotNull(result.getFirstObject().getObject());
        Assert.assertNotNull(result.getSecondObject().getString());
        Assert.assertEquals("string", result.getSecondObject().getString());
        Assert.assertTrue(result instanceof ClassBasicsComplexDependency);
    }

    @Test(expected = MissingDependenciesException.class)
    public void resolve_WhenMissingDependencies_ThenMissingDependenciesException()
            throws DIException
    {
        DIServiceLocator.setProvider(provider);

        DIServiceLocator.resolve(InterfaceDiamondsBottom.class);
    }

    @Test(expected = EmptyContainerProviderException.class)
    public void resolve_WhenProviderIsNull_ThenEmptyContainerProviderException()
            throws DIException
    {
        DIServiceLocator.setProvider(null);

        DIServiceLocator.resolve(InterfaceBasicsComplexDependency.class);
    }

    @Test
    public void getContainer_WhenProviderPresent_ThenContainerInstance()
    {
        DIServiceLocator.setProvider(provider);

        DIContainer result = DIServiceLocator.getContainer();

        Assert.assertNotNull(result);
        Assert.assertSame(container, result);
    }

    @Test(expected = EmptyContainerProviderException.class)
    public void getContainer_WhenProviderIsNull_ThenEmptyContainerProviderException()
    {
        DIServiceLocator.setProvider(null);

        DIServiceLocator.getContainer();
    }

    @Test
    public void buildUp_WhenAllDependenciesArePresent_ThenInstanceIsBuiltUp()
    {
        DIServiceLocator.setProvider(provider);

        InterfaceBasicsComplexDependency instance = null;
        InterfaceBasicsComplexDependency result = null;

        try
        {
            InterfaceDiamonds1 diamond1 = DIServiceLocator.resolve(InterfaceDiamonds1.class);
            InterfaceBasicsStringGetter withString =
                    DIServiceLocator.resolve(InterfaceBasicsStringGetter.class);

            instance = new ClassBasicsComplexDependency(diamond1, withString);
            result = DIServiceLocator.buildUp(instance);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assert.assertNotNull(result);
        Assert.assertNotNull(instance);
        Assert.assertNotNull(result.getBasicObject());
        Assert.assertNotNull(instance.getBasicObject());
        Assert.assertNotNull(result.getFirstObject());
        Assert.assertNotNull(instance.getFirstObject());
        Assert.assertNotNull(result.getSecondObject());
        Assert.assertNotNull(instance.getSecondObject());
        Assert.assertNotNull(result.getFirstObject().getObject());
        Assert.assertNotNull(instance.getFirstObject().getObject());
        Assert.assertNotNull(result.getSecondObject().getString());
        Assert.assertNotNull(instance.getSecondObject().getString());
        Assert.assertEquals("string", result.getSecondObject().getString());
        Assert.assertEquals("string", instance.getSecondObject().getString());
        Assert.assertSame(instance, result);
    }

    @Test(expected = IncorrectDependencySetterException.class)
    public void buildUp_WhenIncorrectSetters_ThenIncorrectDependencySetterException()
            throws DIException
    {
        DIServiceLocator.setProvider(provider);

        InterfaceSettersDouble instance = new ClassSettersDouble();

        DIServiceLocator.buildUp(instance);
    }
}
