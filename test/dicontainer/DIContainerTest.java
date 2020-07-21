package dicontainer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dicontainer.auxiliary.basic.*;
import dicontainer.auxiliary.constructor.*;
import dicontainer.auxiliary.diamond.ClassDiamondLeft;
import dicontainer.auxiliary.diamond.ClassDiamondTop;
import dicontainer.auxiliary.diamond.InterfaceDiamondLeft;
import dicontainer.auxiliary.diamond.InterfaceDiamondTop;
import dicontainer.auxiliary.setter.*;
import dicontainer.exception.AbstractTypeException;
import dicontainer.exception.IncorrectDependencySetterException;
import dicontainer.exception.NullInstanceException;
import dicontainer.resolver.MultipleAnnotatedConstructorsException;
import dicontainer.resolver.NoSuitableConstructorException;

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
        testObject.registerType(ClassConstructorDefault.class);

        ClassConstructorDefault result1 = testObject.resolve(ClassConstructorDefault.class);
        ClassConstructorDefault result2 = testObject.resolve(ClassConstructorDefault.class);

        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertNotSame(result1, result2);
    }

    @Test
    public void register_WhenSingleClassAsSingleton_ThenSameInstance()
    {
        testObject.registerType(ClassConstructorDefault.class, ConstructionPolicy.SINGLETON);

        ClassConstructorDefault result1 = testObject.resolve(ClassConstructorDefault.class);
        ClassConstructorDefault result2 = testObject.resolve(ClassConstructorDefault.class);

        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertSame(result1, result2);
    }

    @Test
    public void register_WhenSingleClassChangesSingleton_ThenChangesInstances()
    {
        testObject.registerType(ClassConstructorDefault.class, ConstructionPolicy.SINGLETON);

        ClassConstructorDefault result11 = testObject.resolve(ClassConstructorDefault.class);
        ClassConstructorDefault result12 = testObject.resolve(ClassConstructorDefault.class);

        Assertions.assertNotNull(result11);
        Assertions.assertNotNull(result12);
        Assertions.assertSame(result11, result12);

        testObject.registerType(ClassConstructorDefault.class, ConstructionPolicy.CONSTRUCT);

        ClassConstructorDefault result21 = testObject.resolve(ClassConstructorDefault.class);
        ClassConstructorDefault result22 = testObject.resolve(ClassConstructorDefault.class);

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
    // region registerType [inheritance]

    @Test
    public void register_WhenInheritanceFromInterface_ThenDifferentInstances()
    {
        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class);

        InterfaceBasic result1 = testObject.resolve(InterfaceBasic.class);
        InterfaceBasic result2 = testObject.resolve(InterfaceBasic.class);

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

        InterfaceBasic result1 = testObject.resolve(InterfaceBasic.class);
        InterfaceBasic result2 = testObject.resolve(InterfaceBasic.class);

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

        InterfaceBasic result11 = testObject.resolve(InterfaceBasic.class);
        InterfaceBasic result12 = testObject.resolve(InterfaceBasic.class);

        Assertions.assertNotNull(result11);
        Assertions.assertNotNull(result12);
        Assertions.assertSame(result11, result12);
        Assertions.assertTrue(result11 instanceof ClassConstructorDefault);
        Assertions.assertTrue(result12 instanceof ClassConstructorDefault);

        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class,
                                ConstructionPolicy.CONSTRUCT);

        InterfaceBasic result21 = testObject.resolve(InterfaceBasic.class);
        InterfaceBasic result22 = testObject.resolve(InterfaceBasic.class);

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

        InterfaceBasic result1 = testObject.resolve(InterfaceBasic.class);

        Assertions.assertNotNull(result1);
        Assertions.assertTrue(result1 instanceof ClassConstructorDefault);

        testObject.registerType(InterfaceBasic.class,
                                ClassConstructorDefaultAndParameterized.class);

        InterfaceBasic result3 = testObject.resolve(InterfaceBasic.class);

        Assertions.assertNotNull(result3);
        Assertions.assertTrue(result3 instanceof ClassConstructorDefaultAndParameterized);
    }

    @Test
    public void register_WhenInheritanceFromAbstractClass_ThenInstanceIsDerived()
    {
        testObject.registerType(ClassBasicAbstract.class, ClassBasicInheritsFromAbstract.class);

        ClassBasicAbstract result = testObject.resolve(ClassBasicAbstract.class);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof ClassBasicInheritsFromAbstract);
    }

    @Test
    public void register_WhenInheritanceFromConcreteClass_ThenInstanceIsDerived()
    {
        testObject.registerType(ClassConstructorParameterized.class,
                                ClassConstructorSuperParameterized.class);

        ClassConstructorParameterized result =
                testObject.resolve(ClassConstructorParameterized.class);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof ClassConstructorSuperParameterized);
    }

    @Test
    public void register_WhenTwoStepsOfHierarchy_ThenInstanceIsDerived()
    {
        testObject.registerType(InterfaceBasic.class, ClassBasicAbstract.class)
                  .registerType(ClassBasicAbstract.class, ClassBasicInheritsFromAbstract.class);

        InterfaceBasic result = testObject.resolve(InterfaceBasic.class);

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

        InterfaceBasic result = testObject.resolve(InterfaceBasic.class);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof ClassConstructorDefault);
        Assertions.assertSame(obj, result);
    }

    @Test
    public void registerInstance_WhenAbstractClass_ThenRegisteredInstance()
    {
        ClassBasicInheritsFromAbstract obj = new ClassBasicInheritsFromAbstract();

        testObject.registerInstance(ClassBasicAbstract.class, obj);

        ClassBasicAbstract result = testObject.resolve(ClassBasicAbstract.class);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof ClassBasicInheritsFromAbstract);
        Assertions.assertSame(obj, result);
    }

    @Test
    public void registerInstance_WhenSameConcreteClass_ThenRegisteredInstance()
    {
        ClassConstructorDefaultAndParameterized obj = new ClassConstructorDefaultAndParameterized();

        testObject.registerInstance(ClassConstructorDefaultAndParameterized.class, obj);

        ClassConstructorDefaultAndParameterized result =
                testObject.resolve(ClassConstructorDefaultAndParameterized.class);

        Assertions.assertNotNull(result);
        Assertions.assertSame(obj, result);
        Assertions.assertEquals(obj.getText(), result.getText());
    }

    @Test
    public void registerInstance_WhenDerivedConcreteClass_ThenRegisteredInstance()
    {
        ClassConstructorSuperParameterized obj = new ClassConstructorSuperParameterized();

        testObject.registerInstance(ClassConstructorParameterized.class, obj);

        ClassConstructorParameterized result =
                testObject.resolve(ClassConstructorParameterized.class);

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
                                () -> testObject.resolve(ClassSetterIncorrectReturnType.class));
    }

    @Test
    public void resolve_WhenDependencySetterHasNoParameters_ThenIncorrectDependencySetterException()
    {
        Assertions.assertThrows(IncorrectDependencySetterException.class,
                                () -> testObject.resolve(ClassSetterWithoutParameters.class));
    }

    @Test
    public void resolve_WhenDependencySetterNameDoesNotStartWithSet_ThenIncorrectDependencySetterException()
    {
        Assertions.assertThrows(IncorrectDependencySetterException.class,
                                () -> testObject.resolve(ClassSetterIncorrectName.class));
    }

    @Test
    public void resolve_WhenDependencySetterOnly_ThenInstanceIsResolved()
    {
        testObject.registerType(InterfaceSetter.class, ClassSetterSingle.class)
                  .registerType(InterfaceBasic.class, ClassConstructorDefault.class);

        InterfaceSetter result = testObject.resolve(InterfaceSetter.class);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBasicObject());
        Assertions.assertTrue(result instanceof ClassSetterSingle);
    }

    @Test
    public void resolve_WhenDependencySetterAndConstructor_ThenInstanceIsResolved()
    {
        testObject.registerType(InterfaceSetter.class, ClassSetterConstructor.class)
                  .registerType(InterfaceBasic.class, ClassConstructorDefault.class);

        InterfaceSetter result = testObject.resolve(InterfaceSetter.class);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBasicObject());
        Assertions.assertTrue(result instanceof ClassSetterConstructor);
    }

    @Test
    public void resolve_WhenDoubleDependencySetter_ThenIncorrectDependencySetterException()
    {
        testObject.registerType(InterfaceSetterMultipleParameters.class,
                                ClassSetterMultipleParameters.class);

        Assertions.assertThrows(IncorrectDependencySetterException.class,
                                () -> testObject.resolve(InterfaceSetterMultipleParameters.class));
    }

    @Test
    public void resolve_WhenMultipleDependencySetters_ThenInstanceIsResolved()
    {
        String string = "string";

        testObject.registerType(InterfaceSetterMultiple.class, ClassSetterMultiple.class)
                  .registerType(InterfaceBasic.class, ClassConstructorDefault.class)
                  .registerType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class);

        testObject.registerInstance(String.class, string);

        InterfaceSetterMultiple result = testObject.resolve(InterfaceSetterMultiple.class);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBasicObject());
        Assertions.assertNotNull(result.getStringObject());
        Assertions.assertNotNull(result.getStringObject().getString());
        Assertions.assertEquals(string, result.getStringObject().getString());
        Assertions.assertTrue(result instanceof ClassSetterMultiple);
    }

    // endregion
    // region buildUp

    @Test
    public void buildUp_WhenDependencySetterOnly_ThenInstanceIsBuiltUp()
    {
        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class);

        InterfaceSetter instance = new ClassSetterSingle();

        InterfaceSetter result = testObject.buildUp(instance);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(instance);
        Assertions.assertNotNull(result.getBasicObject());
        Assertions.assertNotNull(instance.getBasicObject());
        Assertions.assertSame(instance, result);
    }

    @Test
    public void buildUp_WhenDoubleDependencySetter_ThenIncorrectDependencySetterException()
    {
        InterfaceSetterMultipleParameters instance = new ClassSetterMultipleParameters();

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
        InterfaceSetterMultiple result = testObject.buildUp(instance);

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

        InterfaceDiamondLeft diamond1 = testObject.resolve(InterfaceDiamondLeft.class);
        InterfaceBasicStringGetter withString =
                testObject.resolve(InterfaceBasicStringGetter.class);

        InterfaceBasicComplexDependency instance =
                new ClassBasicComplexDependency(diamond1, withString);
        InterfaceBasicComplexDependency result = testObject.buildUp(instance);

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
