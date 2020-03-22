package dicontainer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dicontainer.auxiliary.basic.*;
import dicontainer.auxiliary.circular.*;
import dicontainer.auxiliary.constructor.*;
import dicontainer.auxiliary.diamond.*;
import dicontainer.auxiliary.register.*;
import dicontainer.auxiliary.setter.*;
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
            testObject.registerType(ClassConstructorDefault.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        ClassConstructorDefault result1 = null;
        ClassConstructorDefault result2 = null;

        try
        {
            result1 = testObject.resolve(ClassConstructorDefault.class);
            result2 = testObject.resolve(ClassConstructorDefault.class);
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
            testObject.registerType(ClassConstructorDefault.class, ConstructionPolicy.SINGLETON);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        ClassConstructorDefault result1 = null;
        ClassConstructorDefault result2 = null;

        try
        {
            result1 = testObject.resolve(ClassConstructorDefault.class);
            result2 = testObject.resolve(ClassConstructorDefault.class);
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
            testObject.registerType(ClassConstructorDefault.class, ConstructionPolicy.SINGLETON);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        ClassConstructorDefault result11 = null;
        ClassConstructorDefault result12 = null;

        try
        {
            result11 = testObject.resolve(ClassConstructorDefault.class);
            result12 = testObject.resolve(ClassConstructorDefault.class);
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
            testObject.registerType(ClassConstructorDefault.class, ConstructionPolicy.CONSTRUCT);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        ClassConstructorDefault result21 = null;
        ClassConstructorDefault result22 = null;

        try
        {
            result21 = testObject.resolve(ClassConstructorDefault.class);
            result22 = testObject.resolve(ClassConstructorDefault.class);
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
                                () -> testObject.registerType(InterfaceBasic.class));
    }

    @Test
    public void register_WhenSingleClassIsAbstractClass_ThenAbstractTypeException()
    {
        Assertions.assertThrows(AbstractTypeException.class,
                                () -> testObject.registerType(ClassBasicAbstract.class));
    }

    // endregion
    // region registerType (inheritance)

    @Test
    public void register_WhenInheritanceFromInterface_ThenDifferentInstances()
    {
        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class);

        InterfaceBasic result1 = null;
        InterfaceBasic result2 = null;

        try
        {
            result1 = testObject.resolve(InterfaceBasic.class);
            result2 = testObject.resolve(InterfaceBasic.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertNotSame(result1, result2);
        Assertions.assertTrue(result1 instanceof ClassConstructorDefault);
        Assertions.assertTrue(result2 instanceof ClassConstructorDefault);
    }

    @Test
    public void register_WhenInheritanceFromInterfaceAsSingleton_ThenSameInstances()
    {
        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class,
                                ConstructionPolicy.SINGLETON);

        InterfaceBasic result1 = null;
        InterfaceBasic result2 = null;

        try
        {
            result1 = testObject.resolve(InterfaceBasic.class);
            result2 = testObject.resolve(InterfaceBasic.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertSame(result1, result2);
        Assertions.assertTrue(result1 instanceof ClassConstructorDefault);
        Assertions.assertTrue(result2 instanceof ClassConstructorDefault);
    }

    @Test
    public void register_WhenInheritanceFromInterfaceChangesSingleton_ThenChangeInstances()
    {
        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class,
                                ConstructionPolicy.SINGLETON);

        InterfaceBasic result11 = null;
        InterfaceBasic result12 = null;

        try
        {
            result11 = testObject.resolve(InterfaceBasic.class);
            result12 = testObject.resolve(InterfaceBasic.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result11);
        Assertions.assertNotNull(result12);
        Assertions.assertSame(result11, result12);
        Assertions.assertTrue(result11 instanceof ClassConstructorDefault);
        Assertions.assertTrue(result12 instanceof ClassConstructorDefault);

        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class,
                                ConstructionPolicy.CONSTRUCT);

        InterfaceBasic result21 = null;
        InterfaceBasic result22 = null;

        try
        {
            result21 = testObject.resolve(InterfaceBasic.class);
            result22 = testObject.resolve(InterfaceBasic.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result21);
        Assertions.assertNotNull(result22);
        Assertions.assertNotSame(result21, result22);
        Assertions.assertTrue(result21 instanceof ClassConstructorDefault);
        Assertions.assertTrue(result22 instanceof ClassConstructorDefault);
    }

    @Test
    public void register_WhenInheritanceFromInterfaceChangesClass_ThenInstanceIsDerived()
    {
        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class);

        InterfaceBasic result1 = null;

        try
        {
            result1 = testObject.resolve(InterfaceBasic.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result1);
        Assertions.assertTrue(result1 instanceof ClassConstructorDefault);

        testObject.registerType(InterfaceBasic.class,
                                ClassConstructorDefaultAndParameterized.class);

        InterfaceBasic result3 = null;

        try
        {
            result3 = testObject.resolve(InterfaceBasic.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result3);
        Assertions.assertTrue(result3 instanceof ClassConstructorDefaultAndParameterized);
    }

    @Test
    public void register_WhenInheritanceFromAbstractClass_ThenInstanceIsDerived()
    {
        testObject.registerType(ClassBasicAbstract.class, ClassBasicInheritsFromAbstract.class);

        ClassBasicAbstract result = null;

        try
        {
            result = testObject.resolve(ClassBasicAbstract.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof ClassBasicInheritsFromAbstract);
    }

    @Test
    public void register_WhenInheritanceFromConcreteClass_ThenInstanceIsDerived()
    {
        testObject.registerType(ClassConstructorParameter.class,
                                ClassConstructorSuperParameterized.class);

        ClassConstructorParameter result = null;

        try
        {
            result = testObject.resolve(ClassConstructorParameter.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof ClassConstructorSuperParameterized);
    }

    @Test
    public void register_WhenTwoStepsOfHierarchy_ThenInstanceIsDerived()
    {
        testObject.registerType(InterfaceBasic.class, ClassBasicAbstract.class)
                  .registerType(ClassBasicAbstract.class, ClassBasicInheritsFromAbstract.class);

        InterfaceBasic result = null;

        try
        {
            result = testObject.resolve(InterfaceBasic.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof ClassBasicInheritsFromAbstract);
    }

    // endregion
    // region registerInstance

    @Test
    public void registerInstance_WhenInterface_ThenRegisteredInstance()
    {
        ClassConstructorDefault obj = new ClassConstructorDefault();

        testObject.registerInstance(InterfaceBasic.class, obj);

        InterfaceBasic result = null;

        try
        {
            result = testObject.resolve(InterfaceBasic.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof ClassConstructorDefault);
        Assertions.assertSame(obj, result);
    }

    @Test
    public void registerInstance_WhenAbstractClass_ThenRegisteredInstance()
    {
        ClassBasicInheritsFromAbstract obj = new ClassBasicInheritsFromAbstract();

        testObject.registerInstance(ClassBasicAbstract.class, obj);

        ClassBasicAbstract result = null;

        try
        {
            result = testObject.resolve(ClassBasicAbstract.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof ClassBasicInheritsFromAbstract);
        Assertions.assertSame(obj, result);
    }

    @Test
    public void registerInstance_WhenSameConcreteClass_ThenRegisteredInstance()
    {
        ClassConstructorDefaultAndParameterized obj = new ClassConstructorDefaultAndParameterized();

        testObject.registerInstance(ClassConstructorDefaultAndParameterized.class, obj);

        ClassConstructorDefaultAndParameterized result = null;

        try
        {
            result = testObject.resolve(ClassConstructorDefaultAndParameterized.class);
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
        ClassConstructorSuperParameterized obj = new ClassConstructorSuperParameterized();

        testObject.registerInstance(ClassConstructorParameter.class, obj);

        ClassConstructorParameter result = null;

        try
        {
            result = testObject.resolve(ClassConstructorParameter.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertSame(obj, result);
        Assertions.assertTrue(result instanceof ClassConstructorSuperParameterized);
        Assertions.assertEquals(obj.getNumber(), result.getNumber());
    }

    @Test
    public void registerInstance_WhenInstanceIsNull_ThenNullInstanceException()
    {
        Assertions.assertThrows(NullInstanceException.class, () -> testObject.registerInstance(
                ClassConstructorDefaultAndParameterized.class, null));
    }

    // endregion
    //region resolve (class)

    @Test
    public void resolve_WhenClassHasDefaultConstructorOnly_ThenInstanceIsResolved()
    {
        ClassConstructorDefault result = null;

        try
        {
            result = testObject.resolve(ClassConstructorDefault.class);
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
        ClassConstructorSuperParameterized result = null;

        try
        {
            result = testObject.resolve(ClassConstructorSuperParameterized.class);
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
        ClassBasicInheritsFromAbstract result = null;

        try
        {
            result = testObject.resolve(ClassBasicInheritsFromAbstract.class);
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
                                () -> testObject.resolve(ClassConstructorParameter.class));
    }

    @Test
    public void resolve_WhenClassHasParameterConstructorWithRegisteredPrimitiveParameter_ThenInstanceIsResolved()
    {
        int number = 10;

        testObject.registerInstance(int.class, number);

        ClassConstructorParameter result = null;

        try
        {
            result = testObject.resolve(ClassConstructorParameter.class);
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

        ClassConstructorParameter result = null;

        try
        {
            result = testObject.resolve(ClassConstructorParameter.class);
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
        ClassConstructorDefaultAndParameterized result = null;

        try
        {
            result = testObject.resolve(ClassConstructorDefaultAndParameterized.class);
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
                                () -> testObject.resolve(InterfaceBasic.class));
    }

    @Test
    public void resolve_WhenAbstractClass_ThenMissingDependenciesException()
    {
        Assertions.assertThrows(MissingDependenciesException.class,
                                () -> testObject.resolve(ClassBasicAbstract.class));
    }

    // endregion
    // region resolve (dependencies schemas)

    @Test
    public void resolve_WhenDependenciesWithRegisteredInstance_ThenInstanceIsResolved()
    {
        String string = "String";

        testObject.registerInstance(String.class, string)
                  .registerType(InterfaceBasic.class, ClassConstructorDefault.class)
                  .registerType(InterfaceDiamondLeft.class, ClassDiamondLeft.class)
                  .registerType(InterfaceDiamondTop.class, ClassDiamondTop.class)
                  .registerType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class)
                  .registerType(InterfaceBasicSimpleDependency.class,
                                ClassConstructorNotAnnotatedWithDependency.class);

        InterfaceBasicSimpleDependency result = null;

        try
        {
            result = testObject.resolve(InterfaceBasicSimpleDependency.class);
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
        Assertions.assertTrue(result instanceof ClassConstructorNotAnnotatedWithDependency);
    }

    @Test
    public void resolve_WhenDependenciesWithoutAnnotatedConstructorsWithAllDependencies_ThenInstanceIsResolved()
    {
        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class)
                  .registerType(InterfaceDiamondLeft.class, ClassDiamondLeft.class)
                  .registerType(InterfaceDiamondTop.class, ClassDiamondTop.class)
                  .registerType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class)
                  .registerType(InterfaceBasicSimpleDependency.class,
                                ClassConstructorNotAnnotatedWithDependency.class);

        InterfaceBasicSimpleDependency result = null;

        try
        {
            result = testObject.resolve(InterfaceBasicSimpleDependency.class);
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
        Assertions.assertTrue(result instanceof ClassConstructorNotAnnotatedWithDependency);
    }

    @Test
    public void resolve_WhenDependenciesWithoutAnnotatedConstructorsWithoutSomeDependencies_ThenInstanceIsResolved()
    {
        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class)
                  .registerType(InterfaceDiamondLeft.class, ClassDiamondLeft.class)
                  .registerType(InterfaceDiamondTop.class, ClassDiamondTop.class)
                  .registerType(InterfaceBasicSimpleDependency.class,
                                ClassConstructorNotAnnotatedWithDependency.class);

        InterfaceBasicSimpleDependency result = null;

        try
        {
            result = testObject.resolve(InterfaceBasicSimpleDependency.class);
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
        Assertions.assertTrue(result instanceof ClassConstructorNotAnnotatedWithDependency);
    }

    @Test
    public void resolve_WhenDependenciesWithAnnotatedConstructor_ThenInstanceIsResolved()
    {
        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class)
                  .registerType(InterfaceDiamondLeft.class, ClassDiamondLeft.class)
                  .registerType(InterfaceDiamondTop.class, ClassDiamondTop.class)
                  .registerType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class)
                  .registerType(InterfaceBasicSimpleDependency.class,
                                ClassConstructorAnnotatedWithDependency.class);

        InterfaceBasicSimpleDependency result = null;

        try
        {
            result = testObject.resolve(InterfaceBasicSimpleDependency.class);
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
        Assertions.assertTrue(result instanceof ClassConstructorAnnotatedWithDependency);
    }

    @Test
    public void resolve_WhenDependenciesWithAnnotatedConstructorWithoutSomeDependencies_ThenMissingDependenciesException()
    {
        testObject.registerType(InterfaceDiamondLeft.class, ClassDiamondLeft.class)
                  .registerType(InterfaceDiamondRight.class, ClassDiamondRight.class)
                  .registerType(InterfaceDiamondBottom.class, ClassDiamondBottom.class);

        Assertions.assertThrows(MissingDependenciesException.class,
                                () -> testObject.resolve(InterfaceDiamondBottom.class));
    }

    @Test
    public void resolve_WhenDiamondDependenciesWithoutSingleton_ThenInstanceIsResolved()
    {
        testObject.registerType(InterfaceDiamondLeft.class, ClassDiamondLeft.class)
                  .registerType(InterfaceDiamondRight.class, ClassDiamondRight.class)
                  .registerType(InterfaceDiamondBottom.class, ClassDiamondBottom.class)
                  .registerType(InterfaceDiamondTop.class, ClassDiamondTop.class);

        InterfaceDiamondBottom result = null;

        try
        {
            result = testObject.resolve(InterfaceDiamondBottom.class);
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
        Assertions.assertTrue(result instanceof ClassDiamondBottom);
    }

    @Test
    public void resolve_WhenDiamondDependenciesWithSingleton_ThenInstanceIsResolved()
    {
        testObject.registerType(InterfaceDiamondLeft.class, ClassDiamondLeft.class)
                  .registerType(InterfaceDiamondRight.class, ClassDiamondRight.class)
                  .registerType(InterfaceDiamondBottom.class, ClassDiamondBottom.class)
                  .registerType(InterfaceDiamondTop.class, ClassDiamondTop.class,
                                ConstructionPolicy.SINGLETON);

        InterfaceDiamondBottom result = null;

        try
        {
            result = testObject.resolve(InterfaceDiamondBottom.class);
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
        Assertions.assertTrue(result instanceof ClassDiamondBottom);
    }

    @Test
    public void resolve_WhenCircularDependencies_ThenCircularDependenciesException()
    {
        testObject.registerType(InterfaceCircularLeft.class, ClassCircularLeft.class)
                  .registerType(InterfaceCircularRight.class, ClassCircularRight.class);

        Assertions.assertThrows(CircularDependenciesException.class,
                                () -> testObject.resolve(InterfaceCircularRight.class));
    }

    @Test
    public void resolve_WhenCanOmitCircularDependencies_ThenInstanceIsResolved()
    {
        String string = "String";

        testObject.registerInstance(String.class, string)
                  .registerType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class)
                  .registerType(InterfaceCircularLeft.class, ClassCircularLeft.class)
                  .registerType(InterfaceCircularRight.class, ClassCircularRight.class)
                  .registerType(InterfaceCircularDependency.class, ClassCircularDependency.class);

        InterfaceCircularDependency result = null;

        try
        {
            result = testObject.resolve(InterfaceCircularDependency.class);
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
        Assertions.assertTrue(result instanceof ClassCircularDependency);
    }

    // endregion
    // region resolve (@Dependency)

    @Test
    public void resolve_WhenMultipleAnnotatedConstructors_ThenMultipleAnnotatedConstructorsException()
    {
        Assertions.assertThrows(MultipleAnnotatedConstructorsException.class,
                                () -> testObject.resolve(ClassConstructorMultipleAnnotated.class));
    }

    @Test
    public void resolve_WhenNoPublicConstructors_ThenNoSuitableConstructorException()
    {
        Assertions.assertThrows(NoSuitableConstructorException.class,
                                () -> testObject.resolve(ClassConstructorPrivate.class));
    }

    @Test
    public void resolve_WhenDependencySetterHasReturnType_ThenIncorrectDependencySetterException()
    {
        Assertions.assertThrows(IncorrectDependencySetterException.class,
                                () -> testObject.resolve(ClassSetterIncorrect1.class));
    }

    @Test
    public void resolve_WhenDependencySetterHasNoParameters_ThenIncorrectDependencySetterException()
    {
        Assertions.assertThrows(IncorrectDependencySetterException.class,
                                () -> testObject.resolve(ClassSetterIncorrect2.class));
    }

    @Test
    public void resolve_WhenDependencySetterNameDoesNotStartWithSet_ThenIncorrectDependencySetterException()
    {
        Assertions.assertThrows(IncorrectDependencySetterException.class,
                                () -> testObject.resolve(ClassSetterIncorrect3.class));
    }

    @Test
    public void resolve_WhenDependencySetterOnly_ThenInstanceIsResolved()
    {
        testObject.registerType(InterfaceSetter.class, ClassSetterSingle.class)
                  .registerType(InterfaceBasic.class, ClassConstructorDefault.class);

        InterfaceSetter result = null;

        try
        {
            result = testObject.resolve(InterfaceSetter.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBasicObject());
        Assertions.assertTrue(result instanceof ClassSetterSingle);
    }

    @Test
    public void resolve_WhenDependencySetterAndConstructor_ThenInstanceIsResolved()
    {
        testObject.registerType(InterfaceSetter.class, ClassSetterConstructor.class)
                  .registerType(InterfaceBasic.class, ClassConstructorDefault.class);

        InterfaceSetter result = null;

        try
        {
            result = testObject.resolve(InterfaceSetter.class);
        }
        catch(DIException e)
        {
            e.printStackTrace();
            Assertions.fail(String.format("An instance of %s was thrown", e.getClass().getName()));
        }

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBasicObject());
        Assertions.assertTrue(result instanceof ClassSetterConstructor);
    }

    @Test
    public void resolve_WhenComplexDependency_ThenInstanceIsResolved()
    {
        String string = "string";

        testObject.registerType(InterfaceBasicComplexDependency.class,
                                ClassBasicComplexDependency.class)
                  .registerType(InterfaceBasic.class, ClassConstructorDefault.class)
                  .registerType(InterfaceDiamondTop.class, ClassDiamondTop.class)
                  .registerType(InterfaceDiamondLeft.class, ClassDiamondLeft.class)
                  .registerType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class);

        testObject.registerInstance(String.class, string);

        InterfaceBasicComplexDependency result = null;

        try
        {
            result = testObject.resolve(InterfaceBasicComplexDependency.class);
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
        Assertions.assertTrue(result instanceof ClassBasicComplexDependency);
    }

    @Test
    public void resolve_WhenDoubleDependencySetter_ThenIncorrectDependencySetterException()
    {
        testObject.registerType(InterfaceSetterDouble.class, ClassSetterDouble.class);

        Assertions.assertThrows(IncorrectDependencySetterException.class,
                                () -> testObject.resolve(InterfaceSetterDouble.class));
    }

    @Test
    public void resolve_WhenMultipleDependencySetters_ThenInstanceIsResolved()
    {
        String string = "string";

        testObject.registerType(InterfaceSetterMultiple.class, ClassSetterMultiple.class)
                  .registerType(InterfaceBasic.class, ClassConstructorDefault.class)
                  .registerType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class);

        testObject.registerInstance(String.class, string);

        InterfaceSetterMultiple result = null;

        try
        {
            result = testObject.resolve(InterfaceSetterMultiple.class);
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
        Assertions.assertTrue(result instanceof ClassSetterMultiple);
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
        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class);

        InterfaceSetter instance = new ClassSetterSingle();
        InterfaceSetter result = null;

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
        InterfaceSetterDouble instance = new ClassSetterDouble();

        Assertions.assertThrows(IncorrectDependencySetterException.class,
                                () -> testObject.buildUp(instance));
    }

    @Test
    public void buildUp_WhenMultipleDependencySetters_ThenInstanceIsBuiltUp()
    {
        String string = "string";

        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class)
                  .registerType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class)
                  .registerInstance(String.class, string);

        InterfaceSetterMultiple instance = new ClassSetterMultiple();
        InterfaceSetterMultiple result = new ClassSetterMultiple();

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

        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class)
                  .registerType(InterfaceDiamondLeft.class, ClassDiamondLeft.class)
                  .registerType(InterfaceDiamondTop.class, ClassDiamondTop.class)
                  .registerType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class)
                  .registerInstance(String.class, string);

        InterfaceBasicComplexDependency instance = null;
        InterfaceBasicComplexDependency result = null;

        try
        {
            InterfaceDiamondLeft diamond1 = testObject.resolve(InterfaceDiamondLeft.class);
            InterfaceBasicStringGetter withString =
                    testObject.resolve(InterfaceBasicStringGetter.class);

            instance = new ClassBasicComplexDependency(diamond1, withString);
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
