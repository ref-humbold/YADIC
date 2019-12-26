package dicontainer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dicontainer.auxiliary.basics.*;
import dicontainer.auxiliary.circulars.*;
import dicontainer.auxiliary.constructors.*;
import dicontainer.auxiliary.diamonds.*;
import dicontainer.auxiliary.register.*;
import dicontainer.auxiliary.setters.*;
import dicontainer.exception.*;

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
    public void register_WhenSingleClass_ThenDifferentInstances()
    {
        try
        {
            testObject.registerType(ClassConstructorsDefault.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertNotSame(result1, result2);
    }

    @Test
    public void register_WhenSingleClassAsSingleton_ThenSameInstance()
    {
        try
        {
            testObject.registerType(ClassConstructorsDefault.class, ConstructionPolicy.SINGLETON);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertSame(result1, result2);
    }

    @Test
    public void register_WhenSingleClassChangesSingleton_ThenChangesInstances()
    {
        try
        {
            testObject.registerType(ClassConstructorsDefault.class, ConstructionPolicy.SINGLETON);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assert.assertNotNull(result21);
        Assert.assertNotNull(result22);
        Assert.assertNotSame(result21, result22);
    }

    @Test(expected = AbstractTypeException.class)
    public void register_WhenSingleClassIsInterface_ThenAbstractTypeException()
            throws AbstractTypeException
    {
        testObject.registerType(InterfaceBasics.class);
    }

    @Test(expected = AbstractTypeException.class)
    public void register_WhenSingleClassIsAbstractClass_ThenAbstractTypeException()
            throws AbstractTypeException
    {
        testObject.registerType(ClassBasicsAbstract.class);
    }

    // endregion
    // region registerType (inheritance)

    @Test
    public void register_WhenInheritanceFromInterface_ThenDifferentInstances()
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertNotSame(result1, result2);
        Assert.assertTrue(result1 instanceof ClassConstructorsDefault);
        Assert.assertTrue(result2 instanceof ClassConstructorsDefault);
    }

    @Test
    public void register_WhenInheritanceFromInterfaceAsSingleton_ThenSameInstances()
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertSame(result1, result2);
        Assert.assertTrue(result1 instanceof ClassConstructorsDefault);
        Assert.assertTrue(result2 instanceof ClassConstructorsDefault);
    }

    @Test
    public void register_WhenInheritanceFromInterfaceChangesSingleton_ThenChangeInstances()
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assert.assertNotNull(result21);
        Assert.assertNotNull(result22);
        Assert.assertNotSame(result21, result22);
        Assert.assertTrue(result21 instanceof ClassConstructorsDefault);
        Assert.assertTrue(result22 instanceof ClassConstructorsDefault);
    }

    @Test
    public void register_WhenInheritanceFromInterfaceChangesClass_ThenInstanceIsDerived()
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assert.assertNotNull(result3);
        Assert.assertTrue(result3 instanceof ClassConstructorsDefaultAndParameter);
    }

    @Test
    public void register_WhenInheritanceFromAbstractClass_ThenInstanceIsDerived()
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof ClassBasicsInheritsFromAbstract);
    }

    @Test
    public void register_WhenInheritanceFromConcreteClass_ThenInstanceIsDerived()
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof ClassConstructorsInheritParameter);
    }

    @Test
    public void register_WhenTwoStepsOfHierarchy_ThenInstanceIsDerived()
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof ClassBasicsInheritsFromAbstract);
    }

    // endregion
    // region registerInstance

    @Test
    public void registerInstance_WhenInterface_ThenRegisteredInstance()
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof ClassConstructorsDefault);
        Assert.assertSame(obj, result);
    }

    @Test
    public void registerInstance_WhenAbstractClass_ThenRegisteredInstance()
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof ClassBasicsInheritsFromAbstract);
        Assert.assertSame(obj, result);
    }

    @Test
    public void registerInstance_WhenSameConcreteClass_ThenRegisteredInstance()
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assert.assertNotNull(result);
        Assert.assertSame(obj, result);
        Assert.assertEquals(obj.getText(), result.getText());
    }

    @Test
    public void registerInstance_WhenDerivedConcreteClass_ThenRegisteredInstance()
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assert.assertNotNull(result);
        Assert.assertSame(obj, result);
        Assert.assertTrue(result instanceof ClassConstructorsInheritParameter);
        Assert.assertEquals(obj.getNumber(), result.getNumber());
    }

    @Test(expected = NullInstanceException.class)
    public void registerInstance_WhenInstanceIsNull_ThenNullInstanceException()
            throws IllegalArgumentException
    {
        testObject.registerInstance(ClassConstructorsDefaultAndParameter.class, null);
    }

    // endregion
    //region resolve (class)

    @Test
    public void resolve_WhenClassHasDefaultConstructorOnly_ThenInstanceIsResolved()
    {
        ClassConstructorsDefault result = null;

        try
        {
            result = testObject.resolve(ClassConstructorsDefault.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assert.assertNotNull(result);
    }

    @Test
    public void resolve_WhenClassInheritsFromConcreteClass_ThenInstanceIsResolved()
    {
        ClassConstructorsInheritParameter result = null;

        try
        {
            result = testObject.resolve(ClassConstructorsInheritParameter.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assert.assertNotNull(result);
    }

    @Test
    public void resolve_WhenClassInheritsFromAbstractClass_ThenInstanceIsResolved()
    {
        ClassBasicsInheritsFromAbstract result = null;

        try
        {
            result = testObject.resolve(ClassBasicsInheritsFromAbstract.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assert.assertNotNull(result);
    }

    @Test(expected = MissingDependenciesException.class)
    public void resolve_WhenClassHasParameterConstructorWithoutRegisteredParameter_ThenMissingDependenciesException()
            throws DIException
    {
        testObject.resolve(ClassConstructorsParameter.class);
    }

    @Test
    public void resolve_WhenClassHasParameterConstructorWithRegisteredPrimitiveParameter_ThenInstanceIsResolved()
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assert.assertNotNull(result);
        Assert.assertEquals(number, result.getNumber());
    }

    @Test
    public void resolve_WhenClassHasParameterConstructorWithRegisteredReferenceParameter_ThenInstanceIsResolved()
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assert.assertNotNull(result);
    }

    @Test
    public void resolve_WhenClassHasDefaultAndParameterConstructorWithoutRegisteredParameter_ThenInstanceIsResolved()
    {
        ClassConstructorsDefaultAndParameter result = null;

        try
        {
            result = testObject.resolve(ClassConstructorsDefaultAndParameter.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assert.assertNotNull(result);
    }

    @Test(expected = MissingDependenciesException.class)
    public void resolve_WhenInterface_ThenMissingDependenciesException()
            throws DIException
    {
        testObject.resolve(InterfaceBasics.class);
    }

    @Test(expected = MissingDependenciesException.class)
    public void resolve_WhenAbstractClass_ThenMissingDependenciesException()
            throws DIException
    {
        testObject.resolve(ClassBasicsAbstract.class);
    }

    // endregion
    // region resolve (dependencies schemas)

    @Test
    public void resolve_WhenDependenciesWithRegisteredInstance_ThenInstanceIsResolved()
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
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
    public void resolve_WhenDependenciesWithoutAnnotatedConstructorsWithAllDependencies_ThenInstanceIsResolved()
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
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
    public void resolve_WhenDependenciesWithoutAnnotatedConstructorsWithoutSomeDependencies_ThenInstanceIsResolved()
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getFirstObject());
        Assert.assertNull(result.getSecondObject());
        Assert.assertNotNull(result.getFirstObject().getObject());
        Assert.assertTrue(result instanceof ClassConstructorsNoAnnotationDependency);
    }

    @Test
    public void resolve_WhenDependenciesWithAnnotatedConstructor_ThenInstanceIsResolved()
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
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
    public void resolve_WhenDependenciesWithAnnotatedConstructorWithoutSomeDependencies_ThenMissingDependenciesException()
            throws DIException
    {
        testObject.registerType(InterfaceDiamonds1.class, ClassDiamonds1.class)
                  .registerType(InterfaceDiamonds2.class, ClassDiamonds2.class)
                  .registerType(InterfaceDiamondsBottom.class, ClassDiamondsBottom.class);

        testObject.resolve(InterfaceDiamondsBottom.class);
    }

    @Test
    public void resolve_WhenDiamondDependenciesWithoutSingleton_ThenInstanceIsResolved()
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
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
    public void resolve_WhenDiamondDependenciesWithSingleton_ThenInstanceIsResolved()
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
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
    public void resolve_WhenCircularDependencies_ThenCircularDependenciesException()
            throws DIException
    {
        testObject.registerType(InterfaceCirculars1.class, ClassCirculars1.class)
                  .registerType(InterfaceCirculars2.class, ClassCirculars2.class);

        testObject.resolve(InterfaceCirculars2.class);
    }

    @Test
    public void resolve_WhenCanOmitCircularDependencies_ThenInstanceIsResolved()
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
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
    public void resolve_WhenMultipleAnnotatedConstructors_ThenMultipleAnnotatedConstructorsException()
            throws DIException
    {
        testObject.resolve(ClassConstructorsMultipleAnnotated.class);
    }

    @Test(expected = NoSuitableConstructorException.class)
    public void resolve_WhenNoPublicConstructors_ThenNoSuitableConstructorException()
            throws DIException
    {
        testObject.resolve(ClassConstructorsPrivate.class);
    }

    @Test(expected = IncorrectDependencySetterException.class)
    public void resolve_WhenDependencySetterHasReturnType_ThenIncorrectDependencySetterException()
            throws DIException
    {
        testObject.resolve(ClassSettersIncorrect1.class);
    }

    @Test(expected = IncorrectDependencySetterException.class)
    public void resolve_WhenDependencySetterHasNoParameters_ThenIncorrectDependencySetterException()
            throws DIException
    {
        testObject.resolve(ClassSettersIncorrect2.class);
    }

    @Test(expected = IncorrectDependencySetterException.class)
    public void resolve_WhenDependencySetterNameDoesNotStartWithSet_ThenIncorrectDependencySetterException()
            throws DIException
    {
        testObject.resolve(ClassSettersIncorrect3.class);
    }

    @Test
    public void resolve_WhenDependencySetterOnly_ThenInstanceIsResolved()
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getBasicObject());
        Assert.assertTrue(result instanceof ClassSettersSingle);
    }

    @Test
    public void resolve_WhenDependencySetterAndConstructor_ThenInstanceIsResolved()
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getBasicObject());
        Assert.assertTrue(result instanceof ClassSettersConstructor);
    }

    @Test
    public void resolve_WhenComplexDependency_ThenInstanceIsResolved()
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
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
    public void resolve_WhenDoubleDependencySetter_ThenIncorrectDependencySetterException()
            throws DIException
    {
        testObject.registerType(InterfaceSettersDouble.class, ClassSettersDouble.class);

        testObject.resolve(InterfaceSettersDouble.class);
    }

    @Test
    public void resolve_WhenMultipleDependencySetters_ThenInstanceIsResolved()
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
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
    public void resolve_WhenAnnotatedInterface_ThenInstanceIsResolved()
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
            Assert.fail(String.format("An instance of %s was thrown.", e.getClass().getName()));
        }

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertTrue(result1 instanceof ClassRegisterInterface);
        Assert.assertTrue(result2 instanceof ClassRegisterInterface);
        Assert.assertNotSame(result1, result2);
    }

    @Test
    public void resolve_WhenAnnotatedAbstractClass_ThenInstanceIsResolved()
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
            Assert.fail(String.format("An instance of %s was thrown.", e.getClass().getName()));
        }

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertTrue(result2 instanceof ClassRegisterBase);
        Assert.assertTrue(result1 instanceof ClassRegisterBase);
        Assert.assertNotSame(result1, result2);
    }

    @Test
    public void resolve_WhenAnnotatedConcreteClass_ThenInstanceIsResolved()
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
            Assert.fail(String.format("An instance of %s was thrown.", e.getClass().getName()));
        }

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertTrue(result1 instanceof ClassRegisterDerived);
        Assert.assertTrue(result2 instanceof ClassRegisterDerived);
        Assert.assertNotSame(result1, result2);
    }

    @Test
    public void resolve_WhenSelfAnnotatedConcreteClass_ThenInstanceIsResolved()
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
            Assert.fail(String.format("An instance of %s was thrown.", e.getClass().getName()));
        }

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertNotSame(result1, result2);
    }

    @Test
    public void resolve_WhenAnnotatedConcreteClassAsItself_ThenInstanceIsResolved()
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
            Assert.fail(String.format("An instance of %s was thrown.", e.getClass().getName()));
        }

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertNotSame(result1, result2);
    }

    @Test(expected = AbstractTypeException.class)
    public void resolve_WhenSelfAnnotatedInterface_ThenAbstractTypeException()
            throws DIException
    {
        testObject.resolve(InterfaceRegisterSelfIncorrect.class);
    }

    @Test(expected = NotDerivedTypeException.class)
    public void resolve_WhenInterfaceAnnotatedClassNotImplementing_ThenNotDerivedTypeException()
            throws DIException
    {
        testObject.resolve(InterfaceRegisterIncorrect.class);
    }

    @Test(expected = NotDerivedTypeException.class)
    public void resolve_WhenAnnotatedClassRegistersInterface_ThenNotDerivedTypeException()
            throws DIException
    {
        testObject.resolve(ClassRegisterInterfaceIncorrect.class);
    }

    @Test(expected = NotDerivedTypeException.class)
    public void resolve_WhenAnnotatedClassRegistersNotDerivedClass_ThenNotDerivedTypeException()
            throws DIException
    {
        testObject.resolve(ClassRegisterIncorrect1.class);
    }

    @Test(expected = NotDerivedTypeException.class)
    public void resolve_WhenAnnotatedClassRegistersAbstractSuperclass_ThenNotDerivedTypeException()
            throws DIException
    {
        testObject.resolve(ClassRegisterIncorrect2.class);
    }

    @Test(expected = NotDerivedTypeException.class)
    public void resolve_WhenAnnotatedClassRegistersAbstractConcreteSuperclass_ThenNotDerivedTypeException()
            throws DIException
    {
        testObject.resolve(ClassRegisterIncorrect3.class);
    }

    @Test(expected = AbstractTypeException.class)
    public void resolve_WhenSelfAnnotatedAbstractClass_ThenAbstractTypeException()
            throws DIException
    {
        testObject.resolve(ClassRegisterSelfAbstractIncorrect.class);
    }

    @Test(expected = AbstractTypeException.class)
    public void resolve_WhenAnnotatedAbstractClassAsItself_ThenAbstractTypeException()
            throws DIException
    {
        testObject.resolve(ClassRegisterAbstractIncorrect.class);
    }

    @Test
    public void resolve_WhenAnnotatedInterfaceSingleton_ThenSameInstances()
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
            Assert.fail(String.format("An instance of %s was thrown.", e.getClass().getName()));
        }

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertTrue(result1 instanceof ClassRegisterSingletonBase);
        Assert.assertTrue(result2 instanceof ClassRegisterSingletonBase);
        Assert.assertSame(result1, result2);
    }

    @Test
    public void resolve_WhenAnnotatedConcreteClassSingleton_ThenSameInstances()
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
            Assert.fail(String.format("An instance of %s was thrown.", e.getClass().getName()));
        }

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertTrue(result1 instanceof ClassRegisterSingletonDerived);
        Assert.assertTrue(result2 instanceof ClassRegisterSingletonDerived);
        Assert.assertSame(result1, result2);
    }

    @Test
    public void resolve_WhenSelfAnnotatedConcreteClassSingleton_ThenSameInstances()
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
            Assert.fail(String.format("An instance of %s was thrown.", e.getClass().getName()));
        }

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertSame(result1, result2);
    }

    @Test
    public void resolve_WhenAnnotatedConcreteClassAsItselfSingleton_ThenSameInstances()
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
            Assert.fail(String.format("An instance of %s was thrown.", e.getClass().getName()));
        }

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertSame(result1, result2);
    }

    // endregion
    // region buildUp

    @Test
    public void buildUp_WhenDependencySetterOnly_ThenInstanceIsBuiltUp()
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assert.assertNotNull(result);
        Assert.assertNotNull(instance);
        Assert.assertNotNull(result.getBasicObject());
        Assert.assertNotNull(instance.getBasicObject());
        Assert.assertSame(instance, result);
    }

    @Test(expected = IncorrectDependencySetterException.class)
    public void buildUp_WhenDoubleDependencySetter_ThenIncorrectDependencySetterException()
            throws DIException
    {
        InterfaceSettersDouble instance = new ClassSettersDouble();

        testObject.buildUp(instance);
    }

    @Test
    public void buildUp_WhenMultipleDependencySetters_ThenInstanceIsBuiltUp()
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
            Assert.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
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
    public void buildUp_WhenComplexDependency_ThenInstanceIsBuiltUp()
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
            InterfaceBasicsStringGetter withString =
                    testObject.resolve(InterfaceBasicsStringGetter.class);

            instance = new ClassBasicsComplexDependency(diamond1, withString);
            result = testObject.buildUp(instance);
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
        Assert.assertEquals(string, result.getSecondObject().getString());
        Assert.assertEquals(string, instance.getSecondObject().getString());
        Assert.assertSame(instance, result);
    }

    // endregion
}
