package dicontainer.resolver;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void construct_WhenClassInheritsFromConcreteClass_ThenInstanceIsResolved()
    {
        // when
        ClassConstructorSuperParameterized result =
                testObject.construct(ClassConstructorSuperParameterized.class);
        // then
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void construct_WhenClassInheritsFromAbstractClass_ThenInstanceIsResolved()
    {
        // when
        ClassBasicInheritsFromAbstract result =
                testObject.construct(ClassBasicInheritsFromAbstract.class);
        // then
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void construct_WhenClassHasParameterConstructorWithoutRegisteredParameter_ThenMissingDependenciesException()
    {
        // when
        Throwable throwable = Assertions.catchThrowable(
                () -> testObject.construct(ClassConstructorParameterized.class));
        // then
        Assertions.assertThat(throwable).isInstanceOf(MissingDependenciesException.class);
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
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getNumber()).isEqualTo(number);
    }

    @Test
    public void construct_WhenClassHasPrimitiveParameterConstructorButRegisteredReferenceParameter_ThenMissingDependenciesException()
    {
        // given
        Integer number = 10;

        dictionary.addInstance(Integer.class, number);
        // when
        Throwable throwable = Assertions.catchThrowable(
                () -> testObject.construct(ClassConstructorParameterized.class));
        // then
        Assertions.assertThat(throwable).isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void construct_WhenClassHasDefaultAndParameterConstructorWithoutRegisteredParameter_ThenInstanceIsResolved()
    {
        // when
        ClassConstructorDefaultAndParameterized result =
                testObject.construct(ClassConstructorDefaultAndParameterized.class);
        // then
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void construct_WhenInterface_ThenMissingDependenciesException()
    {
        // when
        Throwable throwable =
                Assertions.catchThrowable(() -> testObject.construct(InterfaceBasic.class));
        // then
        Assertions.assertThat(throwable).isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void construct_WhenAbstractClass_ThenMissingDependenciesException()
    {
        // when
        Throwable throwable =
                Assertions.catchThrowable(() -> testObject.construct(ClassBasicAbstract.class));
        // then
        Assertions.assertThat(throwable).isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void construct_WhenClassConstructorThrowsException_ThenNoInstanceCreatedException()
    {
        // when
        Throwable throwable = Assertions.catchThrowable(
                () -> testObject.construct(ClassConstructorExceptionThrown.class));
        // then
        Assertions.assertThat(throwable).isInstanceOf(NoInstanceCreatedException.class);
    }

    @Test
    public void construct_WhenPrimitiveType_ThenNoSuitableConstructorException()
    {
        // when
        Throwable throwable = Assertions.catchThrowable(() -> testObject.construct(double.class));
        // then
        Assertions.assertThat(throwable).isInstanceOf(NoSuitableConstructorException.class);
    }

    @Test
    public void construct_WhenMultipleAnnotatedConstructors_ThenMultipleAnnotatedConstructorsException()
    {
        // when
        Throwable throwable = Assertions.catchThrowable(
                () -> testObject.construct(ClassConstructorMultipleAnnotated.class));
        // then
        Assertions.assertThat(throwable).isInstanceOf(MultipleAnnotatedConstructorsException.class);
    }

    @Test
    public void construct_WhenNoPublicConstructors_ThenNoSuitableConstructorException()
    {
        // when
        Throwable throwable = Assertions.catchThrowable(
                () -> testObject.construct(ClassConstructorPrivate.class));
        // then
        Assertions.assertThat(throwable).isInstanceOf(NoSuitableConstructorException.class);
    }

    // endregion
    // region construct [setter]

    @Test
    public void construct_WhenDependencySetterHasReturnType_ThenIncorrectDependencySetterException()
    {
        // when
        Throwable throwable = Assertions.catchThrowable(
                () -> testObject.construct(ClassSetterIncorrectReturnType.class));
        // then
        Assertions.assertThat(throwable).isInstanceOf(IncorrectDependencySetterException.class);
    }

    @Test
    public void construct_WhenDependencySetterHasNoParameters_ThenIncorrectDependencySetterException()
    {
        // when
        Throwable throwable = Assertions.catchThrowable(
                () -> testObject.construct(ClassSetterWithoutParameters.class));
        // then
        Assertions.assertThat(throwable).isInstanceOf(IncorrectDependencySetterException.class);
    }

    @Test
    public void construct_WhenDependencySetterHasIncorrectName_ThenIncorrectDependencySetterException()
    {
        // when
        Throwable throwable = Assertions.catchThrowable(
                () -> testObject.construct(ClassSetterIncorrectName.class));
        // then
        Assertions.assertThat(throwable).isInstanceOf(IncorrectDependencySetterException.class);
    }

    @Test
    public void construct_WhenDependencySetterHasMultipleParameters_ThenIncorrectDependencySetterException()
    {
        // when
        Throwable throwable = Assertions.catchThrowable(
                () -> testObject.construct(ClassSetterMultipleParameters.class));
        // then
        Assertions.assertThat(throwable).isInstanceOf(IncorrectDependencySetterException.class);
    }

    @Test
    public void construct_WhenMissingDependency_ThenMissingDependenciesException()
    {
        // when
        Throwable throwable =
                Assertions.catchThrowable(() -> testObject.construct(ClassSetterSingle.class));
        // then
        Assertions.assertThat(throwable).isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void construct_WhenSetterThrowsException_ThenSetterInvocationException()
    {
        // given
        dictionary.addInstance(String.class, "string");
        // when
        Throwable throwable =
                Assertions.catchThrowable(() -> testObject.construct(ClassSetterThrows.class));
        // then
        Assertions.assertThat(throwable).isInstanceOf(SetterInvocationException.class);
    }

    @Test
    public void construct_WhenDependencySetterOnly_ThenInstanceIsResolved()
    {
        // given
        dictionary.addType(InterfaceBasic.class, ClassConstructorDefault.class);
        // when
        InterfaceSetter result = testObject.construct(ClassSetterSingle.class);
        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getBasicObject()).isNotNull();
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
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getBasicObject()).isNotNull();
        Assertions.assertThat(result.getStringObject()).isNotNull();
        Assertions.assertThat(result.getStringObject().getString()).isNotNull();
        Assertions.assertThat(result.getStringObject().getString()).isEqualTo(string);
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
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getFirstObject()).isNotNull();
        Assertions.assertThat(result.getSecondObject()).isNotNull();
        Assertions.assertThat(result.getFirstObject().getObject()).isNotNull();
        Assertions.assertThat(result.getSecondObject().getString()).isNotNull();
        Assertions.assertThat(result.getSecondObject().getString()).isEqualTo(string);
        Assertions.assertThat(result)
                  .isInstanceOf(ClassConstructorNotAnnotatedWithDependency.class);
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
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getFirstObject()).isNotNull();
        Assertions.assertThat(result.getSecondObject()).isNotNull();
        Assertions.assertThat(result.getFirstObject().getObject()).isNotNull();
        Assertions.assertThat(result.getSecondObject().getString()).isNotNull();
        Assertions.assertThat(result.getSecondObject().getString()).isEqualTo("");
        Assertions.assertThat(result)
                  .isInstanceOf(ClassConstructorNotAnnotatedWithDependency.class);
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
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getFirstObject()).isNotNull();
        Assertions.assertThat(result.getSecondObject()).isNull();
        Assertions.assertThat(result.getFirstObject().getObject()).isNotNull();
        Assertions.assertThat(result)
                  .isInstanceOf(ClassConstructorNotAnnotatedWithDependency.class);
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
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getFirstObject()).isNotNull();
        Assertions.assertThat(result.getSecondObject()).isNotNull();
        Assertions.assertThat(result.getFirstObject().getObject()).isNotNull();
        Assertions.assertThat(result.getSecondObject().getString()).isNotNull();
        Assertions.assertThat(result.getSecondObject().getString()).isEqualTo("");
        Assertions.assertThat(result).isInstanceOf(ClassConstructorAnnotatedWithDependency.class);
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
        // when
        Throwable throwable =
                Assertions.catchThrowable(() -> testObject.construct(InterfaceDiamondBottom.class));
        // then
        Assertions.assertThat(throwable).isInstanceOf(MissingDependenciesException.class);
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
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getDiamond1()).isNotNull();
        Assertions.assertThat(result.getDiamond2()).isNotNull();
        Assertions.assertThat(result.getDiamond1().getObject()).isNotNull();
        Assertions.assertThat(result.getDiamond2().getObject()).isNotNull();
        Assertions.assertThat(result.getDiamond1().getObject())
                  .isNotSameAs(result.getDiamond2().getObject());
        Assertions.assertThat(result).isInstanceOf(ClassDiamondBottom.class);
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
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getDiamond1()).isNotNull();
        Assertions.assertThat(result.getDiamond2()).isNotNull();
        Assertions.assertThat(result.getDiamond1().getObject()).isNotNull();
        Assertions.assertThat(result.getDiamond2().getObject()).isNotNull();
        Assertions.assertThat(result.getDiamond2().getObject())
                  .isSameAs(result.getDiamond1().getObject());
        Assertions.assertThat(result).isInstanceOf(ClassDiamondBottom.class);
    }

    @Test
    public void construct_WhenCircularDependencies_ThenCircularDependenciesException()
    {
        // given
        dictionary.addType(InterfaceCircularLeft.class, ClassCircularLeft.class,
                           ConstructionPolicy.getDefault());
        dictionary.addType(InterfaceCircularRight.class, ClassCircularRight.class,
                           ConstructionPolicy.getDefault());
        // when
        Throwable throwable =
                Assertions.catchThrowable(() -> testObject.construct(InterfaceCircularRight.class));
        // then
        Assertions.assertThat(throwable).isInstanceOf(CircularDependenciesException.class);
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
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getNonCircularObject()).isNotNull();
        Assertions.assertThat(result.getCircularObject()).isNull();
        Assertions.assertThat(result.getNonCircularObject().getString()).isNotNull();
        Assertions.assertThat(result.getNonCircularObject().getString()).isEqualTo(string);
        Assertions.assertThat(result).isInstanceOf(ClassCircularDependency.class);
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
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getBasicObject()).isNotNull();
        Assertions.assertThat(result.getFirstObject()).isNotNull();
        Assertions.assertThat(result.getSecondObject()).isNotNull();
        Assertions.assertThat(result.getFirstObject().getObject()).isNotNull();
        Assertions.assertThat(result.getSecondObject().getString()).isNotNull();
        Assertions.assertThat(result.getSecondObject().getString()).isEqualTo(string);
        Assertions.assertThat(result).isInstanceOf(ClassBasicComplexDependency.class);
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
        Assertions.assertThat(result1).isNotNull();
        Assertions.assertThat(result2).isNotNull();
        Assertions.assertThat(result1).isInstanceOf(ClassRegisterInterface.class);
        Assertions.assertThat(result2).isInstanceOf(ClassRegisterInterface.class);
        Assertions.assertThat(result2).isNotSameAs(result1);
    }

    @Test
    public void construct_WhenAnnotatedAbstractClass_ThenInstanceIsResolved()
    {
        // when
        ClassRegisterAbstract result1 = testObject.construct(ClassRegisterAbstract.class);
        ClassRegisterAbstract result2 = testObject.construct(ClassRegisterAbstract.class);
        // then
        Assertions.assertThat(result1).isNotNull();
        Assertions.assertThat(result2).isNotNull();
        Assertions.assertThat(result2).isInstanceOf(ClassRegisterConcrete.class);
        Assertions.assertThat(result1).isInstanceOf(ClassRegisterConcrete.class);
        Assertions.assertThat(result2).isNotSameAs(result1);
    }

    @Test
    public void construct_WhenAnnotatedConcreteClass_ThenInstanceIsResolved()
    {
        // when
        ClassRegisterConcrete result1 = testObject.construct(ClassRegisterConcrete.class);
        ClassRegisterConcrete result2 = testObject.construct(ClassRegisterConcrete.class);
        // then
        Assertions.assertThat(result1).isNotNull();
        Assertions.assertThat(result2).isNotNull();
        Assertions.assertThat(result1).isInstanceOf(ClassRegisterDerivedFromRegister.class);
        Assertions.assertThat(result2).isInstanceOf(ClassRegisterDerivedFromRegister.class);
        Assertions.assertThat(result2).isNotSameAs(result1);
    }

    @Test
    public void construct_WhenSelfAnnotatedConcreteClass_ThenInstanceIsResolved()
    {
        // when
        ClassRegisterSelf result1 = testObject.construct(ClassRegisterSelf.class);
        ClassRegisterSelf result2 = testObject.construct(ClassRegisterSelf.class);
        // then
        Assertions.assertThat(result1).isNotNull();
        Assertions.assertThat(result2).isNotNull();
        Assertions.assertThat(result2).isNotSameAs(result1);
    }

    @Test
    public void construct_WhenAnnotatedConcreteClassAsItself_ThenInstanceIsResolved()
    {
        // when
        ClassRegisterSelfAsSubtype result1 = testObject.construct(ClassRegisterSelfAsSubtype.class);
        ClassRegisterSelfAsSubtype result2 = testObject.construct(ClassRegisterSelfAsSubtype.class);
        // then
        Assertions.assertThat(result1).isNotNull();
        Assertions.assertThat(result2).isNotNull();
        Assertions.assertThat(result2).isNotSameAs(result1);
    }

    @Test
    public void construct_WhenSelfAnnotatedInterface_ThenAbstractTypeException()
    {
        // when
        Throwable throwable = Assertions.catchThrowable(
                () -> testObject.construct(InterfaceRegisterSelfIncorrect.class));
        //then
        Assertions.assertThat(throwable).isInstanceOf(AbstractTypeException.class);
    }

    @Test
    public void construct_WhenSelfAnnotatedAbstractClass_ThenAbstractTypeException()
    {
        // when
        Throwable throwable = Assertions.catchThrowable(
                () -> testObject.construct(ClassRegisterSelfAbstractIncorrect.class));
        //then
        Assertions.assertThat(throwable).isInstanceOf(AbstractTypeException.class);
    }

    @Test
    public void construct_WhenAnnotatedAbstractClassAsItself_ThenAbstractTypeException()
    {
        // when
        Throwable throwable = Assertions.catchThrowable(
                () -> testObject.construct(ClassRegisterAbstractIncorrect.class));
        //then
        Assertions.assertThat(throwable).isInstanceOf(AbstractTypeException.class);
    }

    @Test
    public void construct_WhenInterfaceAnnotatedClassNotImplementing_ThenNotDerivedTypeException()
    {
        // when
        Throwable throwable = Assertions.catchThrowable(
                () -> testObject.construct(InterfaceRegisterIncorrect.class));
        //then
        Assertions.assertThat(throwable).isInstanceOf(NotDerivedTypeException.class);
    }

    @Test
    public void construct_WhenAnnotatedClassRegistersInterface_ThenNotDerivedTypeException()
    {
        // when
        Throwable throwable = Assertions.catchThrowable(
                () -> testObject.construct(ClassRegisterInterfaceIncorrect.class));
        //then
        Assertions.assertThat(throwable).isInstanceOf(NotDerivedTypeException.class);
    }

    @Test
    public void construct_WhenAnnotatedClassRegistersNotDerivedClass_ThenNotDerivedTypeException()
    {
        // when
        Throwable throwable = Assertions.catchThrowable(
                () -> testObject.construct(ClassRegisterIncorrectOtherClass.class));
        // then
        Assertions.assertThat(throwable).isInstanceOf(NotDerivedTypeException.class);
    }

    @Test
    public void construct_WhenAnnotatedClassRegistersAbstractConcreteSuperclass_ThenNotDerivedTypeException()
    {
        // when
        Throwable throwable = Assertions.catchThrowable(
                () -> testObject.construct(ClassRegisterIncorrectSuperClass.class));
        // then
        Assertions.assertThat(throwable).isInstanceOf(NotDerivedTypeException.class);
    }

    @Test
    public void construct_WhenAnnotatedInterfaceSingleton_ThenSameInstances()
    {
        // when
        InterfaceRegisterSingleton result1 = testObject.construct(InterfaceRegisterSingleton.class);
        InterfaceRegisterSingleton result2 = testObject.construct(InterfaceRegisterSingleton.class);
        // then
        Assertions.assertThat(result1).isNotNull();
        Assertions.assertThat(result2).isNotNull();
        Assertions.assertThat(result1).isInstanceOf(ClassRegisterSingletonBase.class);
        Assertions.assertThat(result2).isInstanceOf(ClassRegisterSingletonBase.class);
        Assertions.assertThat(result2).isSameAs(result1);
    }

    @Test
    public void construct_WhenAnnotatedConcreteClassSingleton_ThenSameInstances()
    {
        // when
        ClassRegisterSingletonBase result1 = testObject.construct(ClassRegisterSingletonBase.class);
        ClassRegisterSingletonBase result2 = testObject.construct(ClassRegisterSingletonBase.class);
        // then
        Assertions.assertThat(result1).isNotNull();
        Assertions.assertThat(result2).isNotNull();
        Assertions.assertThat(result1).isInstanceOf(ClassRegisterSingletonDerived.class);
        Assertions.assertThat(result2).isInstanceOf(ClassRegisterSingletonDerived.class);
        Assertions.assertThat(result2).isSameAs(result1);
    }

    @Test
    public void construct_WhenSelfAnnotatedConcreteClassSingleton_ThenSameInstances()
    {
        // when
        ClassRegisterSelfSingleton result1 = testObject.construct(ClassRegisterSelfSingleton.class);
        ClassRegisterSelfSingleton result2 = testObject.construct(ClassRegisterSelfSingleton.class);
        // then
        Assertions.assertThat(result1).isNotNull();
        Assertions.assertThat(result2).isNotNull();
        Assertions.assertThat(result2).isSameAs(result1);
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
        Assertions.assertThat(result1).isNotNull();
        Assertions.assertThat(result2).isNotNull();
        Assertions.assertThat(result2).isSameAs(result1);
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
        Assertions.assertThat(result).isSameAs(instance);
        Assertions.assertThat(result.getBasicObject()).isNotNull();
        Assertions.assertThat(result.getStringObject()).isNotNull();
        Assertions.assertThat(result.getStringObject().getString()).isNotNull();
        Assertions.assertThat(result.getStringObject().getString()).isEqualTo(string);
    }
    // endregion
}
