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
import dicontainer.dictionary.RegistrationDictionary;
import dicontainer.exception.*;

class ConstructorResolverTest
{
    private RegistrationDictionary registrationDictionary;
    private ConstructorResolver testObject;

    @BeforeEach
    public void setUp()
    {
        registrationDictionary = new RegistrationDictionary();
        testObject = new ConstructorResolver(registrationDictionary);
    }

    @AfterEach
    public void tearDown()
    {
        testObject = null;
    }

    //region resolve (class)

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

        registrationDictionary.insertInstance(int.class, number);
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

        registrationDictionary.insertInstance(Integer.class, number);
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
        // then
        Assertions.assertThrows(MissingDependenciesException.class,
                                () -> testObject.resolve(InterfaceBasic.class));
    }

    @Test
    public void resolve_WhenAbstractClass_ThenMissingDependenciesException()
    {
        // then
        Assertions.assertThrows(MissingDependenciesException.class,
                                () -> testObject.resolve(ClassBasicAbstract.class));
    }

    // endregion
    // region resolve (dependencies schemas)

    @Test
    public void resolve_WhenDependenciesWithRegisteredInstance_ThenInstanceIsResolved()
    {
        // given
        String string = "String";

        registrationDictionary.insertInstance(String.class, string);
        registrationDictionary.insertType(InterfaceBasic.class, ClassConstructorDefault.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceDiamondTop.class, ClassDiamondTop.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceBasicStringGetter.class,
                                          ClassBasicStringGetter.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceBasicSimpleDependency.class,
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
        registrationDictionary.insertType(InterfaceBasic.class, ClassConstructorDefault.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceDiamondTop.class, ClassDiamondTop.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceBasicStringGetter.class,
                                          ClassBasicStringGetter.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceBasicSimpleDependency.class,
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
        registrationDictionary.insertType(InterfaceBasic.class, ClassConstructorDefault.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceDiamondTop.class, ClassDiamondTop.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceBasicSimpleDependency.class,
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
        registrationDictionary.insertType(InterfaceBasic.class, ClassConstructorDefault.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceDiamondTop.class, ClassDiamondTop.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceBasicStringGetter.class,
                                          ClassBasicStringGetter.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceBasicSimpleDependency.class,
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
        registrationDictionary.insertType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceDiamondRight.class, ClassDiamondRight.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceDiamondBottom.class, ClassDiamondBottom.class,
                                          ConstructionPolicy.getDefault());
        // then
        Assertions.assertThrows(MissingDependenciesException.class,
                                () -> testObject.resolve(InterfaceDiamondBottom.class));
    }

    @Test
    public void resolve_WhenDiamondDependenciesWithoutSingleton_ThenInstanceIsResolved()
    {
        // given
        registrationDictionary.insertType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceDiamondRight.class, ClassDiamondRight.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceDiamondBottom.class, ClassDiamondBottom.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceDiamondTop.class, ClassDiamondTop.class,
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
        registrationDictionary.insertType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceDiamondRight.class, ClassDiamondRight.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceDiamondBottom.class, ClassDiamondBottom.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceDiamondTop.class, ClassDiamondTop.class,
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
        registrationDictionary.insertType(InterfaceCircularLeft.class, ClassCircularLeft.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceCircularRight.class, ClassCircularRight.class,
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

        registrationDictionary.insertInstance(String.class, string);
        registrationDictionary.insertType(InterfaceBasicStringGetter.class,
                                          ClassBasicStringGetter.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceCircularLeft.class, ClassCircularLeft.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceCircularRight.class, ClassCircularRight.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceCircularDependency.class,
                                          ClassCircularDependency.class,
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
    public void resolve_WhenComplexDependency_ThenInstanceIsResolved()
    {
        // given
        String string = "string";

        registrationDictionary.insertType(InterfaceBasicComplexDependency.class,
                                          ClassBasicComplexDependency.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceBasic.class, ClassConstructorDefault.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceDiamondTop.class, ClassDiamondTop.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertType(InterfaceBasicStringGetter.class,
                                          ClassBasicStringGetter.class,
                                          ConstructionPolicy.getDefault());
        registrationDictionary.insertInstance(String.class, string);
        // when
        InterfaceBasicComplexDependency result =
                testObject.resolve(InterfaceBasicComplexDependency.class);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertNull(result.getBasicObject());
        Assertions.assertNotNull(result.getFirstObject());
        Assertions.assertNotNull(result.getSecondObject());
        Assertions.assertNotNull(result.getFirstObject().getObject());
        Assertions.assertNotNull(result.getSecondObject().getString());
        Assertions.assertEquals(string, result.getSecondObject().getString());
        Assertions.assertTrue(result instanceof ClassBasicComplexDependency);
    }

    // endregion
    // region resolve (@Register and @SelfRegister)

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
