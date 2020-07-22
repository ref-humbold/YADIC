package dicontainer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import dicontainer.auxiliary.basic.*;
import dicontainer.auxiliary.constructor.*;
import dicontainer.auxiliary.diamond.ClassDiamondLeft;
import dicontainer.auxiliary.diamond.ClassDiamondTop;
import dicontainer.auxiliary.diamond.InterfaceDiamondLeft;
import dicontainer.auxiliary.diamond.InterfaceDiamondTop;
import dicontainer.auxiliary.setter.*;
import dicontainer.commons.NullInstanceException;
import dicontainer.dictionary.exception.AbstractTypeException;
import dicontainer.resolver.exception.IncorrectDependencySetterException;
import dicontainer.resolver.exception.MultipleAnnotatedConstructorsException;
import dicontainer.resolver.exception.NoSuitableConstructorException;

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
        // given
        testObject.registerType(ClassConstructorDefault.class);
        // when
        ClassConstructorDefault result1 = testObject.resolve(ClassConstructorDefault.class);
        ClassConstructorDefault result2 = testObject.resolve(ClassConstructorDefault.class);
        // then
        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertNotSame(result1, result2);
    }

    @Test
    public void register_WhenSingleClassAsSingleton_ThenSameInstance()
    {
        // given
        testObject.registerType(ClassConstructorDefault.class, ConstructionPolicy.SINGLETON);
        // when
        ClassConstructorDefault result1 = testObject.resolve(ClassConstructorDefault.class);
        ClassConstructorDefault result2 = testObject.resolve(ClassConstructorDefault.class);
        // then
        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertSame(result1, result2);
    }

    @Test
    public void register_WhenSingleClassChangesSingleton_ThenChangesInstances()
    {
        // given 1
        testObject.registerType(ClassConstructorDefault.class, ConstructionPolicy.SINGLETON);
        // when 1
        ClassConstructorDefault result11 = testObject.resolve(ClassConstructorDefault.class);
        ClassConstructorDefault result12 = testObject.resolve(ClassConstructorDefault.class);
        // then 1
        Assertions.assertNotNull(result11);
        Assertions.assertNotNull(result12);
        Assertions.assertSame(result11, result12);

        // given 2
        testObject.registerType(ClassConstructorDefault.class, ConstructionPolicy.CONSTRUCT);
        // when 2
        ClassConstructorDefault result21 = testObject.resolve(ClassConstructorDefault.class);
        ClassConstructorDefault result22 = testObject.resolve(ClassConstructorDefault.class);
        // then 2
        Assertions.assertNotNull(result21);
        Assertions.assertNotNull(result22);
        Assertions.assertNotSame(result21, result22);
    }

    @Test
    public void register_WhenSingleClassIsInterface_ThenAbstractTypeException()
    {
        // when
        Executable executable = () -> testObject.registerType(InterfaceBasic.class);
        // then
        Assertions.assertThrows(AbstractTypeException.class, executable);
    }

    @Test
    public void register_WhenSingleClassIsAbstractClass_ThenAbstractTypeException()
    {
        // when
        Executable executable = () -> testObject.registerType(ClassBasicAbstract.class);
        // then
        Assertions.assertThrows(AbstractTypeException.class, executable);
    }

    // endregion
    // region registerType [inheritance]

    @Test
    public void register_WhenInheritanceFromInterface_ThenDifferentInstances()
    {
        // given
        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class);
        // when
        InterfaceBasic result1 = testObject.resolve(InterfaceBasic.class);
        InterfaceBasic result2 = testObject.resolve(InterfaceBasic.class);
        // then
        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertNotSame(result1, result2);
        Assertions.assertTrue(result1 instanceof ClassConstructorDefault);
        Assertions.assertTrue(result2 instanceof ClassConstructorDefault);
    }

    @Test
    public void register_WhenInheritanceFromInterfaceAsSingleton_ThenSameInstances()
    {
        // given
        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class,
                                ConstructionPolicy.SINGLETON);
        // when
        InterfaceBasic result1 = testObject.resolve(InterfaceBasic.class);
        InterfaceBasic result2 = testObject.resolve(InterfaceBasic.class);
        // then
        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertSame(result1, result2);
        Assertions.assertTrue(result1 instanceof ClassConstructorDefault);
        Assertions.assertTrue(result2 instanceof ClassConstructorDefault);
    }

    @Test
    public void register_WhenInheritanceFromInterfaceChangesSingleton_ThenChangeInstances()
    {
        // given 1
        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class,
                                ConstructionPolicy.SINGLETON);
        // when 1
        InterfaceBasic result11 = testObject.resolve(InterfaceBasic.class);
        InterfaceBasic result12 = testObject.resolve(InterfaceBasic.class);
        // then 1
        Assertions.assertNotNull(result11);
        Assertions.assertNotNull(result12);
        Assertions.assertSame(result11, result12);
        Assertions.assertTrue(result11 instanceof ClassConstructorDefault);
        Assertions.assertTrue(result12 instanceof ClassConstructorDefault);

        // given 2
        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class,
                                ConstructionPolicy.CONSTRUCT);
        // when 2
        InterfaceBasic result21 = testObject.resolve(InterfaceBasic.class);
        InterfaceBasic result22 = testObject.resolve(InterfaceBasic.class);
        // then 2
        Assertions.assertNotNull(result21);
        Assertions.assertNotNull(result22);
        Assertions.assertNotSame(result21, result22);
        Assertions.assertTrue(result21 instanceof ClassConstructorDefault);
        Assertions.assertTrue(result22 instanceof ClassConstructorDefault);
    }

    @Test
    public void register_WhenInheritanceFromInterfaceChangesClass_ThenInstanceIsDerived()
    {
        // given 1
        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class);
        // when 1
        InterfaceBasic result1 = testObject.resolve(InterfaceBasic.class);
        // then 1
        Assertions.assertNotNull(result1);
        Assertions.assertTrue(result1 instanceof ClassConstructorDefault);

        // given 2
        testObject.registerType(InterfaceBasic.class,
                                ClassConstructorDefaultAndParameterized.class);
        // when 2
        InterfaceBasic result3 = testObject.resolve(InterfaceBasic.class);
        // then 2
        Assertions.assertNotNull(result3);
        Assertions.assertTrue(result3 instanceof ClassConstructorDefaultAndParameterized);
    }

    @Test
    public void register_WhenInheritanceFromAbstractClass_ThenInstanceIsDerived()
    {
        // given
        testObject.registerType(ClassBasicAbstract.class, ClassBasicInheritsFromAbstract.class);
        // when
        ClassBasicAbstract result = testObject.resolve(ClassBasicAbstract.class);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof ClassBasicInheritsFromAbstract);
    }

    @Test
    public void register_WhenInheritanceFromConcreteClass_ThenInstanceIsDerived()
    {
        // given
        testObject.registerType(ClassConstructorParameterized.class,
                                ClassConstructorSuperParameterized.class);
        // when
        ClassConstructorParameterized result =
                testObject.resolve(ClassConstructorParameterized.class);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof ClassConstructorSuperParameterized);
    }

    @Test
    public void register_WhenTwoStepsOfHierarchy_ThenInstanceIsDerived()
    {
        // given
        testObject.registerType(InterfaceBasic.class, ClassBasicAbstract.class)
                  .registerType(ClassBasicAbstract.class, ClassBasicInheritsFromAbstract.class);
        // when
        InterfaceBasic result = testObject.resolve(InterfaceBasic.class);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof ClassBasicInheritsFromAbstract);
    }

    // endregion
    // region registerInstance

    @Test
    public void registerInstance_WhenInterface_ThenRegisteredInstance()
    {
        // given
        ClassConstructorDefault instance = new ClassConstructorDefault();

        testObject.registerInstance(InterfaceBasic.class, instance);
        // when
        InterfaceBasic result = testObject.resolve(InterfaceBasic.class);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof ClassConstructorDefault);
        Assertions.assertSame(instance, result);
    }

    @Test
    public void registerInstance_WhenAbstractClass_ThenRegisteredInstance()
    {
        // given
        ClassBasicInheritsFromAbstract instance = new ClassBasicInheritsFromAbstract();

        testObject.registerInstance(ClassBasicAbstract.class, instance);
        // when
        ClassBasicAbstract result = testObject.resolve(ClassBasicAbstract.class);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof ClassBasicInheritsFromAbstract);
        Assertions.assertSame(instance, result);
    }

    @Test
    public void registerInstance_WhenSameConcreteClass_ThenRegisteredInstance()
    {
        // given
        ClassConstructorDefaultAndParameterized instance =
                new ClassConstructorDefaultAndParameterized();

        testObject.registerInstance(ClassConstructorDefaultAndParameterized.class, instance);
        // when
        ClassConstructorDefaultAndParameterized result =
                testObject.resolve(ClassConstructorDefaultAndParameterized.class);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertSame(instance, result);
        Assertions.assertEquals(instance.getText(), result.getText());
    }

    @Test
    public void registerInstance_WhenDerivedConcreteClass_ThenRegisteredInstance()
    {
        // given
        ClassConstructorSuperParameterized instance = new ClassConstructorSuperParameterized();

        testObject.registerInstance(ClassConstructorParameterized.class, instance);
        // when
        ClassConstructorParameterized result =
                testObject.resolve(ClassConstructorParameterized.class);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertSame(instance, result);
        Assertions.assertTrue(result instanceof ClassConstructorSuperParameterized);
        Assertions.assertEquals(instance.getNumber(), result.getNumber());
    }

    @Test
    public void registerInstance_WhenInstanceIsNull_ThenNullInstanceException()
    {
        // when
        Executable executable =
                () -> testObject.registerInstance(ClassConstructorDefaultAndParameterized.class,
                                                  null);
        // then
        Assertions.assertThrows(NullInstanceException.class, executable);
    }

    // endregion
    // region resolve (@Dependency)

    @Test
    public void resolve_WhenMultipleAnnotatedConstructors_ThenMultipleAnnotatedConstructorsException()
    {
        // when
        Executable executable = () -> testObject.resolve(ClassConstructorMultipleAnnotated.class);
        // then
        Assertions.assertThrows(MultipleAnnotatedConstructorsException.class, executable);
    }

    @Test
    public void resolve_WhenNoPublicConstructors_ThenNoSuitableConstructorException()
    {
        // when
        Executable executable = () -> testObject.resolve(ClassConstructorPrivate.class);
        // then
        Assertions.assertThrows(NoSuitableConstructorException.class, executable);
    }

    @Test
    public void resolve_WhenDependencySetterHasReturnType_ThenIncorrectDependencySetterException()
    {
        // when
        Executable executable = () -> testObject.resolve(ClassSetterIncorrectReturnType.class);
        // then
        Assertions.assertThrows(IncorrectDependencySetterException.class, executable);
    }

    @Test
    public void resolve_WhenDependencySetterHasNoParameters_ThenIncorrectDependencySetterException()
    {
        // when
        Executable executable = () -> testObject.resolve(ClassSetterWithoutParameters.class);
        // then
        Assertions.assertThrows(IncorrectDependencySetterException.class, executable);
    }

    @Test
    public void resolve_WhenDependencySetterNameDoesNotStartWithSet_ThenIncorrectDependencySetterException()
    {
        // when
        Executable executable = () -> testObject.resolve(ClassSetterIncorrectName.class);
        // then
        Assertions.assertThrows(IncorrectDependencySetterException.class, executable);
    }

    @Test
    public void resolve_WhenDependencySetterOnly_ThenInstanceIsResolved()
    {
        // given
        testObject.registerType(InterfaceSetter.class, ClassSetterSingle.class)
                  .registerType(InterfaceBasic.class, ClassConstructorDefault.class);
        // when
        InterfaceSetter result = testObject.resolve(InterfaceSetter.class);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBasicObject());
        Assertions.assertTrue(result instanceof ClassSetterSingle);
    }

    @Test
    public void resolve_WhenDependencySetterAndConstructor_ThenInstanceIsResolved()
    {
        // given
        testObject.registerType(InterfaceSetter.class, ClassSetterConstructor.class)
                  .registerType(InterfaceBasic.class, ClassConstructorDefault.class);
        // when
        InterfaceSetter result = testObject.resolve(InterfaceSetter.class);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBasicObject());
        Assertions.assertTrue(result instanceof ClassSetterConstructor);
    }

    @Test
    public void resolve_WhenDoubleDependencySetter_ThenIncorrectDependencySetterException()
    {
        // given
        testObject.registerType(InterfaceSetterMultipleParameters.class,
                                ClassSetterMultipleParameters.class);
        // when
        Executable executable = () -> testObject.resolve(InterfaceSetterMultipleParameters.class);
        // then
        Assertions.assertThrows(IncorrectDependencySetterException.class, executable);
    }

    @Test
    public void resolve_WhenMultipleDependencySetters_ThenInstanceIsResolved()
    {
        // given
        String string = "string";

        testObject.registerInstance(String.class, string)
                  .registerType(InterfaceSetterMultiple.class, ClassSetterMultiple.class)
                  .registerType(InterfaceBasic.class, ClassConstructorDefault.class)
                  .registerType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class);
        // when
        InterfaceSetterMultiple result = testObject.resolve(InterfaceSetterMultiple.class);
        // then
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
        // given
        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class);

        InterfaceSetter instance = new ClassSetterSingle();
        // when
        InterfaceSetter result = testObject.buildUp(instance);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(instance);
        Assertions.assertNotNull(result.getBasicObject());
        Assertions.assertNotNull(instance.getBasicObject());
        Assertions.assertSame(instance, result);
    }

    @Test
    public void buildUp_WhenDependencySetterHasMultipleParameters_ThenIncorrectDependencySetterException()
    {
        // when
        Executable executable = () -> testObject.buildUp(
                (InterfaceSetterMultipleParameters)new ClassSetterMultipleParameters());
        // then
        Assertions.assertThrows(IncorrectDependencySetterException.class, executable);
    }

    @Test
    public void buildUp_WhenMultipleDependencySetters_ThenInstanceIsBuiltUp()
    {
        // given
        String string = "string";
        InterfaceSetterMultiple instance = new ClassSetterMultiple();

        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class)
                  .registerType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class)
                  .registerInstance(String.class, string);
        // when
        InterfaceSetterMultiple result = testObject.buildUp(instance);
        // then
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
        // given
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
        // when
        InterfaceBasicComplexDependency result = testObject.buildUp(instance);
        // then
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
