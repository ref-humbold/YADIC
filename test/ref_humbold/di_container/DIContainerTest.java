package ref_humbold.di_container;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ref_humbold.di_container.auxiliary.basics.*;
import ref_humbold.di_container.auxiliary.circulars.*;
import ref_humbold.di_container.auxiliary.constructors.*;
import ref_humbold.di_container.auxiliary.diamonds.*;
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

        ClassConstructorsDefault cls1 = null;
        ClassConstructorsDefault cls2 = null;

        try
        {
            cls1 = testObject.resolve(ClassConstructorsDefault.class);
            cls2 = testObject.resolve(ClassConstructorsDefault.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls1);
        Assert.assertNotNull(cls2);
        Assert.assertNotSame(cls1, cls2);
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

        ClassConstructorsDefault cls1 = null;
        ClassConstructorsDefault cls2 = null;

        try
        {
            cls1 = testObject.resolve(ClassConstructorsDefault.class);
            cls2 = testObject.resolve(ClassConstructorsDefault.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls1);
        Assert.assertNotNull(cls2);
        Assert.assertSame(cls1, cls2);
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

        ClassConstructorsDefault cls11 = null;
        ClassConstructorsDefault cls12 = null;

        try
        {
            cls11 = testObject.resolve(ClassConstructorsDefault.class);
            cls12 = testObject.resolve(ClassConstructorsDefault.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls11);
        Assert.assertNotNull(cls12);
        Assert.assertSame(cls11, cls12);

        try
        {
            testObject.registerType(ClassConstructorsDefault.class, ConstructionPolicy.CONSTRUCT);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        ClassConstructorsDefault cls21 = null;
        ClassConstructorsDefault cls22 = null;

        try
        {
            cls21 = testObject.resolve(ClassConstructorsDefault.class);
            cls22 = testObject.resolve(ClassConstructorsDefault.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls21);
        Assert.assertNotNull(cls22);
        Assert.assertNotSame(cls21, cls22);
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

        InterfaceBasics cls1 = null;
        InterfaceBasics cls2 = null;

        try
        {
            cls1 = testObject.resolve(InterfaceBasics.class);
            cls2 = testObject.resolve(InterfaceBasics.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls1);
        Assert.assertNotNull(cls2);
        Assert.assertNotSame(cls1, cls2);
        Assert.assertTrue(cls1 instanceof ClassConstructorsDefault);
        Assert.assertTrue(cls2 instanceof ClassConstructorsDefault);
    }

    @Test
    public void testRegisterWhenInheritanceFromInterfaceAsSingleton()
    {
        testObject.registerType(InterfaceBasics.class, ClassConstructorsDefault.class,
                                ConstructionPolicy.SINGLETON);

        InterfaceBasics cls1 = null;
        InterfaceBasics cls2 = null;

        try
        {
            cls1 = testObject.resolve(InterfaceBasics.class);
            cls2 = testObject.resolve(InterfaceBasics.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls1);
        Assert.assertNotNull(cls2);
        Assert.assertSame(cls1, cls2);
        Assert.assertTrue(cls1 instanceof ClassConstructorsDefault);
        Assert.assertTrue(cls2 instanceof ClassConstructorsDefault);
    }

    @Test
    public void testRegisterWhenInheritanceFromInterfaceChangesSingleton()
    {
        testObject.registerType(InterfaceBasics.class, ClassConstructorsDefault.class,
                                ConstructionPolicy.SINGLETON);

        InterfaceBasics cls11 = null;
        InterfaceBasics cls12 = null;

        try
        {
            cls11 = testObject.resolve(InterfaceBasics.class);
            cls12 = testObject.resolve(InterfaceBasics.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls11);
        Assert.assertNotNull(cls12);
        Assert.assertSame(cls11, cls12);
        Assert.assertTrue(cls11 instanceof ClassConstructorsDefault);
        Assert.assertTrue(cls12 instanceof ClassConstructorsDefault);

        testObject.registerType(InterfaceBasics.class, ClassConstructorsDefault.class,
                                ConstructionPolicy.CONSTRUCT);

        InterfaceBasics cls21 = null;
        InterfaceBasics cls22 = null;

        try
        {
            cls21 = testObject.resolve(InterfaceBasics.class);
            cls22 = testObject.resolve(InterfaceBasics.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls21);
        Assert.assertNotNull(cls22);
        Assert.assertNotSame(cls21, cls22);
        Assert.assertTrue(cls21 instanceof ClassConstructorsDefault);
        Assert.assertTrue(cls22 instanceof ClassConstructorsDefault);
    }

    @Test
    public void testRegisterWhenInheritanceFromInterfaceChangesClass()
    {
        testObject.registerType(InterfaceBasics.class, ClassConstructorsDefault.class);

        InterfaceBasics cls1 = null;

        try
        {
            cls1 = testObject.resolve(InterfaceBasics.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls1);
        Assert.assertTrue(cls1 instanceof ClassConstructorsDefault);

        testObject.registerType(InterfaceBasics.class, ClassConstructorsDefaultAndParameter.class);

        InterfaceBasics cls3 = null;

        try
        {
            cls3 = testObject.resolve(InterfaceBasics.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls3);
        Assert.assertTrue(cls3 instanceof ClassConstructorsDefaultAndParameter);
    }

    @Test
    public void testRegisterWhenInheritanceFromAbstractClass()
    {
        testObject.registerType(ClassBasicsAbstract.class, ClassBasicsInheritsFromAbstract.class);

        ClassBasicsAbstract cls = null;

        try
        {
            cls = testObject.resolve(ClassBasicsAbstract.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
        Assert.assertTrue(cls instanceof ClassBasicsInheritsFromAbstract);
    }

    @Test
    public void testRegisterWhenInheritanceFromConcreteClass()
    {
        testObject.registerType(ClassConstructorsParameter.class,
                                ClassConstructorsInheritParameter.class);

        ClassConstructorsParameter cls = null;

        try
        {
            cls = testObject.resolve(ClassConstructorsParameter.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
        Assert.assertTrue(cls instanceof ClassConstructorsInheritParameter);
    }

    @Test
    public void testRegisterWhenTwoStepsOfHierarchy()
    {
        testObject.registerType(InterfaceBasics.class, ClassBasicsAbstract.class)
                  .registerType(ClassBasicsAbstract.class, ClassBasicsInheritsFromAbstract.class);

        InterfaceBasics cls = null;

        try
        {
            cls = testObject.resolve(InterfaceBasics.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
        Assert.assertTrue(cls instanceof ClassBasicsInheritsFromAbstract);
    }

    // endregion
    // region registerInstance

    @Test
    public void testRegisterInstanceWhenInterface()
    {
        ClassConstructorsDefault obj = new ClassConstructorsDefault();

        testObject.registerInstance(InterfaceBasics.class, obj);

        InterfaceBasics cls = null;

        try
        {
            cls = testObject.resolve(InterfaceBasics.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
        Assert.assertTrue(cls instanceof ClassConstructorsDefault);
        Assert.assertSame(obj, cls);
    }

    @Test
    public void testRegisterInstanceWhenAbstractClass()
    {
        ClassBasicsInheritsFromAbstract obj = new ClassBasicsInheritsFromAbstract();

        testObject.registerInstance(ClassBasicsAbstract.class, obj);

        ClassBasicsAbstract cls = null;

        try
        {
            cls = testObject.resolve(ClassBasicsAbstract.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
        Assert.assertTrue(cls instanceof ClassBasicsInheritsFromAbstract);
        Assert.assertSame(obj, cls);
    }

    @Test
    public void testRegisterInstanceWhenSameConcreteClass()
    {
        ClassConstructorsDefaultAndParameter obj = new ClassConstructorsDefaultAndParameter();

        testObject.registerInstance(ClassConstructorsDefaultAndParameter.class, obj);

        ClassConstructorsDefaultAndParameter cls = null;

        try
        {
            cls = testObject.resolve(ClassConstructorsDefaultAndParameter.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
        Assert.assertSame(obj, cls);
        Assert.assertEquals(obj.getText(), cls.getText());
    }

    @Test
    public void testRegisterInstanceWhenDerivedConcreteClass()
    {
        ClassConstructorsInheritParameter obj = new ClassConstructorsInheritParameter();

        testObject.registerInstance(ClassConstructorsParameter.class, obj);

        ClassConstructorsParameter cls = null;

        try
        {
            cls = testObject.resolve(ClassConstructorsParameter.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
        Assert.assertSame(obj, cls);
        Assert.assertTrue(cls instanceof ClassConstructorsInheritParameter);
        Assert.assertEquals(obj.getNumber(), cls.getNumber());
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
        ClassConstructorsDefault cls = null;

        try
        {
            cls = testObject.resolve(ClassConstructorsDefault.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
    }

    @Test
    public void testResolveWhenClassInheritsFromConcreteClass()
    {
        ClassConstructorsInheritParameter cls = null;

        try
        {
            cls = testObject.resolve(ClassConstructorsInheritParameter.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
    }

    @Test
    public void testResolveWhenClassInheritsFromAbstractClass()
    {
        ClassBasicsInheritsFromAbstract cls = null;

        try
        {
            cls = testObject.resolve(ClassBasicsInheritsFromAbstract.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
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

        ClassConstructorsParameter cls = null;

        try
        {
            cls = testObject.resolve(ClassConstructorsParameter.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
        Assert.assertEquals(number, cls.getNumber());
    }

    @Test
    public void testResolveWhenClassHasParameterConstructorWithRegisteredReferenceParameter()
    {
        Integer number = 10;

        testObject.registerInstance(Integer.class, number);

        ClassConstructorsParameter cls = null;

        try
        {
            cls = testObject.resolve(ClassConstructorsParameter.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
    }

    @Test
    public void testResolveWhenClassHasDefaultAndParameterConstructorWithoutRegisteredParameter()
    {
        ClassConstructorsDefaultAndParameter cls = null;
        try
        {
            cls = testObject.resolve(ClassConstructorsDefaultAndParameter.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
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
    // region resolve (constructors)

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

    // endregion
    // region resolve (dependencies)

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

        InterfaceBasicsSimpleDependency cls = null;

        try
        {
            cls = testObject.resolve(InterfaceBasicsSimpleDependency.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
        Assert.assertNotNull(cls.getFirstObject());
        Assert.assertNotNull(cls.getSecondObject());
        Assert.assertNotNull(cls.getFirstObject().getObject());
        Assert.assertNotNull(cls.getSecondObject().getString());
        Assert.assertEquals(string, cls.getSecondObject().getString());
        Assert.assertTrue(cls instanceof ClassConstructorsNoAnnotationDependency);
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

        InterfaceBasicsSimpleDependency cls = null;

        try
        {
            cls = testObject.resolve(InterfaceBasicsSimpleDependency.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
        Assert.assertNotNull(cls.getFirstObject());
        Assert.assertNotNull(cls.getSecondObject());
        Assert.assertNotNull(cls.getFirstObject().getObject());
        Assert.assertNotNull(cls.getSecondObject().getString());
        Assert.assertEquals("", cls.getSecondObject().getString());
        Assert.assertTrue(cls instanceof ClassConstructorsNoAnnotationDependency);
    }

    @Test
    public void testResolveDependenciesWithoutAnnotatedConstructorsWithoutSomeDependencies()
    {
        testObject.registerType(InterfaceBasics.class, ClassConstructorsDefault.class)
                  .registerType(InterfaceDiamonds1.class, ClassDiamonds1.class)
                  .registerType(InterfaceDiamondsTop.class, ClassDiamondsTop.class)
                  .registerType(InterfaceBasicsSimpleDependency.class,
                                ClassConstructorsNoAnnotationDependency.class);

        InterfaceBasicsSimpleDependency cls = null;

        try
        {
            cls = testObject.resolve(InterfaceBasicsSimpleDependency.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
        Assert.assertNotNull(cls.getFirstObject());
        Assert.assertNull(cls.getSecondObject());
        Assert.assertNotNull(cls.getFirstObject().getObject());
        Assert.assertTrue(cls instanceof ClassConstructorsNoAnnotationDependency);
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

        InterfaceBasicsSimpleDependency cls = null;

        try
        {
            cls = testObject.resolve(InterfaceBasicsSimpleDependency.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
        Assert.assertNotNull(cls.getFirstObject());
        Assert.assertNotNull(cls.getSecondObject());
        Assert.assertNotNull(cls.getFirstObject().getObject());
        Assert.assertNotNull(cls.getSecondObject().getString());
        Assert.assertEquals("", cls.getSecondObject().getString());
        Assert.assertTrue(cls instanceof ClassConstructorsAnnotationDependency);
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

        InterfaceDiamondsBottom cls = null;

        try
        {
            cls = testObject.resolve(InterfaceDiamondsBottom.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
        Assert.assertNotNull(cls.getDiamond1());
        Assert.assertNotNull(cls.getDiamond2());
        Assert.assertNotNull(cls.getDiamond1().getObject());
        Assert.assertNotNull(cls.getDiamond2().getObject());
        Assert.assertNotSame(cls.getDiamond1().getObject(), cls.getDiamond2().getObject());
        Assert.assertTrue(cls instanceof ClassDiamondsBottom);
    }

    @Test
    public void testResolveDiamondDependenciesWithSingleton()
    {
        testObject.registerType(InterfaceDiamonds1.class, ClassDiamonds1.class)
                  .registerType(InterfaceDiamonds2.class, ClassDiamonds2.class)
                  .registerType(InterfaceDiamondsBottom.class, ClassDiamondsBottom.class)
                  .registerType(InterfaceDiamondsTop.class, ClassDiamondsTop.class,
                                ConstructionPolicy.SINGLETON);

        InterfaceDiamondsBottom cls = null;

        try
        {
            cls = testObject.resolve(InterfaceDiamondsBottom.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
        Assert.assertNotNull(cls.getDiamond1());
        Assert.assertNotNull(cls.getDiamond2());
        Assert.assertNotNull(cls.getDiamond1().getObject());
        Assert.assertNotNull(cls.getDiamond2().getObject());
        Assert.assertSame(cls.getDiamond1().getObject(), cls.getDiamond2().getObject());
        Assert.assertTrue(cls instanceof ClassDiamondsBottom);
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

        InterfaceCircularsDependency cls = null;

        try
        {
            cls = testObject.resolve(InterfaceCircularsDependency.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
        Assert.assertNotNull(cls.getNonCircularObject());
        Assert.assertNull(cls.getCircularObject());
        Assert.assertNotNull(cls.getNonCircularObject().getString());
        Assert.assertEquals(string, cls.getNonCircularObject().getString());
        Assert.assertTrue(cls instanceof ClassCircularsDependency);
    }

    // endregion
    // region resolve (annotations)

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

        InterfaceSetters cls = null;

        try
        {
            cls = testObject.resolve(InterfaceSetters.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
        Assert.assertNotNull(cls.getBasicObject());
        Assert.assertTrue(cls instanceof ClassSettersSingle);
    }

    @Test
    public void testResolveWhenDependencySetterAndConstructor()
    {
        testObject.registerType(InterfaceSetters.class, ClassSettersConstructor.class)
                  .registerType(InterfaceBasics.class, ClassConstructorsDefault.class);

        InterfaceSetters cls = null;

        try
        {
            cls = testObject.resolve(InterfaceSetters.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
        Assert.assertNotNull(cls.getBasicObject());
        Assert.assertTrue(cls instanceof ClassSettersConstructor);
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

        InterfaceBasicsComplexDependency cls = null;

        try
        {
            cls = testObject.resolve(InterfaceBasicsComplexDependency.class);
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
        Assert.assertEquals(string, cls.getSecondObject().getString());
        Assert.assertTrue(cls instanceof ClassBasicsComplexDependency);
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

        InterfaceSettersMultiple cls = null;

        try
        {
            cls = testObject.resolve(InterfaceSettersMultiple.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
        Assert.assertNotNull(cls.getBasicObject());
        Assert.assertNotNull(cls.getStringObject());
        Assert.assertNotNull(cls.getStringObject().getString());
        Assert.assertEquals(string, cls.getStringObject().getString());
        Assert.assertTrue(cls instanceof ClassSettersMultiple);
    }

    // endregion
    // region buildUp

    @Test
    public void testBuildUpWhenDependencySetterOnly()
    {
        testObject.registerType(InterfaceBasics.class, ClassConstructorsDefault.class);

        InterfaceSetters cls = new ClassSettersSingle();

        try
        {
            testObject.buildUp(cls);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
        Assert.assertNotNull(cls.getBasicObject());
    }

    @Test(expected = IncorrectDependencySetterException.class)
    public void testBuildUpWhenDoubleDependencySetter()
        throws DIException
    {
        InterfaceSettersDouble cls = new ClassSettersDouble();

        testObject.buildUp(cls);
    }

    @Test
    public void testBuildUpWhenMultipleDependencySetters()
    {
        String string = "string";

        testObject.registerType(InterfaceBasics.class, ClassConstructorsDefault.class)
                  .registerType(InterfaceBasicsStringGetter.class, ClassBasicsStringGetter.class)
                  .registerInstance(String.class, string);

        InterfaceSettersMultiple cls = new ClassSettersMultiple();

        try
        {
            testObject.buildUp(cls);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
        Assert.assertNotNull(cls.getBasicObject());
        Assert.assertNotNull(cls.getStringObject());
        Assert.assertNotNull(cls.getStringObject().getString());
        Assert.assertEquals(string, cls.getStringObject().getString());
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

        InterfaceBasicsComplexDependency cls = null;

        try
        {
            InterfaceDiamonds1 diamond1 = testObject.resolve(InterfaceDiamonds1.class);
            InterfaceBasicsStringGetter withString = testObject.resolve(
                InterfaceBasicsStringGetter.class);

            cls = new ClassBasicsComplexDependency(diamond1, withString);
            testObject.buildUp(cls);
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
        Assert.assertEquals(string, cls.getSecondObject().getString());
    }

    // endregion
}
