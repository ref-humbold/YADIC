package dicontainer.resolver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import dicontainer.ConstructionPolicy;
import dicontainer.auxiliary.basic.*;
import dicontainer.auxiliary.circular.*;
import dicontainer.auxiliary.constructor.*;
import dicontainer.auxiliary.diamond.*;
import dicontainer.auxiliary.register.*;
import dicontainer.auxiliary.setter.*;
import dicontainer.dictionary.DIDictionary;
import dicontainer.exception.AbstractTypeException;
import dicontainer.exception.IncorrectDependencySetterException;
import dicontainer.exception.MissingDependenciesException;
import dicontainer.exception.NoInstanceCreatedException;
import dicontainer.exception.NotDerivedTypeException;

public class DIResolverTest
{
    private DIDictionary dictionary;
    private DIResolver testObject;

    @BeforeEach
    public void setUp()
    {
        dictionary = new DIDictionary();
        testObject = new DIResolver(dictionary);
    }

    @AfterEach
    public void tearDown()
    {
        testObject = null;
    }

    // region resolve [constructor]

    @Test
    public void resolve_WhenClassHasDefaultConstructorOnly_ThenInstanceIsResolved()
    {
        // when
        ClassConstructorDefault result = testObject.resolve(ClassConstructorDefault.class);
        // then
        Assertions.assertNotNull(result);
    }

    @Test
    public void resolve_WhenClassInheritsFromConcreteClass_ThenInstanceIsResolved()
    {
        // when
        ClassConstructorSuperParameterized result =
                testObject.resolve(ClassConstructorSuperParameterized.class);
        // then
        Assertions.assertNotNull(result);
    }

    @Test
    public void resolve_WhenClassInheritsFromAbstractClass_ThenInstanceIsResolved()
    {
        // when
        ClassBasicInheritsFromAbstract result =
                testObject.resolve(ClassBasicInheritsFromAbstract.class);
        // then
        Assertions.assertNotNull(result);
    }

    @Test
    public void resolve_WhenClassHasParameterConstructorWithoutRegisteredParameter_ThenMissingDependenciesException()
    {
        // when
        Executable executable = () -> testObject.resolve(ClassConstructorParameterized.class);
        // then
        Assertions.assertThrows(MissingDependenciesException.class, executable);
    }

    @Test
    public void resolve_WhenClassHasParameterConstructorWithRegisteredPrimitiveParameter_ThenInstanceIsResolved()
    {
        // given
        int number = 10;

        dictionary.addInstance(int.class, number);
        // when
        ClassConstructorParameterized result =
                testObject.resolve(ClassConstructorParameterized.class);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(number, result.getNumber());
    }

    @Test
    public void resolve_WhenClassHasPrimitiveParameterConstructorButRegisteredReferenceParameter_ThenMissingDependenciesException()
    {
        // given
        Integer number = 10;

        dictionary.addInstance(Integer.class, number);
        // when
        Executable executable = () -> testObject.resolve(ClassConstructorParameterized.class);
        // then
        Assertions.assertThrows(MissingDependenciesException.class, executable);
    }

    @Test
    public void resolve_WhenClassHasDefaultAndParameterConstructorWithoutRegisteredParameter_ThenInstanceIsResolved()
    {
        // when
        ClassConstructorDefaultAndParameterized result =
                testObject.resolve(ClassConstructorDefaultAndParameterized.class);
        // then
        Assertions.assertNotNull(result);
    }

    @Test
    public void resolve_WhenInterface_ThenMissingDependenciesException()
    {
        // when
        Executable executable = () -> testObject.resolve(InterfaceBasic.class);
        // then
        Assertions.assertThrows(MissingDependenciesException.class, executable);
    }

    @Test
    public void resolve_WhenAbstractClass_ThenMissingDependenciesException()
    {
        // when
        Executable executable = () -> testObject.resolve(ClassBasicAbstract.class);
        // then
        Assertions.assertThrows(MissingDependenciesException.class, executable);
    }

    @Test
    public void resolve_WhenClassConstructorThrowsException_ThenNoInstanceCreatedException()
    {
        // when
        Executable executable = () -> testObject.resolve(ClassConstructorExceptionThrown.class);
        // then
        Assertions.assertThrows(NoInstanceCreatedException.class, executable);
    }

    @Test
    public void resolve_WhenPrimitiveType_ThenNoSuitableConstructorException()
    {
        // when
        Executable executable = () -> testObject.resolve(double.class);
        // then
        Assertions.assertThrows(NoSuitableConstructorException.class, executable);
    }

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

    // endregion
    // region resolve [setter]

    @Test
    public void resolve_WhenDependencySetterHasReturnType_ThenIncorrectDependencySetterException()
    {
        // when
        Executable executable = () -> testObject.resolve(ClassSetterIncorrect1.class);
        // then
        Assertions.assertThrows(IncorrectDependencySetterException.class, executable);
    }

    @Test
    public void resolve_WhenDependencySetterHasNoParameters_ThenIncorrectDependencySetterException()
    {
        // when
        Executable executable = () -> testObject.resolve(ClassSetterIncorrect2.class);
        // then
        Assertions.assertThrows(IncorrectDependencySetterException.class, executable);
    }

    @Test
    public void resolve_WhenDependencySetterNameDoesNotStartWithSet_ThenIncorrectDependencySetterException()
    {
        // when
        Executable executable = () -> testObject.resolve(ClassSetterIncorrect3.class);
        // then
        Assertions.assertThrows(IncorrectDependencySetterException.class, executable);
    }

    @Test
    public void resolve_WhenDoubleDependencySetter_ThenIncorrectDependencySetterException()
    {
        // when
        Executable executable = () -> testObject.resolve(ClassSetterDouble.class);
        // then
        Assertions.assertThrows(IncorrectDependencySetterException.class, executable);
    }

    @Test
    public void resolve_WhenMissingDependency_ThenMissingDependenciesException()
    {
        // when
        Executable executable = () -> testObject.resolve(ClassSetterSingle.class);
        // then
        Assertions.assertThrows(MissingDependenciesException.class, executable);
    }

    @Test
    public void resolve_WhenSetterThrowsException_ThenSetterInvocationException()
    {
        // given
        dictionary.addInstance(String.class, "string");
        // when
        Executable executable = () -> testObject.resolve(ClassSetterThrows.class);
        // then
        Assertions.assertThrows(SetterInvocationException.class, executable);
    }

    @Test
    public void resolve_WhenDependencySetterOnly_ThenInstanceIsResolved()
    {
        // given
        dictionary.addType(InterfaceBasic.class, ClassConstructorDefault.class);
        // when
        InterfaceSetter result = testObject.resolve(ClassSetterSingle.class);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBasicObject());
    }

    @Test
    public void resolve_WhenMultipleDependencySetters_ThenInstanceIsResolved()
    {
        // given
        String string = "string";

        dictionary.addType(InterfaceSetterMultiple.class, ClassSetterMultiple.class);
        dictionary.addType(InterfaceBasic.class, ClassConstructorDefault.class);
        dictionary.addType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class);
        dictionary.addInstance(String.class, string);
        // when
        InterfaceSetterMultiple result = testObject.resolve(InterfaceSetterMultiple.class);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBasicObject());
        Assertions.assertNotNull(result.getStringObject());
        Assertions.assertNotNull(result.getStringObject().getString());
        Assertions.assertEquals(string, result.getStringObject().getString());
    }

    // endregion
    // region resolve [dependencies schemas]

    @Test
    public void resolve_WhenDependenciesWithRegisteredInstance_ThenInstanceIsResolved()
    {
        // given
        String string = "String";

        dictionary.addInstance(String.class, string);
        dictionary.addType(InterfaceBasic.class, ClassConstructorDefault.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceDiamondTop.class, ClassDiamondTop.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceBasicSimpleDependency.class,
                           ClassConstructorNotAnnotatedWithDependency.class,
                           ConstructionPolicy.getDefault());
        // when
        InterfaceBasicSimpleDependency result =
                testObject.resolve(InterfaceBasicSimpleDependency.class);
        // then
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
        // given
        dictionary.addType(InterfaceBasic.class, ClassConstructorDefault.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceDiamondTop.class, ClassDiamondTop.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceBasicSimpleDependency.class,
                           ClassConstructorNotAnnotatedWithDependency.class,
                           ConstructionPolicy.getDefault());
        // when
        InterfaceBasicSimpleDependency result =
                testObject.resolve(InterfaceBasicSimpleDependency.class);
        // then
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
        // given
        dictionary.addType(InterfaceBasic.class, ClassConstructorDefault.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceDiamondTop.class, ClassDiamondTop.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceBasicSimpleDependency.class,
                           ClassConstructorNotAnnotatedWithDependency.class,
                           ConstructionPolicy.getDefault());
        // when
        InterfaceBasicSimpleDependency result =
                testObject.resolve(InterfaceBasicSimpleDependency.class);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getFirstObject());
        Assertions.assertNull(result.getSecondObject());
        Assertions.assertNotNull(result.getFirstObject().getObject());
        Assertions.assertTrue(result instanceof ClassConstructorNotAnnotatedWithDependency);
    }

    @Test
    public void resolve_WhenDependenciesWithAnnotatedConstructor_ThenInstanceIsResolved()
    {
        // given
        dictionary.addType(InterfaceBasic.class, ClassConstructorDefault.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceDiamondTop.class, ClassDiamondTop.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceBasicSimpleDependency.class,
                           ClassConstructorAnnotatedWithDependency.class,
                           ConstructionPolicy.getDefault());
        // when
        InterfaceBasicSimpleDependency result =
                testObject.resolve(InterfaceBasicSimpleDependency.class);
        // then
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
        // given
        dictionary.addType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceDiamondRight.class, ClassDiamondRight.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceDiamondBottom.class, ClassDiamondBottom.class,
                           ConstructionPolicy.getDefault());
        // then
        Assertions.assertThrows(MissingDependenciesException.class,
                                () -> testObject.resolve(InterfaceDiamondBottom.class));
    }

    @Test
    public void resolve_WhenDiamondDependenciesWithoutSingleton_ThenInstanceIsResolved()
    {
        // given
        dictionary.addType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceDiamondRight.class, ClassDiamondRight.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceDiamondBottom.class, ClassDiamondBottom.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceDiamondTop.class, ClassDiamondTop.class,
                           ConstructionPolicy.getDefault());
        // when
        InterfaceDiamondBottom result = testObject.resolve(InterfaceDiamondBottom.class);
        // then
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
        // given
        dictionary.addType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceDiamondRight.class, ClassDiamondRight.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceDiamondBottom.class, ClassDiamondBottom.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceDiamondTop.class, ClassDiamondTop.class,
                           ConstructionPolicy.SINGLETON);
        // when
        InterfaceDiamondBottom result = testObject.resolve(InterfaceDiamondBottom.class);
        // then
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
        // given
        dictionary.addType(InterfaceCircularLeft.class, ClassCircularLeft.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceCircularRight.class, ClassCircularRight.class,
                           ConstructionPolicy.getDefault());
        // then
        Assertions.assertThrows(CircularDependenciesException.class,
                                () -> testObject.resolve(InterfaceCircularRight.class));
    }

    @Test
    public void resolve_WhenCanOmitCircularDependencies_ThenInstanceIsResolved()
    {
        // given
        String string = "String";

        dictionary.addInstance(String.class, string);
        dictionary.addType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceCircularLeft.class, ClassCircularLeft.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceCircularRight.class, ClassCircularRight.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceCircularDependency.class, ClassCircularDependency.class,
                           ConstructionPolicy.getDefault());
        // when
        InterfaceCircularDependency result = testObject.resolve(InterfaceCircularDependency.class);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getNonCircularObject());
        Assertions.assertNull(result.getCircularObject());
        Assertions.assertNotNull(result.getNonCircularObject().getString());
        Assertions.assertEquals(string, result.getNonCircularObject().getString());
        Assertions.assertTrue(result instanceof ClassCircularDependency);
    }

    @Test
    public void resolve_WhenComplexDependency_ThenInstanceIsResolved()
    {
        // given
        String string = "string";

        dictionary.addType(InterfaceBasicComplexDependency.class, ClassBasicComplexDependency.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceBasic.class, ClassConstructorDefault.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceDiamondTop.class, ClassDiamondTop.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class,
                           ConstructionPolicy.getDefault());
        dictionary.addInstance(String.class, string);
        // when
        InterfaceBasicComplexDependency result =
                testObject.resolve(InterfaceBasicComplexDependency.class);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBasicObject());
        Assertions.assertNotNull(result.getFirstObject());
        Assertions.assertNotNull(result.getSecondObject());
        Assertions.assertNotNull(result.getFirstObject().getObject());
        Assertions.assertNotNull(result.getSecondObject().getString());
        Assertions.assertEquals(string, result.getSecondObject().getString());
        Assertions.assertTrue(result instanceof ClassBasicComplexDependency);
    }

    // endregion
    // region resolve [@Register and @SelfRegister]

    @Test
    public void resolve_WhenAnnotatedInterface_ThenInstanceIsResolved()
    {
        // when
        InterfaceRegister result1 = testObject.resolve(InterfaceRegister.class);
        InterfaceRegister result2 = testObject.resolve(InterfaceRegister.class);
        // then
        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertTrue(result1 instanceof ClassRegisterInterface);
        Assertions.assertTrue(result2 instanceof ClassRegisterInterface);
        Assertions.assertNotSame(result1, result2);
    }

    @Test
    public void resolve_WhenAnnotatedAbstractClass_ThenInstanceIsResolved()
    {
        // when
        ClassRegisterAbstract result1 = testObject.resolve(ClassRegisterAbstract.class);
        ClassRegisterAbstract result2 = testObject.resolve(ClassRegisterAbstract.class);
        // then
        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertTrue(result2 instanceof ClassRegisterConcrete);
        Assertions.assertTrue(result1 instanceof ClassRegisterConcrete);
        Assertions.assertNotSame(result1, result2);
    }

    @Test
    public void resolve_WhenAnnotatedConcreteClass_ThenInstanceIsResolved()
    {
        // when
        ClassRegisterConcrete result1 = testObject.resolve(ClassRegisterConcrete.class);
        ClassRegisterConcrete result2 = testObject.resolve(ClassRegisterConcrete.class);
        // then
        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertTrue(result1 instanceof ClassRegisterDerivedFromRegister);
        Assertions.assertTrue(result2 instanceof ClassRegisterDerivedFromRegister);
        Assertions.assertNotSame(result1, result2);
    }

    @Test
    public void resolve_WhenSelfAnnotatedConcreteClass_ThenInstanceIsResolved()
    {
        // when
        ClassRegisterSelf result1 = testObject.resolve(ClassRegisterSelf.class);
        ClassRegisterSelf result2 = testObject.resolve(ClassRegisterSelf.class);
        // then
        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertNotSame(result1, result2);
    }

    @Test
    public void resolve_WhenAnnotatedConcreteClassAsItself_ThenInstanceIsResolved()
    {
        // when
        ClassRegisterSelfAsSubtype result1 = testObject.resolve(ClassRegisterSelfAsSubtype.class);
        ClassRegisterSelfAsSubtype result2 = testObject.resolve(ClassRegisterSelfAsSubtype.class);
        // then
        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertNotSame(result1, result2);
    }

    @Test
    public void resolve_WhenSelfAnnotatedInterface_ThenAbstractTypeException()
    {
        // when
        Executable executable = () -> testObject.resolve(InterfaceRegisterSelfIncorrect.class);
        //then
        Assertions.assertThrows(AbstractTypeException.class, executable);
    }

    @Test
    public void resolve_WhenSelfAnnotatedAbstractClass_ThenAbstractTypeException()
    {
        // when
        Executable executable = () -> testObject.resolve(ClassRegisterSelfAbstractIncorrect.class);
        //then
        Assertions.assertThrows(AbstractTypeException.class, executable);
    }

    @Test
    public void resolve_WhenAnnotatedAbstractClassAsItself_ThenAbstractTypeException()
    {
        // when
        Executable executable = () -> testObject.resolve(ClassRegisterAbstractIncorrect.class);
        //then
        Assertions.assertThrows(AbstractTypeException.class, executable);
    }

    @Test
    public void resolve_WhenInterfaceAnnotatedClassNotImplementing_ThenNotDerivedTypeException()
    {
        // when
        Executable executable = () -> testObject.resolve(InterfaceRegisterIncorrect.class);
        //then
        Assertions.assertThrows(NotDerivedTypeException.class, executable);
    }

    @Test
    public void resolve_WhenAnnotatedClassRegistersInterface_ThenNotDerivedTypeException()
    {
        // when
        Executable executable = () -> testObject.resolve(ClassRegisterInterfaceIncorrect.class);
        //then
        Assertions.assertThrows(NotDerivedTypeException.class, executable);
    }

    @Test
    public void resolve_WhenAnnotatedClassRegistersNotDerivedClass_ThenNotDerivedTypeException()
    {
        // when
        Executable executable = () -> testObject.resolve(ClassRegisterIncorrectOtherClass.class);
        // then
        Assertions.assertThrows(NotDerivedTypeException.class, executable);
    }

    @Test
    public void resolve_WhenAnnotatedClassRegistersAbstractConcreteSuperclass_ThenNotDerivedTypeException()
    {
        // when
        Executable executable = () -> testObject.resolve(ClassRegisterIncorrectSuperClass.class);
        // then
        Assertions.assertThrows(NotDerivedTypeException.class, executable);
    }

    @Test
    public void resolve_WhenAnnotatedInterfaceSingleton_ThenSameInstances()
    {
        // when
        InterfaceRegisterSingleton result1 = testObject.resolve(InterfaceRegisterSingleton.class);
        InterfaceRegisterSingleton result2 = testObject.resolve(InterfaceRegisterSingleton.class);
        // then
        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertTrue(result1 instanceof ClassRegisterSingletonBase);
        Assertions.assertTrue(result2 instanceof ClassRegisterSingletonBase);
        Assertions.assertSame(result1, result2);
    }

    @Test
    public void resolve_WhenAnnotatedConcreteClassSingleton_ThenSameInstances()
    {
        // when
        ClassRegisterSingletonBase result1 = testObject.resolve(ClassRegisterSingletonBase.class);
        ClassRegisterSingletonBase result2 = testObject.resolve(ClassRegisterSingletonBase.class);
        // then
        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertTrue(result1 instanceof ClassRegisterSingletonDerived);
        Assertions.assertTrue(result2 instanceof ClassRegisterSingletonDerived);
        Assertions.assertSame(result1, result2);
    }

    @Test
    public void resolve_WhenSelfAnnotatedConcreteClassSingleton_ThenSameInstances()
    {
        // when
        ClassRegisterSelfSingleton result1 = testObject.resolve(ClassRegisterSelfSingleton.class);
        ClassRegisterSelfSingleton result2 = testObject.resolve(ClassRegisterSelfSingleton.class);
        // then
        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertSame(result1, result2);
    }

    @Test
    public void resolve_WhenAnnotatedConcreteClassAsItselfSingleton_ThenSameInstances()
    {
        // when
        ClassRegisterSelfAsSubtypeSingleton result1 =
                testObject.resolve(ClassRegisterSelfAsSubtypeSingleton.class);
        ClassRegisterSelfAsSubtypeSingleton result2 =
                testObject.resolve(ClassRegisterSelfAsSubtypeSingleton.class);
        // then
        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertSame(result1, result2);
    }

    // endregion
}
