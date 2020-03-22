package dicontainer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dicontainer.auxiliary.CustomProvider;
import dicontainer.auxiliary.basic.ClassBasicComplexDependency;
import dicontainer.auxiliary.basic.InterfaceBasicComplexDependency;
import dicontainer.auxiliary.basic.InterfaceBasicStringGetter;
import dicontainer.auxiliary.diamond.InterfaceDiamondBottom;
import dicontainer.auxiliary.diamond.InterfaceDiamondLeft;
import dicontainer.auxiliary.setter.ClassSetterDouble;
import dicontainer.auxiliary.setter.InterfaceSetterDouble;
import dicontainer.exception.DIException;
import dicontainer.exception.EmptyContainerProviderException;
import dicontainer.exception.IncorrectDependencySetterException;
import dicontainer.exception.MissingDependenciesException;

public class DIServiceLocatorTest
{
    private DIContainer container;
    private CustomProvider provider;

    @BeforeEach
    public void setUp()
    {
        container = new DIContainer();
        provider = new CustomProvider(container);
    }

    @AfterEach
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

        Assertions.assertTrue(result);
    }

    @Test
    public void hasProvider_WhenProviderNotSet_ThenFalse()
    {
        DIServiceLocator.setProvider(null);

        boolean result = DIServiceLocator.hasProvider();

        Assertions.assertFalse(result);
    }

    @Test
    public void resolve_WhenAllDependenciesArePresent_ThenInstanceIsBuiltUp()
    {
        DIServiceLocator.setProvider(provider);

        InterfaceBasicComplexDependency result = null;

        try
        {
            result = DIServiceLocator.resolve(InterfaceBasicComplexDependency.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBasicObject());
        Assertions.assertNotNull(result.getFirstObject());
        Assertions.assertNotNull(result.getSecondObject());
        Assertions.assertNotNull(result.getFirstObject().getObject());
        Assertions.assertNotNull(result.getSecondObject().getString());
        Assertions.assertEquals("string", result.getSecondObject().getString());
        Assertions.assertTrue(result instanceof ClassBasicComplexDependency);
    }

    @Test
    public void resolve_WhenMissingDependencies_ThenMissingDependenciesException()
    {
        DIServiceLocator.setProvider(provider);

        Assertions.assertThrows(MissingDependenciesException.class,
                                () -> DIServiceLocator.resolve(InterfaceDiamondBottom.class));
    }

    @Test
    public void resolve_WhenProviderIsNull_ThenEmptyContainerProviderException()
    {
        DIServiceLocator.setProvider(null);

        Assertions.assertThrows(EmptyContainerProviderException.class,
                                () -> DIServiceLocator.resolve(
                                        InterfaceBasicComplexDependency.class));
    }

    @Test
    public void getContainer_WhenProviderPresent_ThenContainerInstance()
    {
        DIServiceLocator.setProvider(provider);

        DIContainer result = DIServiceLocator.getContainer();

        Assertions.assertNotNull(result);
        Assertions.assertSame(container, result);
    }

    @Test
    public void getContainer_WhenProviderIsNull_ThenEmptyContainerProviderException()
    {
        DIServiceLocator.setProvider(null);

        Assertions.assertThrows(EmptyContainerProviderException.class,
                                () -> DIServiceLocator.getContainer());
    }

    @Test
    public void buildUp_WhenAllDependenciesArePresent_ThenInstanceIsBuiltUp()
    {
        DIServiceLocator.setProvider(provider);

        InterfaceBasicComplexDependency instance = null;
        InterfaceBasicComplexDependency result = null;

        try
        {
            InterfaceDiamondLeft diamond1 = DIServiceLocator.resolve(InterfaceDiamondLeft.class);
            InterfaceBasicStringGetter withString =
                    DIServiceLocator.resolve(InterfaceBasicStringGetter.class);

            instance = new ClassBasicComplexDependency(diamond1, withString);
            result = DIServiceLocator.buildUp(instance);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(instance);
        Assertions.assertNotNull(result.getBasicObject());
        Assertions.assertNotNull(instance.getBasicObject());
        Assertions.assertNotNull(result.getFirstObject());
        Assertions.assertNotNull(instance.getFirstObject());
        Assertions.assertNotNull(result.getSecondObject());
        Assertions.assertNotNull(instance.getSecondObject());
        Assertions.assertNotNull(result.getFirstObject().getObject());
        Assertions.assertNotNull(instance.getFirstObject().getObject());
        Assertions.assertNotNull(result.getSecondObject().getString());
        Assertions.assertNotNull(instance.getSecondObject().getString());
        Assertions.assertEquals("string", result.getSecondObject().getString());
        Assertions.assertEquals("string", instance.getSecondObject().getString());
        Assertions.assertSame(instance, result);
    }

    @Test
    public void buildUp_WhenIncorrectSetters_ThenIncorrectDependencySetterException()
    {
        DIServiceLocator.setProvider(provider);

        InterfaceSetterDouble instance = new ClassSetterDouble();

        Assertions.assertThrows(IncorrectDependencySetterException.class,
                                () -> DIServiceLocator.buildUp(instance));
    }
}
