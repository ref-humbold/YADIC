package dicontainer.resolver;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dicontainer.ConstructionPolicy;
import dicontainer.dictionary.DiDictionary;
import dicontainer.dictionary.exception.AbstractTypeException;
import dicontainer.dictionary.exception.NotDerivedTypeException;
import dicontainer.models.basic.*;
import dicontainer.models.circular.*;
import dicontainer.models.constructor.*;
import dicontainer.models.diamond.*;
import dicontainer.models.register.*;
import dicontainer.models.setter.*;
import dicontainer.resolver.exception.*;

public class DiResolverTest
{
    private DiDictionary dictionary;
    private DiResolver testObject;

    @BeforeEach
    public void setUp()
    {
        dictionary = new DiDictionary();
        testObject = new DiResolver(dictionary);
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
        Assertions.assertThatThrownBy(
                          () -> testObject.construct(ClassConstructorParameterized.class))
                  .isInstanceOf(MissingDependenciesException.class);
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

        // then
        Assertions.assertThatThrownBy(
                          () -> testObject.construct(ClassConstructorParameterized.class))
                  .isInstanceOf(MissingDependenciesException.class);
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
        Assertions.assertThatThrownBy(() -> testObject.construct(InterfaceBasic.class))
                  .isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void construct_WhenAbstractClass_ThenMissingDependenciesException()
    {
        Assertions.assertThatThrownBy(() -> testObject.construct(ClassBasicAbstract.class))
                  .isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void construct_WhenClassConstructorThrowsException_ThenNoInstanceCreatedException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.construct(ClassConstructorExceptionThrown.class))
                  .isInstanceOf(NoInstanceCreatedException.class);
    }

    @Test
    public void construct_WhenPrimitiveType_ThenNoSuitableConstructorException()
    {
        Assertions.assertThatThrownBy(() -> testObject.construct(double.class))
                  .isInstanceOf(NoSuitableConstructorException.class);
    }

    @Test
    public void construct_WhenMultipleAnnotatedConstructors_ThenMultipleAnnotatedConstructorsException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.construct(ClassConstructorMultipleAnnotated.class))
                  .isInstanceOf(MultipleAnnotatedConstructorsException.class);
    }

    @Test
    public void construct_WhenNoPublicConstructors_ThenNoSuitableConstructorException()
    {
        Assertions.assertThatThrownBy(() -> testObject.construct(ClassConstructorPrivate.class))
                  .isInstanceOf(NoSuitableConstructorException.class);
    }

    // endregion
    // region construct [setter]

    @Test
    public void construct_WhenDependencySetterHasReturnType_ThenIncorrectDependencySetterException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.construct(ClassSetterIncorrectReturnType.class))
                  .isInstanceOf(IncorrectDependencySetterException.class);
    }

    @Test
    public void construct_WhenDependencySetterHasNoParameters_ThenIncorrectDependencySetterException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.construct(ClassSetterWithoutParameters.class))
                  .isInstanceOf(IncorrectDependencySetterException.class);
    }

    @Test
    public void construct_WhenDependencySetterHasIncorrectName_ThenIncorrectDependencySetterException()
    {
        Assertions.assertThatThrownBy(() -> testObject.construct(ClassSetterIncorrectName.class))
                  .isInstanceOf(IncorrectDependencySetterException.class);
    }

    @Test
    public void construct_WhenDependencySetterHasMultipleParameters_ThenIncorrectDependencySetterException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.construct(ClassSetterMultipleParameters.class))
                  .isInstanceOf(IncorrectDependencySetterException.class);
    }

    @Test
    public void construct_WhenMissingDependency_ThenMissingDependenciesException()
    {
        Assertions.assertThatThrownBy(() -> testObject.construct(ClassSetterSingle.class))
                  .isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void construct_WhenSetterThrowsException_ThenSetterInvocationException()
    {
        // given
        dictionary.addInstance(String.class, "string");

        // then
        Assertions.assertThatThrownBy(() -> testObject.construct(ClassSetterThrows.class))
                  .isInstanceOf(SetterInvocationException.class);
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
        Assertions.assertThat(result.getStringObject().getString()).isNotNull().isEqualTo(string);
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
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceDiamondTop.class, ClassDiamondTop.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceBasicSimpleDependency.class,
                           ClassConstructorNotAnnotatedWithDependency.class,
                           ConstructionPolicy.defaultPolicy);

        // when
        InterfaceBasicSimpleDependency result =
                testObject.construct(InterfaceBasicSimpleDependency.class);

        // then
        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(ClassConstructorNotAnnotatedWithDependency.class);
        Assertions.assertThat(result.getFirstObject()).isNotNull();
        Assertions.assertThat(result.getSecondObject()).isNotNull();
        Assertions.assertThat(result.getFirstObject().getObject()).isNotNull();
        Assertions.assertThat(result.getSecondObject().getString()).isNotNull().isEqualTo(string);
    }

    @Test
    public void construct_WhenDependenciesWithoutAnnotatedConstructorsWithAllDependencies_ThenInstanceIsResolved()
    {
        // given
        dictionary.addType(InterfaceBasic.class, ClassConstructorDefault.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceDiamondTop.class, ClassDiamondTop.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceBasicSimpleDependency.class,
                           ClassConstructorNotAnnotatedWithDependency.class,
                           ConstructionPolicy.defaultPolicy);

        // when
        InterfaceBasicSimpleDependency result =
                testObject.construct(InterfaceBasicSimpleDependency.class);

        // then
        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(ClassConstructorNotAnnotatedWithDependency.class);
        Assertions.assertThat(result.getFirstObject()).isNotNull();
        Assertions.assertThat(result.getSecondObject()).isNotNull();
        Assertions.assertThat(result.getFirstObject().getObject()).isNotNull();
        Assertions.assertThat(result.getSecondObject().getString()).isNotNull().isEqualTo("");
    }

    @Test
    public void construct_WhenDependenciesWithoutAnnotatedConstructorsWithoutSomeDependencies_ThenInstanceIsResolved()
    {
        // given
        dictionary.addType(InterfaceBasic.class, ClassConstructorDefault.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceDiamondTop.class, ClassDiamondTop.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceBasicSimpleDependency.class,
                           ClassConstructorNotAnnotatedWithDependency.class,
                           ConstructionPolicy.defaultPolicy);

        // when
        InterfaceBasicSimpleDependency result =
                testObject.construct(InterfaceBasicSimpleDependency.class);

        // then
        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(ClassConstructorNotAnnotatedWithDependency.class);
        Assertions.assertThat(result.getFirstObject()).isNotNull();
        Assertions.assertThat(result.getSecondObject()).isNull();
        Assertions.assertThat(result.getFirstObject().getObject()).isNotNull();
    }

    @Test
    public void construct_WhenDependenciesWithAnnotatedConstructor_ThenInstanceIsResolved()
    {
        // given
        dictionary.addType(InterfaceBasic.class, ClassConstructorDefault.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceDiamondTop.class, ClassDiamondTop.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceBasicSimpleDependency.class,
                           ClassConstructorAnnotatedWithDependency.class,
                           ConstructionPolicy.defaultPolicy);

        // when
        InterfaceBasicSimpleDependency result =
                testObject.construct(InterfaceBasicSimpleDependency.class);

        // then
        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(ClassConstructorAnnotatedWithDependency.class);
        Assertions.assertThat(result.getFirstObject()).isNotNull();
        Assertions.assertThat(result.getSecondObject()).isNotNull();
        Assertions.assertThat(result.getFirstObject().getObject()).isNotNull();
        Assertions.assertThat(result.getSecondObject().getString()).isNotNull().isEqualTo("");
    }

    @Test
    public void construct_WhenDependenciesWithAnnotatedConstructorWithoutSomeDependencies_ThenMissingDependenciesException()
    {
        // given
        dictionary.addType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceDiamondRight.class, ClassDiamondRight.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceDiamondBottom.class, ClassDiamondBottom.class,
                           ConstructionPolicy.defaultPolicy);

        // then
        Assertions.assertThatThrownBy(() -> testObject.construct(InterfaceDiamondBottom.class))
                  .isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void construct_WhenDiamondDependenciesWithoutSingleton_ThenInstanceIsResolved()
    {
        // given
        dictionary.addType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceDiamondRight.class, ClassDiamondRight.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceDiamondBottom.class, ClassDiamondBottom.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceDiamondTop.class, ClassDiamondTop.class,
                           ConstructionPolicy.defaultPolicy);

        // when
        InterfaceDiamondBottom result = testObject.construct(InterfaceDiamondBottom.class);

        // then
        Assertions.assertThat(result).isNotNull().isInstanceOf(ClassDiamondBottom.class);
        Assertions.assertThat(result.getDiamond1()).isNotNull();
        Assertions.assertThat(result.getDiamond2()).isNotNull();
        Assertions.assertThat(result.getDiamond1().getObject()).isNotNull();
        Assertions.assertThat(result.getDiamond2().getObject()).isNotNull();
        Assertions.assertThat(result.getDiamond1().getObject())
                  .isNotSameAs(result.getDiamond2().getObject());
    }

    @Test
    public void construct_WhenDiamondDependenciesWithSingleton_ThenInstanceIsResolved()
    {
        // given
        dictionary.addType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceDiamondRight.class, ClassDiamondRight.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceDiamondBottom.class, ClassDiamondBottom.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceDiamondTop.class, ClassDiamondTop.class,
                           ConstructionPolicy.SINGLETON);

        // when
        InterfaceDiamondBottom result = testObject.construct(InterfaceDiamondBottom.class);

        // then
        Assertions.assertThat(result).isNotNull().isInstanceOf(ClassDiamondBottom.class);
        Assertions.assertThat(result.getDiamond1()).isNotNull();
        Assertions.assertThat(result.getDiamond2()).isNotNull();
        Assertions.assertThat(result.getDiamond1().getObject()).isNotNull();
        Assertions.assertThat(result.getDiamond2().getObject())
                  .isNotNull()
                  .isSameAs(result.getDiamond1().getObject());
    }

    @Test
    public void construct_WhenCircularDependencies_ThenCircularDependenciesException()
    {
        // given
        dictionary.addType(InterfaceCircularLeft.class, ClassCircularLeft.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceCircularRight.class, ClassCircularRight.class,
                           ConstructionPolicy.defaultPolicy);

        // then
        Assertions.assertThatThrownBy(() -> testObject.construct(InterfaceCircularRight.class))
                  .isInstanceOf(CircularDependenciesException.class);
    }

    @Test
    public void construct_WhenCanOmitCircularDependencies_ThenInstanceIsResolved()
    {
        // given
        String string = "String";

        dictionary.addInstance(String.class, string);
        dictionary.addType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceCircularLeft.class, ClassCircularLeft.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceCircularRight.class, ClassCircularRight.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceCircularDependency.class, ClassCircularDependency.class,
                           ConstructionPolicy.defaultPolicy);

        // when
        InterfaceCircularDependency result =
                testObject.construct(InterfaceCircularDependency.class);

        // then
        Assertions.assertThat(result).isNotNull().isInstanceOf(ClassCircularDependency.class);
        Assertions.assertThat(result.getNonCircularObject()).isNotNull();
        Assertions.assertThat(result.getCircularObject()).isNull();
        Assertions.assertThat(result.getNonCircularObject().getString())
                  .isNotNull()
                  .isEqualTo(string);
    }

    @Test
    public void construct_WhenComplexDependency_ThenInstanceIsResolved()
    {
        // given
        String string = "string";

        dictionary.addType(InterfaceBasicComplexDependency.class, ClassBasicComplexDependency.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceBasic.class, ClassConstructorDefault.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceDiamondTop.class, ClassDiamondTop.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class,
                           ConstructionPolicy.defaultPolicy);
        dictionary.addInstance(String.class, string);

        // when
        InterfaceBasicComplexDependency result =
                testObject.construct(InterfaceBasicComplexDependency.class);

        // then
        Assertions.assertThat(result).isNotNull().isInstanceOf(ClassBasicComplexDependency.class);
        Assertions.assertThat(result.getBasicObject()).isNotNull();
        Assertions.assertThat(result.getFirstObject()).isNotNull();
        Assertions.assertThat(result.getSecondObject()).isNotNull();
        Assertions.assertThat(result.getFirstObject().getObject()).isNotNull();
        Assertions.assertThat(result.getSecondObject().getString()).isNotNull().isEqualTo(string);
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
        Assertions.assertThat(result1).isNotNull().isInstanceOf(ClassRegisterInterface.class);
        Assertions.assertThat(result2)
                  .isNotNull()
                  .isInstanceOf(ClassRegisterInterface.class)
                  .isNotSameAs(result1);
    }

    @Test
    public void construct_WhenAnnotatedAbstractClass_ThenInstanceIsResolved()
    {
        // when
        ClassRegisterAbstract result1 = testObject.construct(ClassRegisterAbstract.class);
        ClassRegisterAbstract result2 = testObject.construct(ClassRegisterAbstract.class);

        // then
        Assertions.assertThat(result1).isNotNull().isInstanceOf(ClassRegisterConcrete.class);
        Assertions.assertThat(result2)
                  .isNotNull()
                  .isInstanceOf(ClassRegisterConcrete.class)
                  .isNotSameAs(result1);
    }

    @Test
    public void construct_WhenAnnotatedConcreteClass_ThenInstanceIsResolved()
    {
        // when
        ClassRegisterConcrete result1 = testObject.construct(ClassRegisterConcrete.class);
        ClassRegisterConcrete result2 = testObject.construct(ClassRegisterConcrete.class);

        // then
        Assertions.assertThat(result1)
                  .isNotNull()
                  .isInstanceOf(ClassRegisterDerivedFromRegister.class);
        Assertions.assertThat(result2)
                  .isNotNull()
                  .isInstanceOf(ClassRegisterDerivedFromRegister.class)
                  .isNotSameAs(result1);
    }

    @Test
    public void construct_WhenSelfAnnotatedConcreteClass_ThenInstanceIsResolved()
    {
        // when
        ClassRegisterSelf result1 = testObject.construct(ClassRegisterSelf.class);
        ClassRegisterSelf result2 = testObject.construct(ClassRegisterSelf.class);

        // then
        Assertions.assertThat(result1).isNotNull();
        Assertions.assertThat(result2).isNotNull().isNotSameAs(result1);
    }

    @Test
    public void construct_WhenAnnotatedConcreteClassAsItself_ThenInstanceIsResolved()
    {
        // when
        ClassRegisterSelfAsSubtype result1 = testObject.construct(ClassRegisterSelfAsSubtype.class);
        ClassRegisterSelfAsSubtype result2 = testObject.construct(ClassRegisterSelfAsSubtype.class);

        // then
        Assertions.assertThat(result1).isNotNull();
        Assertions.assertThat(result2).isNotNull().isNotSameAs(result1);
    }

    @Test
    public void construct_WhenSelfAnnotatedInterface_ThenAbstractTypeException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.construct(InterfaceRegisterSelfIncorrect.class))
                  .isInstanceOf(AbstractTypeException.class);
    }

    @Test
    public void construct_WhenSelfAnnotatedAbstractClass_ThenAbstractTypeException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.construct(ClassRegisterSelfAbstractIncorrect.class))
                  .isInstanceOf(AbstractTypeException.class);
    }

    @Test
    public void construct_WhenAnnotatedAbstractClassAsItself_ThenAbstractTypeException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.construct(ClassRegisterAbstractIncorrect.class))
                  .isInstanceOf(AbstractTypeException.class);
    }

    @Test
    public void construct_WhenInterfaceAnnotatedClassNotImplementing_ThenNotDerivedTypeException()
    {
        Assertions.assertThatThrownBy(() -> testObject.construct(InterfaceRegisterIncorrect.class))
                  .isInstanceOf(NotDerivedTypeException.class);
    }

    @Test
    public void construct_WhenAnnotatedClassRegistersInterface_ThenNotDerivedTypeException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.construct(ClassRegisterInterfaceIncorrect.class))
                  .isInstanceOf(NotDerivedTypeException.class);
    }

    @Test
    public void construct_WhenAnnotatedClassRegistersNotDerivedClass_ThenNotDerivedTypeException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.construct(ClassRegisterIncorrectOtherClass.class))
                  .isInstanceOf(NotDerivedTypeException.class);
    }

    @Test
    public void construct_WhenAnnotatedClassRegistersAbstractConcreteSuperclass_ThenNotDerivedTypeException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.construct(ClassRegisterIncorrectSuperClass.class))
                  .isInstanceOf(NotDerivedTypeException.class);
    }

    @Test
    public void construct_WhenAnnotatedInterfaceSingleton_ThenSameInstances()
    {
        // when
        InterfaceRegisterSingleton result1 = testObject.construct(InterfaceRegisterSingleton.class);
        InterfaceRegisterSingleton result2 = testObject.construct(InterfaceRegisterSingleton.class);

        // then
        Assertions.assertThat(result1).isNotNull().isInstanceOf(ClassRegisterSingletonBase.class);
        Assertions.assertThat(result2)
                  .isNotNull()
                  .isInstanceOf(ClassRegisterSingletonBase.class)
                  .isSameAs(result1);
    }

    @Test
    public void construct_WhenAnnotatedConcreteClassSingleton_ThenSameInstances()
    {
        // when
        ClassRegisterSingletonBase result1 = testObject.construct(ClassRegisterSingletonBase.class);
        ClassRegisterSingletonBase result2 = testObject.construct(ClassRegisterSingletonBase.class);

        // then
        Assertions.assertThat(result1)
                  .isNotNull()
                  .isInstanceOf(ClassRegisterSingletonDerived.class);
        Assertions.assertThat(result2)
                  .isNotNull()
                  .isInstanceOf(ClassRegisterSingletonDerived.class)
                  .isSameAs(result1);
    }

    @Test
    public void construct_WhenSelfAnnotatedConcreteClassSingleton_ThenSameInstances()
    {
        // when
        ClassRegisterSelfSingleton result1 = testObject.construct(ClassRegisterSelfSingleton.class);
        ClassRegisterSelfSingleton result2 = testObject.construct(ClassRegisterSelfSingleton.class);

        // then
        Assertions.assertThat(result1).isNotNull();
        Assertions.assertThat(result2).isNotNull().isSameAs(result1);
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
        Assertions.assertThat(result2).isNotNull().isSameAs(result1);
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
        Assertions.assertThat(result.getStringObject().getString()).isNotNull().isEqualTo(string);
    }

    // endregion
}
