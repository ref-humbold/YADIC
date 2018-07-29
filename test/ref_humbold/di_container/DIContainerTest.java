package ref_humbold.di_container;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ref_humbold.di_container.auxiliary.basics.*;
import ref_humbold.di_container.auxiliary.circulars.*;
import ref_humbold.di_container.auxiliary.constructors.*;
import ref_humbold.di_container.auxiliary.diamonds.*;
import ref_humbold.di_container.auxiliary.register.*;
import ref_humbold.di_container.auxiliary.setters.*;
import ref_humbold.di_container.exception.*;

public class DIContainerTest
{
    private DIContainer testObject;

    @Before
    public void setUp()
    {
        testObject = new DIContainer();
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    // region registerType (single class)

    @Test
    public void testRegisterWhenSingleClass()
    {
        try
        {
            testObject.registerType(ClassConstructorsDefault.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        ClassConstructorsDefault result1 = null;
        ClassConstructorsDefault result2 = null;

        try
        {
            result1 = testObject.resolve(ClassConstructorsDefault.class);
            result2 = testObject.resolve(ClassConstructorsDefault.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertNotSame(result1, result2);
    }

    @Test
    public void testRegisterWhenSingleClassAsSingleton()
    {
        try
        {
            testObject.registerType(ClassConstructorsDefault.class, ConstructionPolicy.SINGLETON);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        ClassConstructorsDefault result1 = null;
        ClassConstructorsDefault result2 = null;

        try
        {
            result1 = testObject.resolve(ClassConstructorsDefault.class);
            result2 = testObject.resolve(ClassConstructorsDefault.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertSame(result1, result2);
    }

    @Test
    public void testRegisterWhenSingleClassChangesSingleton()
    {
        try
        {
            testObject.registerType(ClassConstructorsDefault.class, ConstructionPolicy.SINGLETON);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        ClassConstructorsDefault result11 = null;
        ClassConstructorsDefault result12 = null;

        try
        {
            result11 = testObject.resolve(ClassConstructorsDefault.class);
            result12 = testObject.resolve(ClassConstructorsDefault.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result11);
        Assert.assertNotNull(result12);
        Assert.assertSame(result11, result12);

        try
        {
            testObject.registerType(ClassConstructorsDefault.class, ConstructionPolicy.CONSTRUCT);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        ClassConstructorsDefault result21 = null;
        ClassConstructorsDefault result22 = null;

        try
        {
            result21 = testObject.resolve(ClassConstructorsDefault.class);
            result22 = testObject.resolve(ClassConstructorsDefault.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result21);
        Assert.assertNotNull(result22);
        Assert.assertNotSame(result21, result22);
    }

    @Test(expected = AbstractTypeException.class)
    public void testRegisterWhenSingleClassIsInterface()
        throws AbstractTypeException
    {
        testObject.registerType(InterfaceBasics.class);
    }

    @Test(expected = AbstractTypeException.class)
    public void testRegisterWhenSingleClassIsAbstractClass()
        throws AbstractTypeException
    {
        testObject.registerType(ClassBasicsAbstract.class);
    }

    // endregion
    // region registerType (inheritance)

    @Test
    public void testRegisterWhenInheritanceFromInterface()
    {
        testObject.registerType(InterfaceBasics.class, ClassConstructorsDefault.class);

        InterfaceBasics result1 = null;
        InterfaceBasics result2 = null;

        try
        {
            result1 = testObject.resolve(InterfaceBasics.class);
            result2 = testObject.resolve(InterfaceBasics.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertNotSame(result1, result2);
        Assert.assertTrue(result1 instanceof ClassConstructorsDefault);
        Assert.assertTrue(result2 instanceof ClassConstructorsDefault);
    }

    @Test
    public void testRegisterWhenInheritanceFromInterfaceAsSingleton()
    {
        testObject.registerType(InterfaceBasics.class, ClassConstructorsDefault.class,
                                ConstructionPolicy.SINGLETON);

        InterfaceBasics result1 = null;
        InterfaceBasics result2 = null;

        try
        {
            result1 = testObject.resolve(InterfaceBasics.class);
            result2 = testObject.resolve(InterfaceBasics.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertSame(result1, result2);
        Assert.assertTrue(result1 instanceof ClassConstructorsDefault);
        Assert.assertTrue(result2 instanceof ClassConstructorsDefault);
    }

    @Test
    public void testRegisterWhenInheritanceFromInterfaceChangesSingleton()
    {
        testObject.registerType(InterfaceBasics.class, ClassConstructorsDefault.class,
                                ConstructionPolicy.SINGLETON);

        InterfaceBasics result11 = null;
        InterfaceBasics result12 = null;

        try
        {
            result11 = testObject.resolve(InterfaceBasics.class);
            result12 = testObject.resolve(InterfaceBasics.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result11);
        Assert.assertNotNull(result12);
        Assert.assertSame(result11, result12);
        Assert.assertTrue(result11 instanceof ClassConstructorsDefault);
        Assert.assertTrue(result12 instanceof ClassConstructorsDefault);

        testObject.registerType(InterfaceBasics.class, ClassConstructorsDefault.class,
                                ConstructionPolicy.CONSTRUCT);

        InterfaceBasics result21 = null;
        InterfaceBasics result22 = null;

        try
        {
            result21 = testObject.resolve(InterfaceBasics.class);
            result22 = testObject.resolve(InterfaceBasics.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result21);
        Assert.assertNotNull(result22);
        Assert.assertNotSame(result21, result22);
        Assert.assertTrue(result21 instanceof ClassConstructorsDefault);
        Assert.assertTrue(result22 instanceof ClassConstructorsDefault);
    }

    @Test
    public void testRegisterWhenInheritanceFromInterfaceChangesClass()
    {
        testObject.registerType(InterfaceBasics.class, ClassConstructorsDefault.class);

        InterfaceBasics result1 = null;

        try
        {
            result1 = testObject.resolve(InterfaceBasics.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result1);
        Assert.assertTrue(result1 instanceof ClassConstructorsDefault);

        testObject.registerType(InterfaceBasics.class, ClassConstructorsDefaultAndParameter.class);

        InterfaceBasics result3 = null;

        try
        {
            result3 = testObject.resolve(InterfaceBasics.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result3);
        Assert.assertTrue(result3 instanceof ClassConstructorsDefaultAndParameter);
    }

    @Test
    public void testRegisterWhenInheritanceFromAbstractClass()
    {
        testObject.registerType(ClassBasicsAbstract.class, ClassBasicsInheritsFromAbstract.class);

        ClassBasicsAbstract result = null;

        try
        {
            result = testObject.resolve(ClassBasicsAbstract.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof ClassBasicsInheritsFromAbstract);
    }

    @Test
    public void testRegisterWhenInheritanceFromConcreteClass()
    {
        testObject.registerType(ClassConstructorsParameter.class,
                                ClassConstructorsInheritParameter.class);

        ClassConstructorsParameter result = null;

        try
        {
            result = testObject.resolve(ClassConstructorsParameter.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof ClassConstructorsInheritParameter);
    }

    @Test
    public void testRegisterWhenTwoStepsOfHierarchy()
    {
        testObject.registerType(InterfaceBasics.class, ClassBasicsAbstract.class)
                  .registerType(ClassBasicsAbstract.class, ClassBasicsInheritsFromAbstract.class);

        InterfaceBasics result = null;

        try
        {
            result = testObject.resolve(InterfaceBasics.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof ClassBasicsInheritsFromAbstract);
    }

    // endregion
    // region registerInstance

    @Test
    public void testRegisterInstanceWhenInterface()
    {
        ClassConstructorsDefault obj = new ClassConstructorsDefault();

        testObject.registerInstance(InterfaceBasics.class, obj);

        InterfaceBasics result = null;

        try
        {
            result = testObject.resolve(InterfaceBasics.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof ClassConstructorsDefault);
        Assert.assertSame(obj, result);
    }

    @Test
    public void testRegisterInstanceWhenAbstractClass()
    {
        ClassBasicsInheritsFromAbstract obj = new ClassBasicsInheritsFromAbstract();

        testObject.registerInstance(ClassBasicsAbstract.class, obj);

        ClassBasicsAbstract result = null;

        try
        {
            result = testObject.resolve(ClassBasicsAbstract.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof ClassBasicsInheritsFromAbstract);
        Assert.assertSame(obj, result);
    }

    @Test
    public void testRegisterInstanceWhenSameConcreteClass()
    {
        ClassConstructorsDefaultAndParameter obj = new ClassConstructorsDefaultAndParameter();

        testObject.registerInstance(ClassConstructorsDefaultAndParameter.class, obj);

        ClassConstructorsDefaultAndParameter result = null;

        try
        {
            result = testObject.resolve(ClassConstructorsDefaultAndParameter.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result);
        Assert.assertSame(obj, result);
        Assert.assertEquals(obj.getText(), result.getText());
    }

    @Test
    public void testRegisterInstanceWhenDerivedConcreteClass()
    {
        ClassConstructorsInheritParameter obj = new ClassConstructorsInheritParameter();

        testObject.registerInstance(ClassConstructorsParameter.class, obj);

        ClassConstructorsParameter result = null;

        try
        {
            result = testObject.resolve(ClassConstructorsParameter.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result);
        Assert.assertSame(obj, result);
        Assert.assertTrue(result instanceof ClassConstructorsInheritParameter);
        Assert.assertEquals(obj.getNumber(), result.getNumber());
    }

    @Test(expected = NullInstanceException.class)
    public void testRegisterInstanceWhenInstanceIsNull()
        throws IllegalArgumentException
    {
        testObject.registerInstance(ClassConstructorsDefaultAndParameter.class, null);
    }

    // endregion
    //region resolve (class)

    @Test
    public void testResolveWhenClassHasDefaultConstructorOnly()
    {
        ClassConstructorsDefault result = null;

        try
        {
            result = testObject.resolve(ClassConstructorsDefault.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result);
    }

    @Test
    public void testResolveWhenClassInheritsFromConcreteClass()
    {
        ClassConstructorsInheritParameter result = null;

        try
        {
            result = testObject.resolve(ClassConstructorsInheritParameter.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result);
    }

    @Test
    public void testResolveWhenClassInheritsFromAbstractClass()
    {
        ClassBasicsInheritsFromAbstract result = null;

        try
        {
            result = testObject.resolve(ClassBasicsInheritsFromAbstract.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result);
    }

    @Test(expected = MissingDependenciesException.class)
    public void testResolveWhenClassHasParameterConstructorWithoutRegisteredParameter()
        throws DIException

    {
        testObject.resolve(ClassConstructorsParameter.class);
    }

    @Test
    public void testResolveWhenClassHasParameterConstructorWithRegisteredPrimitiveParameter()
    {
        int number = 10;

        testObject.registerInstance(int.class, number);

        ClassConstructorsParameter result = null;

        try
        {
            result = testObject.resolve(ClassConstructorsParameter.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result);
        Assert.assertEquals(number, result.getNumber());
    }

    @Test
    public void testResolveWhenClassHasParameterConstructorWithRegisteredReferenceParameter()
    {
        Integer number = 10;

        testObject.registerInstance(Integer.class, number);

        ClassConstructorsParameter result = null;

        try
        {
            result = testObject.resolve(ClassConstructorsParameter.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result);
    }

    @Test
    public void testResolveWhenClassHasDefaultAndParameterConstructorWithoutRegisteredParameter()
    {
        ClassConstructorsDefaultAndParameter result = null;
        try
        {
            result = testObject.resolve(ClassConstructorsDefaultAndParameter.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result);
    }

    @Test(expected = MissingDependenciesException.class)
    public void testResolveWhenInterface()
        throws DIException
    {
        testObject.resolve(InterfaceBasics.class);
    }

    @Test(expected = MissingDependenciesException.class)
    public void testResolveWhenAbstractClass()
        throws DIException
    {
        testObject.resolve(ClassBasicsAbstract.class);
    }

    // endregion
    // region resolve (dependencies schemas)

    @Test
    public void testResolveDependenciesWithRegisteredInstance()
    {
        String string = "String";

        testObject.registerInstance(String.class, string)
                  .registerType(InterfaceBasics.class, ClassConstructorsDefault.class)
                  .registerType(InterfaceDiamonds1.class, ClassDiamonds1.class)
                  .registerType(InterfaceDiamondsTop.class, ClassDiamondsTop.class)
                  .registerType(InterfaceBasicsStringGetter.class, ClassBasicsStringGetter.class)
                  .registerType(InterfaceBasicsSimpleDependency.class,
                                ClassConstructorsNoAnnotationDependency.class);

        InterfaceBasicsSimpleDependency result = null;

        try
        {
            result = testObject.resolve(InterfaceBasicsSimpleDependency.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getFirstObject());
        Assert.assertNotNull(result.getSecondObject());
        Assert.assertNotNull(result.getFirstObject().getObject());
        Assert.assertNotNull(result.getSecondObject().getString());
        Assert.assertEquals(string, result.getSecondObject().getString());
        Assert.assertTrue(result instanceof ClassConstructorsNoAnnotationDependency);
    }

    @Test
    public void testResolveDependenciesWithoutAnnotatedConstructorsWithAllDependencies()
    {
        testObject.registerType(InterfaceBasics.class, ClassConstructorsDefault.class)
                  .registerType(InterfaceDiamonds1.class, ClassDiamonds1.class)
                  .registerType(InterfaceDiamondsTop.class, ClassDiamondsTop.class)
                  .registerType(InterfaceBasicsStringGetter.class, ClassBasicsStringGetter.class)
                  .registerType(InterfaceBasicsSimpleDependency.class,
                                ClassConstructorsNoAnnotationDependency.class);

        InterfaceBasicsSimpleDependency result = null;

        try
        {
            result = testObject.resolve(InterfaceBasicsSimpleDependency.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getFirstObject());
        Assert.assertNotNull(result.getSecondObject());
        Assert.assertNotNull(result.getFirstObject().getObject());
        Assert.assertNotNull(result.getSecondObject().getString());
        Assert.assertEquals("", result.getSecondObject().getString());
        Assert.assertTrue(result instanceof ClassConstructorsNoAnnotationDependency);
    }

    @Test
    public void testResolveDependenciesWithoutAnnotatedConstructorsWithoutSomeDependencies()
    {
        testObject.registerType(InterfaceBasics.class, ClassConstructorsDefault.class)
                  .registerType(InterfaceDiamonds1.class, ClassDiamonds1.class)
                  .registerType(InterfaceDiamondsTop.class, ClassDiamondsTop.class)
                  .registerType(InterfaceBasicsSimpleDependency.class,
                                ClassConstructorsNoAnnotationDependency.class);

        InterfaceBasicsSimpleDependency result = null;

        try
        {
            result = testObject.resolve(InterfaceBasicsSimpleDependency.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getFirstObject());
        Assert.assertNull(result.getSecondObject());
        Assert.assertNotNull(result.getFirstObject().getObject());
        Assert.assertTrue(result instanceof ClassConstructorsNoAnnotationDependency);
    }

    @Test
    public void testResolveDependenciesWithAnnotatedConstructor()
    {
        testObject.registerType(InterfaceBasics.class, ClassConstructorsDefault.class)
                  .registerType(InterfaceDiamonds1.class, ClassDiamonds1.class)
                  .registerType(InterfaceDiamondsTop.class, ClassDiamondsTop.class)
                  .registerType(InterfaceBasicsStringGetter.class, ClassBasicsStringGetter.class)
                  .registerType(InterfaceBasicsSimpleDependency.class,
                                ClassConstructorsAnnotationDependency.class);

        InterfaceBasicsSimpleDependency result = null;

        try
        {
            result = testObject.resolve(InterfaceBasicsSimpleDependency.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getFirstObject());
        Assert.assertNotNull(result.getSecondObject());
        Assert.assertNotNull(result.getFirstObject().getObject());
        Assert.assertNotNull(result.getSecondObject().getString());
        Assert.assertEquals("", result.getSecondObject().getString());
        Assert.assertTrue(result instanceof ClassConstructorsAnnotationDependency);
    }

    @Test(expected = MissingDependenciesException.class)
    public void testResolveDependenciesWithAnnotatedConstructorWithoutSomeDependencies()
        throws DIException

    {
        testObject.registerType(InterfaceDiamonds1.class, ClassDiamonds1.class)
                  .registerType(InterfaceDiamonds2.class, ClassDiamonds2.class)
                  .registerType(InterfaceDiamondsBottom.class, ClassDiamondsBottom.class);

        testObject.resolve(InterfaceDiamondsBottom.class);
    }

    @Test
    public void testResolveDiamondDependenciesWithoutSingleton()
    {
        testObject.registerType(InterfaceDiamonds1.class, ClassDiamonds1.class)
                  .registerType(InterfaceDiamonds2.class, ClassDiamonds2.class)
                  .registerType(InterfaceDiamondsBottom.class, ClassDiamondsBottom.class)
                  .registerType(InterfaceDiamondsTop.class, ClassDiamondsTop.class);

        InterfaceDiamondsBottom result = null;

        try
        {
            result = testObject.resolve(InterfaceDiamondsBottom.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getDiamond1());
        Assert.assertNotNull(result.getDiamond2());
        Assert.assertNotNull(result.getDiamond1().getObject());
        Assert.assertNotNull(result.getDiamond2().getObject());
        Assert.assertNotSame(result.getDiamond1().getObject(), result.getDiamond2().getObject());
        Assert.assertTrue(result instanceof ClassDiamondsBottom);
    }

    @Test
    public void testResolveDiamondDependenciesWithSingleton()
    {
        testObject.registerType(InterfaceDiamonds1.class, ClassDiamonds1.class)
                  .registerType(InterfaceDiamonds2.class, ClassDiamonds2.class)
                  .registerType(InterfaceDiamondsBottom.class, ClassDiamondsBottom.class)
                  .registerType(InterfaceDiamondsTop.class, ClassDiamondsTop.class,
                                ConstructionPolicy.SINGLETON);

        InterfaceDiamondsBottom result = null;

        try
        {
            result = testObject.resolve(InterfaceDiamondsBottom.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getDiamond1());
        Assert.assertNotNull(result.getDiamond2());
        Assert.assertNotNull(result.getDiamond1().getObject());
        Assert.assertNotNull(result.getDiamond2().getObject());
        Assert.assertSame(result.getDiamond1().getObject(), result.getDiamond2().getObject());
        Assert.assertTrue(result instanceof ClassDiamondsBottom);
    }

    @Test(expected = CircularDependenciesException.class)
    public void testResolveCircularDependencies()
        throws DIException

    {
        testObject.registerType(InterfaceCirculars1.class, ClassCirculars1.class)
                  .registerType(InterfaceCirculars2.class, ClassCirculars2.class);

        testObject.resolve(InterfaceCirculars2.class);
    }

    @Test
    public void testResolveWhenCanOmitCircularDependencies()
    {
        String string = "String";

        testObject.registerInstance(String.class, string)
                  .registerType(InterfaceBasicsStringGetter.class, ClassBasicsStringGetter.class)
                  .registerType(InterfaceCirculars1.class, ClassCirculars1.class)
                  .registerType(InterfaceCirculars2.class, ClassCirculars2.class)
                  .registerType(InterfaceCircularsDependency.class, ClassCircularsDependency.class);

        InterfaceCircularsDependency result = null;

        try
        {
            result = testObject.resolve(InterfaceCircularsDependency.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getNonCircularObject());
        Assert.assertNull(result.getCircularObject());
        Assert.assertNotNull(result.getNonCircularObject().getString());
        Assert.assertEquals(string, result.getNonCircularObject().getString());
        Assert.assertTrue(result instanceof ClassCircularsDependency);
    }

    // endregion
    // region resolve (@Dependency)

    @Test(expected = MultipleAnnotatedConstructorsException.class)
    public void testResolveWhenMultipleAnnotatedConstructors()
        throws DIException
    {
        testObject.resolve(ClassConstructorsMultipleAnnotated.class);
    }

    @Test(expected = NoSuitableConstructorException.class)
    public void testResolveWhenNoPublicConstructors()
        throws DIException
    {
        testObject.resolve(ClassConstructorsPrivate.class);
    }

    @Test(expected = IncorrectDependencySetterException.class)
    public void testResolveWhenDependencySetterHasReturnType()
        throws DIException
    {
        testObject.resolve(ClassSettersIncorrect1.class);
    }

    @Test(expected = IncorrectDependencySetterException.class)
    public void testResolveWhenDependencySetterHasNoParameters()
        throws DIException
    {
        testObject.resolve(ClassSettersIncorrect2.class);
    }

    @Test(expected = IncorrectDependencySetterException.class)
    public void testResolveWhenDependencySetterNameDoesNotStartWithSet()
        throws DIException
    {
        testObject.resolve(ClassSettersIncorrect3.class);
    }

    @Test
    public void testResolveWhenDependencySetterOnly()
    {
        testObject.registerType(InterfaceSetters.class, ClassSettersSingle.class)
                  .registerType(InterfaceBasics.class, ClassConstructorsDefault.class);

        InterfaceSetters result = null;

        try
        {
            result = testObject.resolve(InterfaceSetters.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getBasicObject());
        Assert.assertTrue(result instanceof ClassSettersSingle);
    }

    @Test
    public void testResolveWhenDependencySetterAndConstructor()
    {
        testObject.registerType(InterfaceSetters.class, ClassSettersConstructor.class)
                  .registerType(InterfaceBasics.class, ClassConstructorsDefault.class);

        InterfaceSetters result = null;

        try
        {
            result = testObject.resolve(InterfaceSetters.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getBasicObject());
        Assert.assertTrue(result instanceof ClassSettersConstructor);
    }

    @Test
    public void testResolveWhenComplexDependency()
    {
        String string = "string";

        testObject.registerType(InterfaceBasicsComplexDependency.class,
                                ClassBasicsComplexDependency.class)
                  .registerType(InterfaceBasics.class, ClassConstructorsDefault.class)
                  .registerType(InterfaceDiamondsTop.class, ClassDiamondsTop.class)
                  .registerType(InterfaceDiamonds1.class, ClassDiamonds1.class)
                  .registerType(InterfaceBasicsStringGetter.class, ClassBasicsStringGetter.class);

        testObject.registerInstance(String.class, string);

        InterfaceBasicsComplexDependency result = null;

        try
        {
            result = testObject.resolve(InterfaceBasicsComplexDependency.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getBasicObject());
        Assert.assertNotNull(result.getFirstObject());
        Assert.assertNotNull(result.getSecondObject());
        Assert.assertNotNull(result.getFirstObject().getObject());
        Assert.assertNotNull(result.getSecondObject().getString());
        Assert.assertEquals(string, result.getSecondObject().getString());
        Assert.assertTrue(result instanceof ClassBasicsComplexDependency);
    }

    @Test(expected = IncorrectDependencySetterException.class)
    public void testResolveWhenDoubleDependencySetter()
        throws DIException
    {
        String string = "string";

        testObject.registerType(InterfaceSettersDouble.class, ClassSettersDouble.class);

        testObject.resolve(InterfaceSettersDouble.class);
    }

    @Test
    public void testResolveWhenMultipleDependencySetters()
    {
        String string = "string";

        testObject.registerType(InterfaceSettersMultiple.class, ClassSettersMultiple.class)
                  .registerType(InterfaceBasics.class, ClassConstructorsDefault.class)
                  .registerType(InterfaceBasicsStringGetter.class, ClassBasicsStringGetter.class);

        testObject.registerInstance(String.class, string);

        InterfaceSettersMultiple result = null;

        try
        {
            result = testObject.resolve(InterfaceSettersMultiple.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getBasicObject());
        Assert.assertNotNull(result.getStringObject());
        Assert.assertNotNull(result.getStringObject().getString());
        Assert.assertEquals(string, result.getStringObject().getString());
        Assert.assertTrue(result instanceof ClassSettersMultiple);
    }

    // endregion
    // region resolve (@Register and @SelfRegister)

    @Test
    public void testResolveWhenAnnotatedInterface()
    {
        InterfaceRegister result1 = null;
        InterfaceRegister result2 = null;

        try
        {
            result1 = testObject.resolve(InterfaceRegister.class);
            result2 = testObject.resolve(InterfaceRegister.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail(
                String.format("An instance of %s was thrown.", e.getClass().getSimpleName()));
        }

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertTrue(result1 instanceof ClassRegisterInterface);
        Assert.assertTrue(result2 instanceof ClassRegisterInterface);
        Assert.assertNotSame(result1, result2);
    }

    @Test
    public void testResolveWhenAnnotatedAbstractClass()
    {
        ClassRegisterAbstract result1 = null;
        ClassRegisterAbstract result2 = null;

        try
        {
            result1 = testObject.resolve(ClassRegisterAbstract.class);
            result2 = testObject.resolve(ClassRegisterAbstract.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail(
                String.format("An instance of %s was thrown.", e.getClass().getSimpleName()));
        }

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertTrue(result2 instanceof ClassRegisterBase);
        Assert.assertTrue(result1 instanceof ClassRegisterBase);
        Assert.assertNotSame(result1, result2);
    }

    @Test
    public void testResolveWhenAnnotatedConcreteClass()
    {
        ClassRegisterBase result1 = null;
        ClassRegisterBase result2 = null;

        try
        {
            result1 = testObject.resolve(ClassRegisterBase.class);
            result2 = testObject.resolve(ClassRegisterBase.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail(
                String.format("An instance of %s was thrown.", e.getClass().getSimpleName()));
        }

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertTrue(result1 instanceof ClassRegisterDerived);
        Assert.assertTrue(result2 instanceof ClassRegisterDerived);
        Assert.assertNotSame(result1, result2);
    }

    @Test
    public void testResolveWhenSelfAnnotatedConcreteClass()
    {
        ClassRegisterSelf1 result1 = null;
        ClassRegisterSelf1 result2 = null;

        try
        {
            result1 = testObject.resolve(ClassRegisterSelf1.class);
            result2 = testObject.resolve(ClassRegisterSelf1.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail(
                String.format("An instance of %s was thrown.", e.getClass().getSimpleName()));
        }

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertNotSame(result1, result2);
    }

    @Test
    public void testResolveWhenAnnotatedConcreteClassAsItself()
    {
        ClassRegisterSelf2 result1 = null;
        ClassRegisterSelf2 result2 = null;

        try
        {
            result1 = testObject.resolve(ClassRegisterSelf2.class);
            result2 = testObject.resolve(ClassRegisterSelf2.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail(
                String.format("An instance of %s was thrown.", e.getClass().getSimpleName()));
        }

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertNotSame(result1, result2);
    }

    @Test(expected = AbstractTypeException.class)
    public void testResolveWhenSelfAnnotatedInterface()
        throws DIException
    {
        testObject.resolve(InterfaceRegisterSelfIncorrect.class);
    }

    @Test(expected = NotDerivedTypeException.class)
    public void testResolveWhenInterfaceAnnotatedClassNotImplementing()
        throws DIException
    {
        testObject.resolve(InterfaceRegisterIncorrect.class);
    }

    @Test(expected = NotDerivedTypeException.class)
    public void testResolveWhenAnnotatedClassRegistersInterface()
        throws DIException
    {
        testObject.resolve(ClassRegisterInterfaceIncorrect.class);
    }

    @Test(expected = NotDerivedTypeException.class)
    public void testResolveWhenAnnotatedClassRegistersNotDerivedClass()
        throws DIException
    {
        testObject.resolve(ClassRegisterIncorrect1.class);
    }

    @Test(expected = NotDerivedTypeException.class)
    public void testResolveWhenAnnotatedClassRegistersAbstractSuperclass()
        throws DIException
    {
        testObject.resolve(ClassRegisterIncorrect2.class);
    }

    @Test(expected = NotDerivedTypeException.class)
    public void testResolveWhenAnnotatedClassRegistersAbstractConcreteSuperclass()
        throws DIException
    {
        testObject.resolve(ClassRegisterIncorrect3.class);
    }

    @Test(expected = AbstractTypeException.class)
    public void testResolveWhenSelfAnnotatedAbstractClass()
        throws DIException
    {
        testObject.resolve(ClassRegisterSelfAbstractIncorrect.class);
    }

    @Test(expected = AbstractTypeException.class)
    public void testResolveWhenAnnotatedAbstractClassAsItself()
        throws DIException
    {
        testObject.resolve(ClassRegisterAbstractIncorrect.class);
    }

    @Test
    public void testResolveWhenAnnotatedInterfaceSingleton()
    {
        InterfaceRegisterSingleton result1 = null;
        InterfaceRegisterSingleton result2 = null;

        try
        {
            result1 = testObject.resolve(InterfaceRegisterSingleton.class);
            result2 = testObject.resolve(InterfaceRegisterSingleton.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail(
                String.format("An instance of %s was thrown.", e.getClass().getSimpleName()));
        }

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertTrue(result1 instanceof ClassRegisterSingletonBase);
        Assert.assertTrue(result2 instanceof ClassRegisterSingletonBase);
        Assert.assertSame(result1, result2);
    }

    @Test
    public void testResolveWhenAnnotatedConcreteClassSingleton()
    {
        ClassRegisterSingletonBase result1 = null;
        ClassRegisterSingletonBase result2 = null;

        try
        {
            result1 = testObject.resolve(ClassRegisterSingletonBase.class);
            result2 = testObject.resolve(ClassRegisterSingletonBase.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail(
                String.format("An instance of %s was thrown.", e.getClass().getSimpleName()));
        }

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertTrue(result1 instanceof ClassRegisterSingletonDerived);
        Assert.assertTrue(result2 instanceof ClassRegisterSingletonDerived);
        Assert.assertSame(result1, result2);
    }

    @Test
    public void testResolveWhenSelfAnnotatedConcreteClassSingleton()
    {
        ClassRegisterSelfSingleton1 result1 = null;
        ClassRegisterSelfSingleton1 result2 = null;

        try
        {
            result1 = testObject.resolve(ClassRegisterSelfSingleton1.class);
            result2 = testObject.resolve(ClassRegisterSelfSingleton1.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail(
                String.format("An instance of %s was thrown.", e.getClass().getSimpleName()));
        }

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertSame(result1, result2);
    }

    @Test
    public void testResolveWhenAnnotatedConcreteClassAsItselfSingleton()
    {
        ClassRegisterSelfSingleton2 result1 = null;
        ClassRegisterSelfSingleton2 result2 = null;

        try
        {
            result1 = testObject.resolve(ClassRegisterSelfSingleton2.class);
            result2 = testObject.resolve(ClassRegisterSelfSingleton2.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail(
                String.format("An instance of %s was thrown.", e.getClass().getSimpleName()));
        }

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertSame(result1, result2);
    }

    // endregion
    // region buildUp

    @Test
    public void testBuildUpWhenDependencySetterOnly()
    {
        testObject.registerType(InterfaceBasics.class, ClassConstructorsDefault.class);

        InterfaceSetters instance = new ClassSettersSingle();
        InterfaceSetters result = null;

        try
        {
            result = testObject.buildUp(instance);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result);
        Assert.assertNotNull(instance);
        Assert.assertNotNull(result.getBasicObject());
        Assert.assertNotNull(instance.getBasicObject());
        Assert.assertSame(instance, result);
    }

    @Test(expected = IncorrectDependencySetterException.class)
    public void testBuildUpWhenDoubleDependencySetter()
        throws DIException
    {
        InterfaceSettersDouble instance = new ClassSettersDouble();

        testObject.buildUp(instance);
    }

    @Test
    public void testBuildUpWhenMultipleDependencySetters()
    {
        String string = "string";

        testObject.registerType(InterfaceBasics.class, ClassConstructorsDefault.class)
                  .registerType(InterfaceBasicsStringGetter.class, ClassBasicsStringGetter.class)
                  .registerInstance(String.class, string);

        InterfaceSettersMultiple instance = new ClassSettersMultiple();
        InterfaceSettersMultiple result = new ClassSettersMultiple();

        try
        {
            result = testObject.buildUp(instance);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(result);
        Assert.assertNotNull(instance);
        Assert.assertNotNull(result.getBasicObject());
        Assert.assertNotNull(instance.getBasicObject());
        Assert.assertNotNull(result.getStringObject());
        Assert.assertNotNull(instance.getStringObject());
        Assert.assertNotNull(result.getStringObject().getString());
        Assert.assertNotNull(instance.getStringObject().getString());
        Assert.assertEquals(string, result.getStringObject().getString());
        Assert.assertEquals(string, instance.getStringObject().getString());
        Assert.assertSame(instance, result);
    }

    @Test
    public void testBuildUpWhenComplexDependency()
    {
        String string = "string";

        testObject.registerType(InterfaceBasics.class, ClassConstructorsDefault.class)
                  .registerType(InterfaceDiamonds1.class, ClassDiamonds1.class)
                  .registerType(InterfaceDiamondsTop.class, ClassDiamondsTop.class)
                  .registerType(InterfaceBasicsStringGetter.class, ClassBasicsStringGetter.class)
                  .registerInstance(String.class, string);

        InterfaceBasicsComplexDependency instance = null;
        InterfaceBasicsComplexDependency result = null;

        try
        {
            InterfaceDiamonds1 diamond1 = testObject.resolve(InterfaceDiamonds1.class);
            InterfaceBasicsStringGetter withString = testObject.resolve(
                InterfaceBasicsStringGetter.class);

            instance = new ClassBasicsComplexDependency(diamond1, withString);
            result = testObject.buildUp(instance);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
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
        Assert.assertEquals(string, result.getSecondObject().getString());
        Assert.assertEquals(string, instance.getSecondObject().getString());
        Assert.assertSame(instance, result);
    }

    // endregion
}
