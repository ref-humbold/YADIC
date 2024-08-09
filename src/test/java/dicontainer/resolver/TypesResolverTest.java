package dicontainer.resolver;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dicontainer.ConstructionPolicy;
import dicontainer.models.basic.*;
import dicontainer.models.circular.*;
import dicontainer.models.constructor.*;
import dicontainer.models.diamond.*;
import dicontainer.models.register.*;
import dicontainer.models.setter.*;
import dicontainer.registry.DependencyRegistry;
import dicontainer.registry.exception.AbstractTypeException;
import dicontainer.registry.exception.NotDerivedTypeException;
import dicontainer.resolver.exception.*;

public class TypesResolverTest
{
    private DependencyRegistry dictionary;
    private TypesResolver testObject;

    @BeforeEach
    public void setUp()
    {
        dictionary = new DependencyRegistry();
        testObject = new TypesResolver(dictionary);
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
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void resolve_WhenClassInheritsFromConcreteClass_ThenInstanceIsResolved()
    {
        // when
        ClassConstructorSuperParameterized result =
                testObject.resolve(ClassConstructorSuperParameterized.class);

        // then
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void resolve_WhenClassInheritsFromAbstractClass_ThenInstanceIsResolved()
    {
        // when
        ClassBasicInheritsFromAbstract result =
                testObject.resolve(ClassBasicInheritsFromAbstract.class);

        // then
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void resolve_WhenClassHasParameterConstructorWithoutRegisteredParameter_ThenMissingDependenciesException()
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(ClassConstructorParameterized.class))
                  .isInstanceOf(MissingDependenciesException.class);
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
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getNumber()).isEqualTo(number);
    }

    @Test
    public void resolve_WhenClassHasPrimitiveParameterConstructorButRegisteredReferenceParameter_ThenMissingDependenciesException()
    {
        // given
        Integer number = 10;

        dictionary.addInstance(Integer.class, number);

        // then
        Assertions.assertThatThrownBy(() -> testObject.resolve(ClassConstructorParameterized.class))
                  .isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void resolve_WhenClassHasDefaultAndParameterConstructorWithoutRegisteredParameter_ThenInstanceIsResolved()
    {
        // when
        ClassConstructorDefaultAndParameterized result =
                testObject.resolve(ClassConstructorDefaultAndParameterized.class);

        // then
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void resolve_WhenInterface_ThenMissingDependenciesException()
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(InterfaceBasic.class))
                  .isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void resolve_WhenAbstractClass_ThenMissingDependenciesException()
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(ClassBasicAbstract.class))
                  .isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void resolve_WhenClassConstructorThrowsException_ThenNoInstanceCreatedException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.resolve(ClassConstructorExceptionThrown.class))
                  .isInstanceOf(NoInstanceCreatedException.class);
    }

    @Test
    public void resolve_WhenPrimitiveType_ThenNoSuitableConstructorException()
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(double.class))
                  .isInstanceOf(NoSuitableConstructorException.class);
    }

    @Test
    public void resolve_WhenMultipleAnnotatedConstructors_ThenMultipleAnnotatedConstructorsException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.resolve(ClassConstructorMultipleAnnotated.class))
                  .isInstanceOf(MultipleAnnotatedConstructorsException.class);
    }

    @Test
    public void resolve_WhenNoPublicConstructors_ThenNoSuitableConstructorException()
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(ClassConstructorPrivate.class))
                  .isInstanceOf(NoSuitableConstructorException.class);
    }

    // endregion
    // region resolve [setter]

    @Test
    public void resolve_WhenDependencySetterHasReturnType_ThenIncorrectDependencySetterException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.resolve(ClassSetterIncorrectReturnType.class))
                  .isInstanceOf(IncorrectDependencySetterException.class);
    }

    @Test
    public void resolve_WhenDependencySetterHasNoParameters_ThenIncorrectDependencySetterException()
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(ClassSetterWithoutParameters.class))
                  .isInstanceOf(IncorrectDependencySetterException.class);
    }

    @Test
    public void resolve_WhenDependencySetterHasIncorrectName_ThenIncorrectDependencySetterException()
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(ClassSetterIncorrectName.class))
                  .isInstanceOf(IncorrectDependencySetterException.class);
    }

    @Test
    public void resolve_WhenDependencySetterHasMultipleParameters_ThenIncorrectDependencySetterException()
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(ClassSetterMultipleParameters.class))
                  .isInstanceOf(IncorrectDependencySetterException.class);
    }

    @Test
    public void resolve_WhenMissingDependency_ThenMissingDependenciesException()
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(ClassSetterSingle.class))
                  .isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void resolve_WhenSetterThrowsException_ThenSetterInvocationException()
    {
        // given
        dictionary.addInstance(String.class, "string");

        // then
        Assertions.assertThatThrownBy(() -> testObject.resolve(ClassSetterThrows.class))
                  .isInstanceOf(SetterInvocationException.class);
    }

    @Test
    public void resolve_WhenDependencySetterOnly_ThenInstanceIsResolved()
    {
        // given
        dictionary.addType(InterfaceBasic.class, ClassConstructorDefault.class,
                           ConstructionPolicy.CONSTRUCTION);

        // when
        InterfaceSetter result = testObject.resolve(ClassSetterSingle.class);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getBasicObject()).isNotNull();
    }

    @Test
    public void resolve_WhenMultipleDependencySetters_ThenInstanceIsResolved()
    {
        // given
        String string = "string";

        dictionary.addType(InterfaceSetterMultiple.class, ClassSetterMultiple.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceBasic.class, ClassConstructorDefault.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addInstance(String.class, string);

        // when
        InterfaceSetterMultiple result = testObject.resolve(InterfaceSetterMultiple.class);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getBasicObject()).isNotNull();
        Assertions.assertThat(result.getStringObject()).isNotNull();
        Assertions.assertThat(result.getStringObject().getString()).isNotNull().isEqualTo(string);
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
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceDiamondTop.class, ClassDiamondTop.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceBasicSimpleDependency.class,
                           ClassConstructorNotAnnotatedWithDependency.class,
                           ConstructionPolicy.CONSTRUCTION);

        // when
        InterfaceBasicSimpleDependency result =
                testObject.resolve(InterfaceBasicSimpleDependency.class);

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
    public void resolve_WhenDependenciesWithoutAnnotatedConstructorsWithAllDependencies_ThenInstanceIsResolved()
    {
        // given
        dictionary.addType(InterfaceBasic.class, ClassConstructorDefault.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceDiamondTop.class, ClassDiamondTop.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceBasicSimpleDependency.class,
                           ClassConstructorNotAnnotatedWithDependency.class,
                           ConstructionPolicy.CONSTRUCTION);

        // when
        InterfaceBasicSimpleDependency result =
                testObject.resolve(InterfaceBasicSimpleDependency.class);

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
    public void resolve_WhenDependenciesWithoutAnnotatedConstructorsWithoutSomeDependencies_ThenInstanceIsResolved()
    {
        // given
        dictionary.addType(InterfaceBasic.class, ClassConstructorDefault.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceDiamondTop.class, ClassDiamondTop.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceBasicSimpleDependency.class,
                           ClassConstructorNotAnnotatedWithDependency.class,
                           ConstructionPolicy.CONSTRUCTION);

        // when
        InterfaceBasicSimpleDependency result =
                testObject.resolve(InterfaceBasicSimpleDependency.class);

        // then
        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(ClassConstructorNotAnnotatedWithDependency.class);
        Assertions.assertThat(result.getFirstObject()).isNotNull();
        Assertions.assertThat(result.getSecondObject()).isNull();
        Assertions.assertThat(result.getFirstObject().getObject()).isNotNull();
    }

    @Test
    public void resolve_WhenDependenciesWithAnnotatedConstructor_ThenInstanceIsResolved()
    {
        // given
        dictionary.addType(InterfaceBasic.class, ClassConstructorDefault.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceDiamondTop.class, ClassDiamondTop.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceBasicSimpleDependency.class,
                           ClassConstructorAnnotatedWithDependency.class,
                           ConstructionPolicy.CONSTRUCTION);

        // when
        InterfaceBasicSimpleDependency result =
                testObject.resolve(InterfaceBasicSimpleDependency.class);

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
    public void resolve_WhenDependenciesWithAnnotatedConstructorWithoutSomeDependencies_ThenMissingDependenciesException()
    {
        // given
        dictionary.addType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceDiamondRight.class, ClassDiamondRight.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceDiamondBottom.class, ClassDiamondBottom.class,
                           ConstructionPolicy.CONSTRUCTION);

        // then
        Assertions.assertThatThrownBy(() -> testObject.resolve(InterfaceDiamondBottom.class))
                  .isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void resolve_WhenDiamondDependenciesWithoutSingleton_ThenInstanceIsResolved()
    {
        // given
        dictionary.addType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceDiamondRight.class, ClassDiamondRight.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceDiamondBottom.class, ClassDiamondBottom.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceDiamondTop.class, ClassDiamondTop.class,
                           ConstructionPolicy.CONSTRUCTION);

        // when
        InterfaceDiamondBottom result = testObject.resolve(InterfaceDiamondBottom.class);

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
    public void resolve_WhenDiamondDependenciesWithSingleton_ThenInstanceIsResolved()
    {
        // given
        dictionary.addType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceDiamondRight.class, ClassDiamondRight.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceDiamondBottom.class, ClassDiamondBottom.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceDiamondTop.class, ClassDiamondTop.class,
                           ConstructionPolicy.SINGLETON);

        // when
        InterfaceDiamondBottom result = testObject.resolve(InterfaceDiamondBottom.class);

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
    public void resolve_WhenCircularDependencies_ThenCircularDependenciesException()
    {
        // given
        dictionary.addType(InterfaceCircularLeft.class, ClassCircularLeft.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceCircularRight.class, ClassCircularRight.class,
                           ConstructionPolicy.CONSTRUCTION);

        // then
        Assertions.assertThatThrownBy(() -> testObject.resolve(InterfaceCircularRight.class))
                  .isInstanceOf(CircularDependenciesException.class);
    }

    @Test
    public void resolve_WhenCanOmitCircularDependencies_ThenInstanceIsResolved()
    {
        // given
        String string = "String";

        dictionary.addInstance(String.class, string);
        dictionary.addType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceCircularLeft.class, ClassCircularLeft.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceCircularRight.class, ClassCircularRight.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceCircularDependency.class, ClassCircularDependency.class,
                           ConstructionPolicy.CONSTRUCTION);

        // when
        InterfaceCircularDependency result = testObject.resolve(InterfaceCircularDependency.class);

        // then
        Assertions.assertThat(result).isNotNull().isInstanceOf(ClassCircularDependency.class);
        Assertions.assertThat(result.getNonCircularObject()).isNotNull();
        Assertions.assertThat(result.getCircularObject()).isNull();
        Assertions.assertThat(result.getNonCircularObject().getString())
                  .isNotNull()
                  .isEqualTo(string);
    }

    @Test
    public void resolve_WhenComplexDependency_ThenInstanceIsResolved()
    {
        // given
        String string = "string";

        dictionary.addType(InterfaceBasicComplexDependency.class, ClassBasicComplexDependency.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceBasic.class, ClassConstructorDefault.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceDiamondTop.class, ClassDiamondTop.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class,
                           ConstructionPolicy.CONSTRUCTION);
        dictionary.addInstance(String.class, string);

        // when
        InterfaceBasicComplexDependency result =
                testObject.resolve(InterfaceBasicComplexDependency.class);

        // then
        Assertions.assertThat(result).isNotNull().isInstanceOf(ClassBasicComplexDependency.class);
        Assertions.assertThat(result.getBasicObject()).isNotNull();
        Assertions.assertThat(result.getFirstObject()).isNotNull();
        Assertions.assertThat(result.getSecondObject()).isNotNull();
        Assertions.assertThat(result.getFirstObject().getObject()).isNotNull();
        Assertions.assertThat(result.getSecondObject().getString()).isNotNull().isEqualTo(string);
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
        Assertions.assertThat(result1).isNotNull().isInstanceOf(ClassRegisterInterface.class);
        Assertions.assertThat(result2)
                  .isNotNull()
                  .isInstanceOf(ClassRegisterInterface.class)
                  .isNotSameAs(result1);
    }

    @Test
    public void resolve_WhenAnnotatedAbstractClass_ThenInstanceIsResolved()
    {
        // when
        ClassRegisterAbstract result1 = testObject.resolve(ClassRegisterAbstract.class);
        ClassRegisterAbstract result2 = testObject.resolve(ClassRegisterAbstract.class);

        // then
        Assertions.assertThat(result1).isNotNull().isInstanceOf(ClassRegisterConcrete.class);
        Assertions.assertThat(result2)
                  .isNotNull()
                  .isInstanceOf(ClassRegisterConcrete.class)
                  .isNotSameAs(result1);
    }

    @Test
    public void resolve_WhenAnnotatedConcreteClass_ThenInstanceIsResolved()
    {
        // when
        ClassRegisterConcrete result1 = testObject.resolve(ClassRegisterConcrete.class);
        ClassRegisterConcrete result2 = testObject.resolve(ClassRegisterConcrete.class);

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
    public void resolve_WhenSelfAnnotatedConcreteClass_ThenInstanceIsResolved()
    {
        // when
        ClassRegisterSelf result1 = testObject.resolve(ClassRegisterSelf.class);
        ClassRegisterSelf result2 = testObject.resolve(ClassRegisterSelf.class);

        // then
        Assertions.assertThat(result1).isNotNull();
        Assertions.assertThat(result2).isNotNull().isNotSameAs(result1);
    }

    @Test
    public void resolve_WhenAnnotatedConcreteClassAsItself_ThenInstanceIsResolved()
    {
        // when
        ClassRegisterSelfAsSubtype result1 = testObject.resolve(ClassRegisterSelfAsSubtype.class);
        ClassRegisterSelfAsSubtype result2 = testObject.resolve(ClassRegisterSelfAsSubtype.class);

        // then
        Assertions.assertThat(result1).isNotNull();
        Assertions.assertThat(result2).isNotNull().isNotSameAs(result1);
    }

    @Test
    public void resolve_WhenSelfAnnotatedInterface_ThenAbstractTypeException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.resolve(InterfaceRegisterSelfIncorrect.class))
                  .isInstanceOf(AbstractTypeException.class);
    }

    @Test
    public void resolve_WhenSelfAnnotatedAbstractClass_ThenAbstractTypeException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.resolve(ClassRegisterSelfAbstractIncorrect.class))
                  .isInstanceOf(AbstractTypeException.class);
    }

    @Test
    public void resolve_WhenAnnotatedAbstractClassAsItself_ThenAbstractTypeException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.resolve(ClassRegisterAbstractIncorrect.class))
                  .isInstanceOf(AbstractTypeException.class);
    }

    @Test
    public void resolve_WhenInterfaceAnnotatedClassNotImplementing_ThenNotDerivedTypeException()
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(InterfaceRegisterIncorrect.class))
                  .isInstanceOf(NotDerivedTypeException.class);
    }

    @Test
    public void resolve_WhenAnnotatedClassRegistersInterface_ThenNotDerivedTypeException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.resolve(ClassRegisterInterfaceIncorrect.class))
                  .isInstanceOf(NotDerivedTypeException.class);
    }

    @Test
    public void resolve_WhenAnnotatedClassRegistersNotDerivedClass_ThenNotDerivedTypeException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.resolve(ClassRegisterIncorrectOtherClass.class))
                  .isInstanceOf(NotDerivedTypeException.class);
    }

    @Test
    public void resolve_WhenAnnotatedClassRegistersAbstractConcreteSuperclass_ThenNotDerivedTypeException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.resolve(ClassRegisterIncorrectSuperClass.class))
                  .isInstanceOf(NotDerivedTypeException.class);
    }

    @Test
    public void resolve_WhenAnnotatedInterfaceSingleton_ThenSameInstances()
    {
        // when
        InterfaceRegisterSingleton result1 = testObject.resolve(InterfaceRegisterSingleton.class);
        InterfaceRegisterSingleton result2 = testObject.resolve(InterfaceRegisterSingleton.class);

        // then
        Assertions.assertThat(result1).isNotNull().isInstanceOf(ClassRegisterSingletonBase.class);
        Assertions.assertThat(result2)
                  .isNotNull()
                  .isInstanceOf(ClassRegisterSingletonBase.class)
                  .isSameAs(result1);
    }

    @Test
    public void resolve_WhenAnnotatedConcreteClassSingleton_ThenSameInstances()
    {
        // when
        ClassRegisterSingletonBase result1 = testObject.resolve(ClassRegisterSingletonBase.class);
        ClassRegisterSingletonBase result2 = testObject.resolve(ClassRegisterSingletonBase.class);

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
    public void resolve_WhenSelfAnnotatedConcreteClassSingleton_ThenSameInstances()
    {
        // when
        ClassRegisterSelfSingleton result1 = testObject.resolve(ClassRegisterSelfSingleton.class);
        ClassRegisterSelfSingleton result2 = testObject.resolve(ClassRegisterSelfSingleton.class);

        // then
        Assertions.assertThat(result1).isNotNull();
        Assertions.assertThat(result2).isNotNull().isSameAs(result1);
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
        Assertions.assertThat(result1).isNotNull();
        Assertions.assertThat(result2).isNotNull().isSameAs(result1);
    }

    // endregion
}
