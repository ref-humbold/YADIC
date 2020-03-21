package dicontainer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @BeforeEach
    public void setUp()
    {
        testObject = new DIContainer();
    }

    @AfterEach
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertNotSame(result1, result2);
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertSame(result1, result2);
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result11);
        Assertions.assertNotNull(result12);
        Assertions.assertSame(result11, result12);

        try
        {
            testObject.registerType(ClassConstructorsDefault.class, ConstructionPolicy.CONSTRUCT);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result21);
        Assertions.assertNotNull(result22);
        Assertions.assertNotSame(result21, result22);
    }

    @Test
    public void register_WhenSingleClassIsInterface_ThenAbstractTypeException()
    {
        Assertions.assertThrows(AbstractTypeException.class,
                                () -> testObject.registerType(InterfaceBasics.class));
    }

    @Test
    public void register_WhenSingleClassIsAbstractClass_ThenAbstractTypeException()
    {
        Assertions.assertThrows(AbstractTypeException.class,
                                () -> testObject.registerType(ClassBasicsAbstract.class));
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertNotSame(result1, result2);
        Assertions.assertTrue(result1 instanceof ClassConstructorsDefault);
        Assertions.assertTrue(result2 instanceof ClassConstructorsDefault);
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertSame(result1, result2);
        Assertions.assertTrue(result1 instanceof ClassConstructorsDefault);
        Assertions.assertTrue(result2 instanceof ClassConstructorsDefault);
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result11);
        Assertions.assertNotNull(result12);
        Assertions.assertSame(result11, result12);
        Assertions.assertTrue(result11 instanceof ClassConstructorsDefault);
        Assertions.assertTrue(result12 instanceof ClassConstructorsDefault);

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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result21);
        Assertions.assertNotNull(result22);
        Assertions.assertNotSame(result21, result22);
        Assertions.assertTrue(result21 instanceof ClassConstructorsDefault);
        Assertions.assertTrue(result22 instanceof ClassConstructorsDefault);
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result1);
        Assertions.assertTrue(result1 instanceof ClassConstructorsDefault);

        testObject.registerType(InterfaceBasics.class, ClassConstructorsDefaultAndParameter.class);

        InterfaceBasics result3 = null;

        try
        {
            result3 = testObject.resolve(InterfaceBasics.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result3);
        Assertions.assertTrue(result3 instanceof ClassConstructorsDefaultAndParameter);
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof ClassBasicsInheritsFromAbstract);
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof ClassConstructorsInheritParameter);
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof ClassBasicsInheritsFromAbstract);
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof ClassConstructorsDefault);
        Assertions.assertSame(obj, result);
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof ClassBasicsInheritsFromAbstract);
        Assertions.assertSame(obj, result);
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertSame(obj, result);
        Assertions.assertEquals(obj.getText(), result.getText());
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertSame(obj, result);
        Assertions.assertTrue(result instanceof ClassConstructorsInheritParameter);
        Assertions.assertEquals(obj.getNumber(), result.getNumber());
    }

    @Test
    public void registerInstance_WhenInstanceIsNull_ThenNullInstanceException()
    {
        Assertions.assertThrows(NullInstanceException.class, () -> testObject.registerInstance(
                ClassConstructorsDefaultAndParameter.class, null));
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
    }

    @Test
    public void resolve_WhenClassHasParameterConstructorWithoutRegisteredParameter_ThenMissingDependenciesException()
    {
        Assertions.assertThrows(MissingDependenciesException.class,
                                () -> testObject.resolve(ClassConstructorsParameter.class));
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertEquals(number, result.getNumber());
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
    }

    @Test
    public void resolve_WhenInterface_ThenMissingDependenciesException()
    {
        Assertions.assertThrows(MissingDependenciesException.class,
                                () -> testObject.resolve(InterfaceBasics.class));
    }

    @Test
    public void resolve_WhenAbstractClass_ThenMissingDependenciesException()
    {
        Assertions.assertThrows(MissingDependenciesException.class,
                                () -> testObject.resolve(ClassBasicsAbstract.class));
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getFirstObject());
        Assertions.assertNotNull(result.getSecondObject());
        Assertions.assertNotNull(result.getFirstObject().getObject());
        Assertions.assertNotNull(result.getSecondObject().getString());
        Assertions.assertEquals(string, result.getSecondObject().getString());
        Assertions.assertTrue(result instanceof ClassConstructorsNoAnnotationDependency);
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getFirstObject());
        Assertions.assertNotNull(result.getSecondObject());
        Assertions.assertNotNull(result.getFirstObject().getObject());
        Assertions.assertNotNull(result.getSecondObject().getString());
        Assertions.assertEquals("", result.getSecondObject().getString());
        Assertions.assertTrue(result instanceof ClassConstructorsNoAnnotationDependency);
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getFirstObject());
        Assertions.assertNull(result.getSecondObject());
        Assertions.assertNotNull(result.getFirstObject().getObject());
        Assertions.assertTrue(result instanceof ClassConstructorsNoAnnotationDependency);
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getFirstObject());
        Assertions.assertNotNull(result.getSecondObject());
        Assertions.assertNotNull(result.getFirstObject().getObject());
        Assertions.assertNotNull(result.getSecondObject().getString());
        Assertions.assertEquals("", result.getSecondObject().getString());
        Assertions.assertTrue(result instanceof ClassConstructorsAnnotationDependency);
    }

    @Test
    public void resolve_WhenDependenciesWithAnnotatedConstructorWithoutSomeDependencies_ThenMissingDependenciesException()
    {
        testObject.registerType(InterfaceDiamonds1.class, ClassDiamonds1.class)
                  .registerType(InterfaceDiamonds2.class, ClassDiamonds2.class)
                  .registerType(InterfaceDiamondsBottom.class, ClassDiamondsBottom.class);

        Assertions.assertThrows(MissingDependenciesException.class,
                                () -> testObject.resolve(InterfaceDiamondsBottom.class));
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getDiamond1());
        Assertions.assertNotNull(result.getDiamond2());
        Assertions.assertNotNull(result.getDiamond1().getObject());
        Assertions.assertNotNull(result.getDiamond2().getObject());
        Assertions.assertNotSame(result.getDiamond1().getObject(),
                                 result.getDiamond2().getObject());
        Assertions.assertTrue(result instanceof ClassDiamondsBottom);
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getDiamond1());
        Assertions.assertNotNull(result.getDiamond2());
        Assertions.assertNotNull(result.getDiamond1().getObject());
        Assertions.assertNotNull(result.getDiamond2().getObject());
        Assertions.assertSame(result.getDiamond1().getObject(), result.getDiamond2().getObject());
        Assertions.assertTrue(result instanceof ClassDiamondsBottom);
    }

    @Test
    public void resolve_WhenCircularDependencies_ThenCircularDependenciesException()
    {
        testObject.registerType(InterfaceCirculars1.class, ClassCirculars1.class)
                  .registerType(InterfaceCirculars2.class, ClassCirculars2.class);

        Assertions.assertThrows(CircularDependenciesException.class,
                                () -> testObject.resolve(InterfaceCirculars2.class));
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getNonCircularObject());
        Assertions.assertNull(result.getCircularObject());
        Assertions.assertNotNull(result.getNonCircularObject().getString());
        Assertions.assertEquals(string, result.getNonCircularObject().getString());
        Assertions.assertTrue(result instanceof ClassCircularsDependency);
    }

    // endregion
    // region resolve (@Dependency)

    @Test
    public void resolve_WhenMultipleAnnotatedConstructors_ThenMultipleAnnotatedConstructorsException()
    {
        Assertions.assertThrows(MultipleAnnotatedConstructorsException.class,
                                () -> testObject.resolve(ClassConstructorsMultipleAnnotated.class));
    }

    @Test
    public void resolve_WhenNoPublicConstructors_ThenNoSuitableConstructorException()
    {
        Assertions.assertThrows(NoSuitableConstructorException.class,
                                () -> testObject.resolve(ClassConstructorsPrivate.class));
    }

    @Test
    public void resolve_WhenDependencySetterHasReturnType_ThenIncorrectDependencySetterException()
    {
        Assertions.assertThrows(IncorrectDependencySetterException.class,
                                () -> testObject.resolve(ClassSettersIncorrect1.class));
    }

    @Test
    public void resolve_WhenDependencySetterHasNoParameters_ThenIncorrectDependencySetterException()
    {
        Assertions.assertThrows(IncorrectDependencySetterException.class,
                                () -> testObject.resolve(ClassSettersIncorrect2.class));
    }

    @Test
    public void resolve_WhenDependencySetterNameDoesNotStartWithSet_ThenIncorrectDependencySetterException()
    {
        Assertions.assertThrows(IncorrectDependencySetterException.class,
                                () -> testObject.resolve(ClassSettersIncorrect3.class));
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBasicObject());
        Assertions.assertTrue(result instanceof ClassSettersSingle);
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBasicObject());
        Assertions.assertTrue(result instanceof ClassSettersConstructor);
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBasicObject());
        Assertions.assertNotNull(result.getFirstObject());
        Assertions.assertNotNull(result.getSecondObject());
        Assertions.assertNotNull(result.getFirstObject().getObject());
        Assertions.assertNotNull(result.getSecondObject().getString());
        Assertions.assertEquals(string, result.getSecondObject().getString());
        Assertions.assertTrue(result instanceof ClassBasicsComplexDependency);
    }

    @Test
    public void resolve_WhenDoubleDependencySetter_ThenIncorrectDependencySetterException()
    {
        testObject.registerType(InterfaceSettersDouble.class, ClassSettersDouble.class);

        Assertions.assertThrows(IncorrectDependencySetterException.class,
                                () -> testObject.resolve(InterfaceSettersDouble.class));
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBasicObject());
        Assertions.assertNotNull(result.getStringObject());
        Assertions.assertNotNull(result.getStringObject().getString());
        Assertions.assertEquals(string, result.getStringObject().getString());
        Assertions.assertTrue(result instanceof ClassSettersMultiple);
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
            Assertions.fail(String.format("An instance of %s was thrown.", e.getClass().getName()));
        }

        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertTrue(result1 instanceof ClassRegisterInterface);
        Assertions.assertTrue(result2 instanceof ClassRegisterInterface);
        Assertions.assertNotSame(result1, result2);
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
            Assertions.fail(String.format("An instance of %s was thrown.", e.getClass().getName()));
        }

        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertTrue(result2 instanceof ClassRegisterBase);
        Assertions.assertTrue(result1 instanceof ClassRegisterBase);
        Assertions.assertNotSame(result1, result2);
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
            Assertions.fail(String.format("An instance of %s was thrown.", e.getClass().getName()));
        }

        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertTrue(result1 instanceof ClassRegisterDerived);
        Assertions.assertTrue(result2 instanceof ClassRegisterDerived);
        Assertions.assertNotSame(result1, result2);
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
            Assertions.fail(String.format("An instance of %s was thrown.", e.getClass().getName()));
        }

        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertNotSame(result1, result2);
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
            Assertions.fail(String.format("An instance of %s was thrown.", e.getClass().getName()));
        }

        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertNotSame(result1, result2);
    }

    @Test
    public void resolve_WhenSelfAnnotatedInterface_ThenAbstractTypeException()
    {
        Assertions.assertThrows(AbstractTypeException.class,
                                () -> testObject.resolve(InterfaceRegisterSelfIncorrect.class));
    }

    @Test
    public void resolve_WhenSelfAnnotatedAbstractClass_ThenAbstractTypeException()
    {
        Assertions.assertThrows(AbstractTypeException.class,
                                () -> testObject.resolve(ClassRegisterSelfAbstractIncorrect.class));
    }

    @Test
    public void resolve_WhenAnnotatedAbstractClassAsItself_ThenAbstractTypeException()
    {
        Assertions.assertThrows(AbstractTypeException.class,
                                () -> testObject.resolve(ClassRegisterAbstractIncorrect.class));
    }

    @Test
    public void resolve_WhenInterfaceAnnotatedClassNotImplementing_ThenNotDerivedTypeException()
    {
        Assertions.assertThrows(NotDerivedTypeException.class,
                                () -> testObject.resolve(InterfaceRegisterIncorrect.class));
    }

    @Test
    public void resolve_WhenAnnotatedClassRegistersInterface_ThenNotDerivedTypeException()
    {
        Assertions.assertThrows(NotDerivedTypeException.class,
                                () -> testObject.resolve(ClassRegisterInterfaceIncorrect.class));
    }

    @Test
    public void resolve_WhenAnnotatedClassRegistersNotDerivedClass_ThenNotDerivedTypeException()
    {
        Assertions.assertThrows(NotDerivedTypeException.class,
                                () -> testObject.resolve(ClassRegisterIncorrect1.class));
    }

    @Test
    public void resolve_WhenAnnotatedClassRegistersAbstractSuperclass_ThenNotDerivedTypeException()
    {
        Assertions.assertThrows(NotDerivedTypeException.class,
                                () -> testObject.resolve(ClassRegisterIncorrect2.class));
    }

    @Test
    public void resolve_WhenAnnotatedClassRegistersAbstractConcreteSuperclass_ThenNotDerivedTypeException()
    {
        Assertions.assertThrows(NotDerivedTypeException.class,
                                () -> testObject.resolve(ClassRegisterIncorrect3.class));
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
            Assertions.fail(String.format("An instance of %s was thrown.", e.getClass().getName()));
        }

        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertTrue(result1 instanceof ClassRegisterSingletonBase);
        Assertions.assertTrue(result2 instanceof ClassRegisterSingletonBase);
        Assertions.assertSame(result1, result2);
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
            Assertions.fail(String.format("An instance of %s was thrown.", e.getClass().getName()));
        }

        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertTrue(result1 instanceof ClassRegisterSingletonDerived);
        Assertions.assertTrue(result2 instanceof ClassRegisterSingletonDerived);
        Assertions.assertSame(result1, result2);
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
            Assertions.fail(String.format("An instance of %s was thrown.", e.getClass().getName()));
        }

        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertSame(result1, result2);
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
            Assertions.fail(String.format("An instance of %s was thrown.", e.getClass().getName()));
        }

        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertSame(result1, result2);
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(instance);
        Assertions.assertNotNull(result.getBasicObject());
        Assertions.assertNotNull(instance.getBasicObject());
        Assertions.assertSame(instance, result);
    }

    @Test
    public void buildUp_WhenDoubleDependencySetter_ThenIncorrectDependencySetterException()
    {
        InterfaceSettersDouble instance = new ClassSettersDouble();

        Assertions.assertThrows(IncorrectDependencySetterException.class,
                                () -> testObject.buildUp(instance));
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
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(instance);
        Assertions.assertNotNull(result.getBasicObject());
        Assertions.assertNotNull(instance.getBasicObject());
        Assertions.assertNotNull(result.getStringObject());
        Assertions.assertNotNull(instance.getStringObject());
        Assertions.assertNotNull(result.getStringObject().getString());
        Assertions.assertNotNull(instance.getStringObject().getString());
        Assertions.assertEquals(string, result.getStringObject().getString());
        Assertions.assertEquals(string, instance.getStringObject().getString());
        Assertions.assertSame(instance, result);
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
        Assertions.assertEquals(string, result.getSecondObject().getString());
        Assertions.assertEquals(string, instance.getSecondObject().getString());
        Assertions.assertSame(instance, result);
    }

    // endregion
}
