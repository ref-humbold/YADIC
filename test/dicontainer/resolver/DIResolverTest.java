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
import dicontainer.dictionary.exception.AbstractTypeException;
import dicontainer.dictionary.exception.NotDerivedTypeException;
import dicontainer.resolver.exception.*;

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

    // region construct [constructor]

    @Test
    public void construct_WhenClassHasDefaultConstructorOnly_ThenInstanceIsResolved()
    {
        // when
        ClassConstructorDefault result = testObject.construct(ClassConstructorDefault.class);
        // then
        Assertions.assertNotNull(result);
    }

    @Test
    public void construct_WhenClassInheritsFromConcreteClass_ThenInstanceIsResolved()
    {
        // when
        ClassConstructorSuperParameterized result =
                testObject.construct(ClassConstructorSuperParameterized.class);
        // then
        Assertions.assertNotNull(result);
    }

    @Test
    public void construct_WhenClassInheritsFromAbstractClass_ThenInstanceIsResolved()
    {
        // when
        ClassBasicInheritsFromAbstract result =
                testObject.construct(ClassBasicInheritsFromAbstract.class);
        // then
        Assertions.assertNotNull(result);
    }

    @Test
    public void construct_WhenClassHasParameterConstructorWithoutRegisteredParameter_ThenMissingDependenciesException()
    {
        // when
        Executable executable = () -> testObject.construct(ClassConstructorParameterized.class);
        // then
        Assertions.assertThrows(MissingDependenciesException.class, executable);
    }

    @Test
    public void construct_WhenClassHasParameterConstructorWithRegisteredPrimitiveParameter_ThenInstanceIsResolved()
    {
        // given
        int number = 10;

        dictionary.addInstance(int.class, number);
        // when
        ClassConstructorParameterized result =
                testObject.construct(ClassConstructorParameterized.class);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(number, result.getNumber());
    }

    @Test
    public void construct_WhenClassHasPrimitiveParameterConstructorButRegisteredReferenceParameter_ThenMissingDependenciesException()
    {
        // given
        Integer number = 10;

        dictionary.addInstance(Integer.class, number);
        // when
        Executable executable = () -> testObject.construct(ClassConstructorParameterized.class);
        // then
        Assertions.assertThrows(MissingDependenciesException.class, executable);
    }

    @Test
    public void construct_WhenClassHasDefaultAndParameterConstructorWithoutRegisteredParameter_ThenInstanceIsResolved()
    {
        // when
        ClassConstructorDefaultAndParameterized result =
                testObject.construct(ClassConstructorDefaultAndParameterized.class);
        // then
        Assertions.assertNotNull(result);
    }

    @Test
    public void construct_WhenInterface_ThenMissingDependenciesException()
    {
        // when
        Executable executable = () -> testObject.construct(InterfaceBasic.class);
        // then
        Assertions.assertThrows(MissingDependenciesException.class, executable);
    }

    @Test
    public void construct_WhenAbstractClass_ThenMissingDependenciesException()
    {
        // when
        Executable executable = () -> testObject.construct(ClassBasicAbstract.class);
        // then
        Assertions.assertThrows(MissingDependenciesException.class, executable);
    }

    @Test
    public void construct_WhenClassConstructorThrowsException_ThenNoInstanceCreatedException()
    {
        // when
        Executable executable = () -> testObject.construct(ClassConstructorExceptionThrown.class);
        // then
        Assertions.assertThrows(NoInstanceCreatedException.class, executable);
    }

    @Test
    public void construct_WhenPrimitiveType_ThenNoSuitableConstructorException()
    {
        // when
        Executable executable = () -> testObject.construct(double.class);
        // then
        Assertions.assertThrows(NoSuitableConstructorException.class, executable);
    }

    @Test
    public void construct_WhenMultipleAnnotatedConstructors_ThenMultipleAnnotatedConstructorsException()
    {
        // when
        Executable executable = () -> testObject.construct(ClassConstructorMultipleAnnotated.class);
        // then
        Assertions.assertThrows(MultipleAnnotatedConstructorsException.class, executable);
    }

    @Test
    public void construct_WhenNoPublicConstructors_ThenNoSuitableConstructorException()
    {
        // when
        Executable executable = () -> testObject.construct(ClassConstructorPrivate.class);
        // then
        Assertions.assertThrows(NoSuitableConstructorException.class, executable);
    }

    // endregion
    // region construct [setter]

    @Test
    public void construct_WhenDependencySetterHasReturnType_ThenIncorrectDependencySetterException()
    {
        // when
        Executable executable = () -> testObject.construct(ClassSetterIncorrectReturnType.class);
        // then
        Assertions.assertThrows(IncorrectDependencySetterException.class, executable);
    }

    @Test
    public void construct_WhenDependencySetterHasNoParameters_ThenIncorrectDependencySetterException()
    {
        // when
        Executable executable = () -> testObject.construct(ClassSetterWithoutParameters.class);
        // then
        Assertions.assertThrows(IncorrectDependencySetterException.class, executable);
    }

    @Test
    public void construct_WhenDependencySetterHasIncorrectName_ThenIncorrectDependencySetterException()
    {
        // when
        Executable executable = () -> testObject.construct(ClassSetterIncorrectName.class);
        // then
        Assertions.assertThrows(IncorrectDependencySetterException.class, executable);
    }

    @Test
    public void construct_WhenDependencySetterHasMultipleParameters_ThenIncorrectDependencySetterException()
    {
        // when
        Executable executable = () -> testObject.construct(ClassSetterMultipleParameters.class);
        // then
        Assertions.assertThrows(IncorrectDependencySetterException.class, executable);
    }

    @Test
    public void construct_WhenMissingDependency_ThenMissingDependenciesException()
    {
        // when
        Executable executable = () -> testObject.construct(ClassSetterSingle.class);
        // then
        Assertions.assertThrows(MissingDependenciesException.class, executable);
    }

    @Test
    public void construct_WhenSetterThrowsException_ThenSetterInvocationException()
    {
        // given
        dictionary.addInstance(String.class, "string");
        // when
        Executable executable = () -> testObject.construct(ClassSetterThrows.class);
        // then
        Assertions.assertThrows(SetterInvocationException.class, executable);
    }

    @Test
    public void construct_WhenDependencySetterOnly_ThenInstanceIsResolved()
    {
        // given
        dictionary.addType(InterfaceBasic.class, ClassConstructorDefault.class);
        // when
        InterfaceSetter result = testObject.construct(ClassSetterSingle.class);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBasicObject());
    }

    @Test
    public void construct_WhenMultipleDependencySetters_ThenInstanceIsResolved()
    {
        // given
        String string = "string";

        dictionary.addType(InterfaceSetterMultiple.class, ClassSetterMultiple.class);
        dictionary.addType(InterfaceBasic.class, ClassConstructorDefault.class);
        dictionary.addType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class);
        dictionary.addInstance(String.class, string);
        // when
        InterfaceSetterMultiple result = testObject.construct(InterfaceSetterMultiple.class);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBasicObject());
        Assertions.assertNotNull(result.getStringObject());
        Assertions.assertNotNull(result.getStringObject().getString());
        Assertions.assertEquals(string, result.getStringObject().getString());
    }

    // endregion
    // region construct [dependencies schemas]

    @Test
    public void construct_WhenDependenciesWithRegisteredInstance_ThenInstanceIsResolved()
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
                testObject.construct(InterfaceBasicSimpleDependency.class);
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
    public void construct_WhenDependenciesWithoutAnnotatedConstructorsWithAllDependencies_ThenInstanceIsResolved()
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
                testObject.construct(InterfaceBasicSimpleDependency.class);
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
    public void construct_WhenDependenciesWithoutAnnotatedConstructorsWithoutSomeDependencies_ThenInstanceIsResolved()
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
                testObject.construct(InterfaceBasicSimpleDependency.class);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getFirstObject());
        Assertions.assertNull(result.getSecondObject());
        Assertions.assertNotNull(result.getFirstObject().getObject());
        Assertions.assertTrue(result instanceof ClassConstructorNotAnnotatedWithDependency);
    }

    @Test
    public void construct_WhenDependenciesWithAnnotatedConstructor_ThenInstanceIsResolved()
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
                testObject.construct(InterfaceBasicSimpleDependency.class);
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
    public void construct_WhenDependenciesWithAnnotatedConstructorWithoutSomeDependencies_ThenMissingDependenciesException()
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
                                () -> testObject.construct(InterfaceDiamondBottom.class));
    }

    @Test
    public void construct_WhenDiamondDependenciesWithoutSingleton_ThenInstanceIsResolved()
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
        InterfaceDiamondBottom result = testObject.construct(InterfaceDiamondBottom.class);
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
    public void construct_WhenDiamondDependenciesWithSingleton_ThenInstanceIsResolved()
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
        InterfaceDiamondBottom result = testObject.construct(InterfaceDiamondBottom.class);
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
    public void construct_WhenCircularDependencies_ThenCircularDependenciesException()
    {
        // given
        dictionary.addType(InterfaceCircularLeft.class, ClassCircularLeft.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceCircularRight.class, ClassCircularRight.class,
                           ConstructionPolicy.getDefault());
        // then
        Assertions.assertThrows(CircularDependenciesException.class,
                                () -> testObject.construct(InterfaceCircularRight.class));
    }

    @Test
    public void construct_WhenCanOmitCircularDependencies_ThenInstanceIsResolved()
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
        InterfaceCircularDependency result =
                testObject.construct(InterfaceCircularDependency.class);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getNonCircularObject());
        Assertions.assertNull(result.getCircularObject());
        Assertions.assertNotNull(result.getNonCircularObject().getString());
        Assertions.assertEquals(string, result.getNonCircularObject().getString());
        Assertions.assertTrue(result instanceof ClassCircularDependency);
    }

    @Test
    public void construct_WhenComplexDependency_ThenInstanceIsResolved()
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
                testObject.construct(InterfaceBasicComplexDependency.class);
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
    // region construct [@Register and @SelfRegister]

    @Test
    public void construct_WhenAnnotatedInterface_ThenInstanceIsResolved()
    {
        // when
        InterfaceRegister result1 = testObject.construct(InterfaceRegister.class);
        InterfaceRegister result2 = testObject.construct(InterfaceRegister.class);
        // then
        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertTrue(result1 instanceof ClassRegisterInterface);
        Assertions.assertTrue(result2 instanceof ClassRegisterInterface);
        Assertions.assertNotSame(result1, result2);
    }

    @Test
    public void construct_WhenAnnotatedAbstractClass_ThenInstanceIsResolved()
    {
        // when
        ClassRegisterAbstract result1 = testObject.construct(ClassRegisterAbstract.class);
        ClassRegisterAbstract result2 = testObject.construct(ClassRegisterAbstract.class);
        // then
        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertTrue(result2 instanceof ClassRegisterConcrete);
        Assertions.assertTrue(result1 instanceof ClassRegisterConcrete);
        Assertions.assertNotSame(result1, result2);
    }

    @Test
    public void construct_WhenAnnotatedConcreteClass_ThenInstanceIsResolved()
    {
        // when
        ClassRegisterConcrete result1 = testObject.construct(ClassRegisterConcrete.class);
        ClassRegisterConcrete result2 = testObject.construct(ClassRegisterConcrete.class);
        // then
        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertTrue(result1 instanceof ClassRegisterDerivedFromRegister);
        Assertions.assertTrue(result2 instanceof ClassRegisterDerivedFromRegister);
        Assertions.assertNotSame(result1, result2);
    }

    @Test
    public void construct_WhenSelfAnnotatedConcreteClass_ThenInstanceIsResolved()
    {
        // when
        ClassRegisterSelf result1 = testObject.construct(ClassRegisterSelf.class);
        ClassRegisterSelf result2 = testObject.construct(ClassRegisterSelf.class);
        // then
        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertNotSame(result1, result2);
    }

    @Test
    public void construct_WhenAnnotatedConcreteClassAsItself_ThenInstanceIsResolved()
    {
        // when
        ClassRegisterSelfAsSubtype result1 = testObject.construct(ClassRegisterSelfAsSubtype.class);
        ClassRegisterSelfAsSubtype result2 = testObject.construct(ClassRegisterSelfAsSubtype.class);
        // then
        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertNotSame(result1, result2);
    }

    @Test
    public void construct_WhenSelfAnnotatedInterface_ThenAbstractTypeException()
    {
        // when
        Executable executable = () -> testObject.construct(InterfaceRegisterSelfIncorrect.class);
        //then
        Assertions.assertThrows(AbstractTypeException.class, executable);
    }

    @Test
    public void construct_WhenSelfAnnotatedAbstractClass_ThenAbstractTypeException()
    {
        // when
        Executable executable =
                () -> testObject.construct(ClassRegisterSelfAbstractIncorrect.class);
        //then
        Assertions.assertThrows(AbstractTypeException.class, executable);
    }

    @Test
    public void construct_WhenAnnotatedAbstractClassAsItself_ThenAbstractTypeException()
    {
        // when
        Executable executable = () -> testObject.construct(ClassRegisterAbstractIncorrect.class);
        //then
        Assertions.assertThrows(AbstractTypeException.class, executable);
    }

    @Test
    public void construct_WhenInterfaceAnnotatedClassNotImplementing_ThenNotDerivedTypeException()
    {
        // when
        Executable executable = () -> testObject.construct(InterfaceRegisterIncorrect.class);
        //then
        Assertions.assertThrows(NotDerivedTypeException.class, executable);
    }

    @Test
    public void construct_WhenAnnotatedClassRegistersInterface_ThenNotDerivedTypeException()
    {
        // when
        Executable executable = () -> testObject.construct(ClassRegisterInterfaceIncorrect.class);
        //then
        Assertions.assertThrows(NotDerivedTypeException.class, executable);
    }

    @Test
    public void construct_WhenAnnotatedClassRegistersNotDerivedClass_ThenNotDerivedTypeException()
    {
        // when
        Executable executable = () -> testObject.construct(ClassRegisterIncorrectOtherClass.class);
        // then
        Assertions.assertThrows(NotDerivedTypeException.class, executable);
    }

    @Test
    public void construct_WhenAnnotatedClassRegistersAbstractConcreteSuperclass_ThenNotDerivedTypeException()
    {
        // when
        Executable executable = () -> testObject.construct(ClassRegisterIncorrectSuperClass.class);
        // then
        Assertions.assertThrows(NotDerivedTypeException.class, executable);
    }

    @Test
    public void construct_WhenAnnotatedInterfaceSingleton_ThenSameInstances()
    {
        // when
        InterfaceRegisterSingleton result1 = testObject.construct(InterfaceRegisterSingleton.class);
        InterfaceRegisterSingleton result2 = testObject.construct(InterfaceRegisterSingleton.class);
        // then
        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertTrue(result1 instanceof ClassRegisterSingletonBase);
        Assertions.assertTrue(result2 instanceof ClassRegisterSingletonBase);
        Assertions.assertSame(result1, result2);
    }

    @Test
    public void construct_WhenAnnotatedConcreteClassSingleton_ThenSameInstances()
    {
        // when
        ClassRegisterSingletonBase result1 = testObject.construct(ClassRegisterSingletonBase.class);
        ClassRegisterSingletonBase result2 = testObject.construct(ClassRegisterSingletonBase.class);
        // then
        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertTrue(result1 instanceof ClassRegisterSingletonDerived);
        Assertions.assertTrue(result2 instanceof ClassRegisterSingletonDerived);
        Assertions.assertSame(result1, result2);
    }

    @Test
    public void construct_WhenSelfAnnotatedConcreteClassSingleton_ThenSameInstances()
    {
        // when
        ClassRegisterSelfSingleton result1 = testObject.construct(ClassRegisterSelfSingleton.class);
        ClassRegisterSelfSingleton result2 = testObject.construct(ClassRegisterSelfSingleton.class);
        // then
        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertSame(result1, result2);
    }

    @Test
    public void construct_WhenAnnotatedConcreteClassAsItselfSingleton_ThenSameInstances()
    {
        // when
        ClassRegisterSelfAsSubtypeSingleton result1 =
                testObject.construct(ClassRegisterSelfAsSubtypeSingleton.class);
        ClassRegisterSelfAsSubtypeSingleton result2 =
                testObject.construct(ClassRegisterSelfAsSubtypeSingleton.class);
        // then
        Assertions.assertNotNull(result1);
        Assertions.assertNotNull(result2);
        Assertions.assertSame(result1, result2);
    }

    // endregion
    // region inject

    @Test
    public void inject_WhenMultipleDependencySetters_ThenInstanceIsResolved()
    {
        // given
        String string = "string";
        InterfaceSetterMultiple instance = new ClassSetterMultiple();

        dictionary.addType(InterfaceBasic.class, ClassConstructorDefault.class);
        dictionary.addType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class);
        dictionary.addInstance(String.class, string);
        // when
        InterfaceSetterMultiple result = testObject.inject(instance);
        // then
        Assertions.assertSame(instance, result);
        Assertions.assertNotNull(result.getBasicObject());
        Assertions.assertNotNull(result.getStringObject());
        Assertions.assertNotNull(result.getStringObject().getString());
        Assertions.assertEquals(string, result.getStringObject().getString());
    }
    // endregion
}
