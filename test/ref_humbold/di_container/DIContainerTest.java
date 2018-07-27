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
            testObject.registerType(TestClassWithDefaultConstructorOnly.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        TestClassWithDefaultConstructorOnly cls1 = null;
        TestClassWithDefaultConstructorOnly cls2 = null;

        try
        {
            cls1 = testObject.resolve(TestClassWithDefaultConstructorOnly.class);
            cls2 = testObject.resolve(TestClassWithDefaultConstructorOnly.class);
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
            testObject.registerType(TestClassWithDefaultConstructorOnly.class,
                                    ConstructionPolicy.SINGLETON);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        TestClassWithDefaultConstructorOnly cls1 = null;
        TestClassWithDefaultConstructorOnly cls2 = null;

        try
        {
            cls1 = testObject.resolve(TestClassWithDefaultConstructorOnly.class);
            cls2 = testObject.resolve(TestClassWithDefaultConstructorOnly.class);
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
            testObject.registerType(TestClassWithDefaultConstructorOnly.class,
                                    ConstructionPolicy.SINGLETON);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        TestClassWithDefaultConstructorOnly cls11 = null;
        TestClassWithDefaultConstructorOnly cls12 = null;

        try
        {
            cls11 = testObject.resolve(TestClassWithDefaultConstructorOnly.class);
            cls12 = testObject.resolve(TestClassWithDefaultConstructorOnly.class);
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
            testObject.registerType(TestClassWithDefaultConstructorOnly.class,
                                    ConstructionPolicy.CONSTRUCT);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        TestClassWithDefaultConstructorOnly cls21 = null;
        TestClassWithDefaultConstructorOnly cls22 = null;

        try
        {
            cls21 = testObject.resolve(TestClassWithDefaultConstructorOnly.class);
            cls22 = testObject.resolve(TestClassWithDefaultConstructorOnly.class);
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
        testObject.registerType(TestInterfaceBasic.class);
    }

    @Test(expected = AbstractTypeException.class)
    public void testRegisterWhenSingleClassIsAbstractClass()
        throws AbstractTypeException
    {
        testObject.registerType(TestClassAbstract.class);
    }

    // endregion
    // region registerType (inheritance)

    @Test
    public void testRegisterWhenInheritanceFromInterface()
    {
        testObject.registerType(TestInterfaceBasic.class,
                                TestClassWithDefaultConstructorOnly.class);

        TestInterfaceBasic cls1 = null;
        TestInterfaceBasic cls2 = null;

        try
        {
            cls1 = testObject.resolve(TestInterfaceBasic.class);
            cls2 = testObject.resolve(TestInterfaceBasic.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls1);
        Assert.assertNotNull(cls2);
        Assert.assertNotSame(cls1, cls2);
        Assert.assertTrue(cls1 instanceof TestClassWithDefaultConstructorOnly);
        Assert.assertTrue(cls2 instanceof TestClassWithDefaultConstructorOnly);
    }

    @Test
    public void testRegisterWhenInheritanceFromInterfaceAsSingleton()
    {
        testObject.registerType(TestInterfaceBasic.class, TestClassWithDefaultConstructorOnly.class,
                                ConstructionPolicy.SINGLETON);

        TestInterfaceBasic cls1 = null;
        TestInterfaceBasic cls2 = null;

        try
        {
            cls1 = testObject.resolve(TestInterfaceBasic.class);
            cls2 = testObject.resolve(TestInterfaceBasic.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls1);
        Assert.assertNotNull(cls2);
        Assert.assertSame(cls1, cls2);
        Assert.assertTrue(cls1 instanceof TestClassWithDefaultConstructorOnly);
        Assert.assertTrue(cls2 instanceof TestClassWithDefaultConstructorOnly);
    }

    @Test
    public void testRegisterWhenInheritanceFromInterfaceChangesSingleton()
    {
        testObject.registerType(TestInterfaceBasic.class, TestClassWithDefaultConstructorOnly.class,
                                ConstructionPolicy.SINGLETON);

        TestInterfaceBasic cls11 = null;
        TestInterfaceBasic cls12 = null;

        try
        {
            cls11 = testObject.resolve(TestInterfaceBasic.class);
            cls12 = testObject.resolve(TestInterfaceBasic.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls11);
        Assert.assertNotNull(cls12);
        Assert.assertSame(cls11, cls12);
        Assert.assertTrue(cls11 instanceof TestClassWithDefaultConstructorOnly);
        Assert.assertTrue(cls12 instanceof TestClassWithDefaultConstructorOnly);

        testObject.registerType(TestInterfaceBasic.class, TestClassWithDefaultConstructorOnly.class,
                                ConstructionPolicy.CONSTRUCT);

        TestInterfaceBasic cls21 = null;
        TestInterfaceBasic cls22 = null;

        try
        {
            cls21 = testObject.resolve(TestInterfaceBasic.class);
            cls22 = testObject.resolve(TestInterfaceBasic.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls21);
        Assert.assertNotNull(cls22);
        Assert.assertNotSame(cls21, cls22);
        Assert.assertTrue(cls21 instanceof TestClassWithDefaultConstructorOnly);
        Assert.assertTrue(cls22 instanceof TestClassWithDefaultConstructorOnly);
    }

    @Test
    public void testRegisterWhenInheritanceFromInterfaceChangesClass()
    {
        testObject.registerType(TestInterfaceBasic.class,
                                TestClassWithDefaultConstructorOnly.class);

        TestInterfaceBasic cls1 = null;

        try
        {
            cls1 = testObject.resolve(TestInterfaceBasic.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls1);
        Assert.assertTrue(cls1 instanceof TestClassWithDefaultConstructorOnly);

        testObject.registerType(TestInterfaceBasic.class,
                                TestClassWithDefaultAndParameterConstructor.class);

        TestInterfaceBasic cls3 = null;

        try
        {
            cls3 = testObject.resolve(TestInterfaceBasic.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls3);
        Assert.assertTrue(cls3 instanceof TestClassWithDefaultAndParameterConstructor);
    }

    @Test
    public void testRegisterWhenInheritanceFromAbstractClass()
    {
        testObject.registerType(TestClassAbstract.class, TestClassInheritsFromAbstractClass.class);

        TestClassAbstract cls = null;

        try
        {
            cls = testObject.resolve(TestClassAbstract.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
        Assert.assertTrue(cls instanceof TestClassInheritsFromAbstractClass);
    }

    @Test
    public void testRegisterWhenInheritanceFromConcreteClass()
    {
        testObject.registerType(TestClassWithParameterConstructorOnly.class,
                                TestClassInheritsFromClassWithParameterConstructorOnly.class);

        TestClassWithParameterConstructorOnly cls = null;

        try
        {
            cls = testObject.resolve(TestClassWithParameterConstructorOnly.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
        Assert.assertTrue(cls instanceof TestClassInheritsFromClassWithParameterConstructorOnly);
    }

    @Test
    public void testRegisterWhenTwoStepsOfHierarchy()
    {
        testObject.registerType(TestInterfaceBasic.class, TestClassAbstract.class)
                  .registerType(TestClassAbstract.class, TestClassInheritsFromAbstractClass.class);

        TestInterfaceBasic cls = null;

        try
        {
            cls = testObject.resolve(TestInterfaceBasic.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
        Assert.assertTrue(cls instanceof TestClassInheritsFromAbstractClass);
    }

    // endregion
    // region registerInstance

    @Test
    public void testRegisterInstanceWhenInterface()
    {
        TestClassWithDefaultConstructorOnly obj = new TestClassWithDefaultConstructorOnly();

        testObject.registerInstance(TestInterfaceBasic.class, obj);

        TestInterfaceBasic cls = null;

        try
        {
            cls = testObject.resolve(TestInterfaceBasic.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
        Assert.assertTrue(cls instanceof TestClassWithDefaultConstructorOnly);
        Assert.assertSame(obj, cls);
    }

    @Test
    public void testRegisterInstanceWhenAbstractClass()
    {
        TestClassInheritsFromAbstractClass obj = new TestClassInheritsFromAbstractClass();

        testObject.registerInstance(TestClassAbstract.class, obj);

        TestClassAbstract cls = null;

        try
        {
            cls = testObject.resolve(TestClassAbstract.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
        Assert.assertTrue(cls instanceof TestClassInheritsFromAbstractClass);
        Assert.assertSame(obj, cls);
    }

    @Test
    public void testRegisterInstanceWhenSameConcreteClass()
    {
        TestClassWithDefaultAndParameterConstructor obj = new TestClassWithDefaultAndParameterConstructor();

        testObject.registerInstance(TestClassWithDefaultAndParameterConstructor.class, obj);

        TestClassWithDefaultAndParameterConstructor cls = null;

        try
        {
            cls = testObject.resolve(TestClassWithDefaultAndParameterConstructor.class);
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
        TestClassInheritsFromClassWithParameterConstructorOnly obj = new TestClassInheritsFromClassWithParameterConstructorOnly();

        testObject.registerInstance(TestClassWithParameterConstructorOnly.class, obj);

        TestClassWithParameterConstructorOnly cls = null;

        try
        {
            cls = testObject.resolve(TestClassWithParameterConstructorOnly.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
        Assert.assertSame(obj, cls);
        Assert.assertTrue(cls instanceof TestClassInheritsFromClassWithParameterConstructorOnly);
        Assert.assertEquals(obj.getNumber(), cls.getNumber());
    }

    @Test(expected = NullInstanceException.class)
    public void testRegisterInstanceWhenInstanceIsNull()
        throws IllegalArgumentException
    {
        testObject.registerInstance(TestClassWithDefaultAndParameterConstructor.class, null);
    }

    // endregion
    //region resolve (class)

    @Test
    public void testResolveWhenClassHasDefaultConstructorOnly()
    {
        TestClassWithDefaultConstructorOnly cls = null;

        try
        {
            cls = testObject.resolve(TestClassWithDefaultConstructorOnly.class);
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
        TestClassInheritsFromClassWithParameterConstructorOnly cls = null;

        try
        {
            cls = testObject.resolve(TestClassInheritsFromClassWithParameterConstructorOnly.class);
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
        TestClassInheritsFromAbstractClass cls = null;

        try
        {
            cls = testObject.resolve(TestClassInheritsFromAbstractClass.class);
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
        testObject.resolve(TestClassWithParameterConstructorOnly.class);
    }

    @Test
    public void testResolveWhenClassHasParameterConstructorWithRegisteredPrimitiveParameter()
    {
        int number = 10;

        testObject.registerInstance(int.class, number);

        TestClassWithParameterConstructorOnly cls = null;

        try
        {
            cls = testObject.resolve(TestClassWithParameterConstructorOnly.class);
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

        TestClassWithParameterConstructorOnly cls = null;

        try
        {
            cls = testObject.resolve(TestClassWithParameterConstructorOnly.class);
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
        TestClassWithDefaultAndParameterConstructor cls = null;
        try
        {
            cls = testObject.resolve(TestClassWithDefaultAndParameterConstructor.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
    }

    @Test(expected = AbstractTypeException.class)
    public void testResolveWhenInterface()
        throws DIException

    {
        testObject.resolve(TestInterfaceBasic.class);
    }

    @Test(expected = AbstractTypeException.class)
    public void testResolveWhenAbstractClass()
        throws DIException

    {
        testObject.resolve(TestClassAbstract.class);
    }

    // endregion
    // region resolve (constructors)

    @Test(expected = MultipleAnnotatedConstructorsException.class)
    public void testResolveWhenMultipleAnnotatedConstructors()
        throws DIException

    {
        testObject.resolve(TestClassWithMultipleAnnotatedConstructors.class);
    }

    @Test(expected = NoSuitableConstructorException.class)
    public void testResolveWhenNoPublicConstructors()
        throws DIException

    {
        testObject.resolve(TestClassWithPrivateConstructorOnly.class);
    }

    @Test(expected = NoSuitableConstructorException.class)
    public void testResolveWhenNoDeclaredConstructors()
        throws DIException

    {
        testObject.resolve(TestClassWithNoDeclaredConstructors.class);
    }

    // endregion
    // region resolve (dependencies)

    @Test
    public void testResolveDependenciesWithRegisteredInstance()
    {
        String string = "String";

        testObject.registerInstance(String.class, string)
                  .registerType(TestInterfaceBasic.class, TestClassWithDefaultConstructorOnly.class)
                  .registerType(TestInterfaceDiamond1.class, TestClassDiamond1.class)
                  .registerType(TestInterfaceWithString.class, TestClassWithString.class)
                  .registerType(TestInterfaceSimpleDependency.class,
                                TestClassSimpleDependencyWithoutAnnotation.class);

        TestInterfaceSimpleDependency cls = null;

        try
        {
            cls = testObject.resolve(TestInterfaceSimpleDependency.class);
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
        Assert.assertTrue(cls instanceof TestClassSimpleDependencyWithoutAnnotation);
    }

    @Test
    public void testResolveDependenciesWithoutAnnotatedConstructorsWithAllDependencies()
    {
        testObject.registerType(TestInterfaceBasic.class, TestClassWithDefaultConstructorOnly.class)
                  .registerType(TestInterfaceDiamond1.class, TestClassDiamond1.class)
                  .registerType(TestInterfaceWithString.class, TestClassWithString.class)
                  .registerType(TestInterfaceSimpleDependency.class,
                                TestClassSimpleDependencyWithoutAnnotation.class);

        TestInterfaceSimpleDependency cls = null;

        try
        {
            cls = testObject.resolve(TestInterfaceSimpleDependency.class);
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
        Assert.assertTrue(cls instanceof TestClassSimpleDependencyWithoutAnnotation);
    }

    @Test
    public void testResolveDependenciesWithoutAnnotatedConstructorsWithoutSomeDependencies()
    {
        testObject.registerType(TestInterfaceBasic.class, TestClassWithDefaultConstructorOnly.class)
                  .registerType(TestInterfaceDiamond1.class, TestClassDiamond1.class)
                  .registerType(TestInterfaceSimpleDependency.class,
                                TestClassSimpleDependencyWithoutAnnotation.class);

        TestInterfaceSimpleDependency cls = null;

        try
        {
            cls = testObject.resolve(TestInterfaceSimpleDependency.class);
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
        Assert.assertTrue(cls instanceof TestClassSimpleDependencyWithoutAnnotation);
    }

    @Test
    public void testResolveDependenciesWithAnnotatedConstructor()
    {
        testObject.registerType(TestInterfaceBasic.class, TestClassWithDefaultConstructorOnly.class)
                  .registerType(TestInterfaceDiamond1.class, TestClassDiamond1.class)
                  .registerType(TestInterfaceWithString.class, TestClassWithString.class)
                  .registerType(TestInterfaceSimpleDependency.class,
                                TestClassSimpleDependencyWithAnnotation.class);

        TestInterfaceSimpleDependency cls = null;

        try
        {
            cls = testObject.resolve(TestInterfaceSimpleDependency.class);
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
        Assert.assertTrue(cls instanceof TestClassSimpleDependencyWithAnnotation);
    }

    @Test(expected = MissingDependenciesException.class)
    public void testResolveDependenciesWithAnnotatedConstructorWithoutSomeDependencies()
        throws DIException

    {
        testObject.registerType(TestInterfaceDiamond1.class, TestClassDiamond1.class)
                  .registerType(TestInterfaceDiamond2.class, TestClassDiamond2.class)
                  .registerType(TestInterfaceDiamondBase.class, TestClassDiamondBase.class);

        testObject.resolve(TestInterfaceDiamondBase.class);
    }

    @Test
    public void testResolveDiamondDependenciesWithoutSingleton()
    {
        testObject.registerType(TestInterfaceBasic.class, TestClassWithDefaultConstructorOnly.class)
                  .registerType(TestInterfaceDiamond1.class, TestClassDiamond1.class)
                  .registerType(TestInterfaceDiamond2.class, TestClassDiamond2.class)
                  .registerType(TestInterfaceDiamondBase.class, TestClassDiamondBase.class);

        TestInterfaceDiamondBase cls = null;

        try
        {
            cls = testObject.resolve(TestInterfaceDiamondBase.class);
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
        Assert.assertTrue(cls instanceof TestClassDiamondBase);
    }

    @Test
    public void testResolveDiamondDependenciesWithSingleton()
    {
        testObject.registerType(TestInterfaceBasic.class, TestClassWithDefaultConstructorOnly.class,
                                ConstructionPolicy.SINGLETON)
                  .registerType(TestInterfaceDiamond1.class, TestClassDiamond1.class)
                  .registerType(TestInterfaceDiamond2.class, TestClassDiamond2.class)
                  .registerType(TestInterfaceDiamondBase.class, TestClassDiamondBase.class);

        TestInterfaceDiamondBase cls = null;

        try
        {
            cls = testObject.resolve(TestInterfaceDiamondBase.class);
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
        Assert.assertTrue(cls instanceof TestClassDiamondBase);
    }

    @Test(expected = CircularDependenciesException.class)
    public void testResolveCircularDependencies()
        throws DIException

    {
        testObject.registerType(TestInterfaceCircular1.class, TestClassCircular1.class)
                  .registerType(TestInterfaceCircular2.class, TestClassCircular2.class);

        testObject.resolve(TestInterfaceCircular2.class);
    }

    @Test
    public void testResolveWhenCanOmitCircularDependencies()
    {
        String string = "String";

        testObject.registerInstance(String.class, string)
                  .registerType(TestInterfaceWithString.class, TestClassWithString.class)
                  .registerType(TestInterfaceCircular1.class, TestClassCircular1.class)
                  .registerType(TestInterfaceCircular2.class, TestClassCircular2.class)
                  .registerType(TestInterfaceCircularDependency.class,
                                TestClassCircularDependency.class);

        TestInterfaceCircularDependency cls = null;

        try
        {
            cls = testObject.resolve(TestInterfaceCircularDependency.class);
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
        Assert.assertTrue(cls instanceof TestClassCircularDependency);
    }

    // endregion
    // region resolve (annotations)

    @Test(expected = IncorrectDependencySetterException.class)
    public void testResolveWhenDependencySetterHasReturnType()
        throws DIException
    {
        testObject.resolve(TestClassWithIncorrectDependencySetter1.class);
    }

    @Test(expected = IncorrectDependencySetterException.class)
    public void testResolveWhenDependencySetterHasNoParameters()
        throws DIException
    {
        testObject.resolve(TestClassWithIncorrectDependencySetter2.class);
    }

    @Test(expected = IncorrectDependencySetterException.class)
    public void testResolveWhenDependencySetterNameDoesNotStartWithSet()
        throws DIException
    {
        testObject.resolve(TestClassWithIncorrectDependencySetter3.class);
    }

    @Test
    public void testResolveWhenDependencySetterOnly()
    {
        testObject.registerType(TestInterfaceDependencySetter.class,
                                TestClassWithDependencySetterOnly.class)
                  .registerType(TestInterfaceBasic.class,
                                TestClassWithDefaultConstructorOnly.class);

        TestInterfaceDependencySetter cls = null;

        try
        {
            cls = testObject.resolve(TestInterfaceDependencySetter.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
        Assert.assertNotNull(cls.getBasicObject());
        Assert.assertTrue(cls instanceof TestClassWithDependencySetterOnly);
    }

    @Test
    public void testResolveWhenDependencySetterAndConstructor()
    {
        testObject.registerType(TestInterfaceDependencySetter.class,
                                TestClassWithDependencySetterAndConstructor.class)
                  .registerType(TestInterfaceBasic.class,
                                TestClassWithDefaultConstructorOnly.class);

        TestInterfaceDependencySetter cls = null;

        try
        {
            cls = testObject.resolve(TestInterfaceDependencySetter.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail("An instance of " + e.getClass().getSimpleName() + " was thrown.");
        }

        Assert.assertNotNull(cls);
        Assert.assertNotNull(cls.getBasicObject());
        Assert.assertTrue(cls instanceof TestClassWithDependencySetterAndConstructor);
    }

    @Test
    public void testResolveWhenComplexDependency()
    {
        String string = "string";

        testObject.registerType(TestInterfaceComplexDependency.class,
                                TestClassComplexDependency.class)
                  .registerType(TestInterfaceBasic.class, TestClassWithDefaultConstructorOnly.class)
                  .registerType(TestInterfaceDiamond1.class, TestClassDiamond1.class)
                  .registerType(TestInterfaceWithString.class, TestClassWithString.class);

        testObject.registerInstance(String.class, string);

        TestInterfaceComplexDependency cls = null;

        try
        {
            cls = testObject.resolve(TestInterfaceComplexDependency.class);
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
        Assert.assertTrue(cls instanceof TestClassComplexDependency);
    }

    @Test
    public void testResolveWhenDoubleDependencySetter()
    {
        String string = "string";

        testObject.registerType(TestInterfaceDoubleDependencySetter.class,
                                TestClassWithDoubleDependencySetter.class)
                  .registerType(TestInterfaceBasic.class, TestClassWithDefaultConstructorOnly.class)
                  .registerType(TestInterfaceWithString.class, TestClassWithString.class);

        testObject.registerInstance(String.class, string);

        TestInterfaceDoubleDependencySetter cls = null;

        try
        {
            cls = testObject.resolve(TestInterfaceDoubleDependencySetter.class);
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
        Assert.assertTrue(cls instanceof TestClassWithDoubleDependencySetter);
    }

    @Test
    public void testResolveWhenMultipleDependencySetters()
    {
        String string = "string";

        testObject.registerType(TestInterfaceMultipleDependencySetters.class,
                                TestClassWithMultipleDependencySetters.class)
                  .registerType(TestInterfaceBasic.class, TestClassWithDefaultConstructorOnly.class)
                  .registerType(TestInterfaceWithString.class, TestClassWithString.class);

        testObject.registerInstance(String.class, string);

        TestInterfaceMultipleDependencySetters cls = null;

        try
        {
            cls = testObject.resolve(TestInterfaceMultipleDependencySetters.class);
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
        Assert.assertTrue(cls instanceof TestClassWithMultipleDependencySetters);
    }

    // endregion
    // region buildUp

    @Test
    public void testBuildUpWhenDependencySetterOnly()
    {
        testObject.registerType(TestInterfaceBasic.class,
                                TestClassWithDefaultConstructorOnly.class);

        TestInterfaceDependencySetter cls = new TestClassWithDependencySetterOnly();

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

    @Test
    public void testBuildUpWhenDoubleDependencySetter()
    {
        String string = "string";

        testObject.registerType(TestInterfaceBasic.class, TestClassWithDefaultConstructorOnly.class)
                  .registerType(TestInterfaceWithString.class, TestClassWithString.class);

        testObject.registerInstance(String.class, string);

        TestInterfaceDoubleDependencySetter cls = new TestClassWithDoubleDependencySetter();

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
    public void testBuildUpWhenMultipleDependencySetters()
    {
        String string = "string";

        testObject.registerType(TestInterfaceBasic.class, TestClassWithDefaultConstructorOnly.class)
                  .registerType(TestInterfaceWithString.class, TestClassWithString.class)
                  .registerInstance(String.class, string);

        TestInterfaceMultipleDependencySetters cls = new TestClassWithMultipleDependencySetters();

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

        testObject.registerType(TestInterfaceBasic.class, TestClassWithDefaultConstructorOnly.class)
                  .registerType(TestInterfaceDiamond1.class, TestClassDiamond1.class)
                  .registerType(TestInterfaceWithString.class, TestClassWithString.class)
                  .registerInstance(String.class, string);

        TestInterfaceComplexDependency cls = null;

        try
        {
            TestInterfaceDiamond1 diamond1 = testObject.resolve(TestInterfaceDiamond1.class);
            TestInterfaceWithString withString = testObject.resolve(TestInterfaceWithString.class);

            cls = new TestClassComplexDependency(diamond1, withString);
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
