package yadic;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import yadic.models.basic.ClassBasicAbstract;
import yadic.models.basic.ClassBasicInheritsFromAbstract;
import yadic.models.basic.ClassBasicStringGetter;
import yadic.models.basic.InterfaceBasic;
import yadic.models.basic.InterfaceBasicStringGetter;
import yadic.models.constructor.*;
import yadic.models.setter.*;
import yadic.registry.exception.AbstractTypeException;
import yadic.resolver.exception.IncorrectDependencySetterException;
import yadic.resolver.exception.MultipleAnnotatedConstructorsException;
import yadic.resolver.exception.NoSuitableConstructorException;

public class DiContainerTest
{
    private DiContainer testObject;

    @BeforeEach
    public void setUp()
    {
        testObject = new DiContainer();
    }

    @AfterEach
    public void tearDown()
    {
        testObject = null;
    }

    // region registerType (single class)

    @Test
    public void registerType_WhenSingleClass_ThenDifferentInstances()
    {
        // given
        testObject.registerType(ClassConstructorDefault.class, ConstructionPolicy.CONSTRUCTION);

        // when
        ClassConstructorDefault result1 = testObject.resolve(ClassConstructorDefault.class);
        ClassConstructorDefault result2 = testObject.resolve(ClassConstructorDefault.class);

        // then
        Assertions.assertThat(result1).isNotNull();
        Assertions.assertThat(result2).isNotNull().isNotSameAs(result1);
    }

    @Test
    public void registerType_WhenSingleClassAsSingleton_ThenSameInstance()
    {
        // given
        testObject.registerType(ClassConstructorDefault.class, ConstructionPolicy.SINGLETON);

        // when
        ClassConstructorDefault result1 = testObject.resolve(ClassConstructorDefault.class);
        ClassConstructorDefault result2 = testObject.resolve(ClassConstructorDefault.class);

        // then
        Assertions.assertThat(result1).isNotNull();
        Assertions.assertThat(result2).isNotNull().isSameAs(result1);
    }

    @Test
    public void registerType_WhenSingleClassChangesSingleton_ThenChangesInstances()
    {
        // given 1
        testObject.registerType(ClassConstructorDefault.class, ConstructionPolicy.SINGLETON);

        // when 1
        ClassConstructorDefault result11 = testObject.resolve(ClassConstructorDefault.class);
        ClassConstructorDefault result12 = testObject.resolve(ClassConstructorDefault.class);

        // then 1
        Assertions.assertThat(result11).isNotNull();
        Assertions.assertThat(result12).isNotNull().isSameAs(result11);

        // given 2
        testObject.registerType(ClassConstructorDefault.class, ConstructionPolicy.CONSTRUCTION);

        // when 2
        ClassConstructorDefault result21 = testObject.resolve(ClassConstructorDefault.class);
        ClassConstructorDefault result22 = testObject.resolve(ClassConstructorDefault.class);

        // then 2
        Assertions.assertThat(result21).isNotNull();
        Assertions.assertThat(result22).isNotNull().isNotSameAs(result21);
    }

    @Test
    public void registerType_WhenSingleClassIsInterface_ThenAbstractTypeException()
    {
        Assertions.assertThatThrownBy(() -> testObject.registerType(InterfaceBasic.class,
                                                                    ConstructionPolicy.CONSTRUCTION))
                  .isInstanceOf(AbstractTypeException.class);
    }

    @Test
    public void registerType_WhenSingleClassIsAbstractClass_ThenAbstractTypeException()
    {
        Assertions.assertThatThrownBy(() -> testObject.registerType(ClassBasicAbstract.class,
                                                                    ConstructionPolicy.CONSTRUCTION))
                  .isInstanceOf(AbstractTypeException.class);
    }

    // endregion
    // region registerType [inheritance]

    @Test
    public void registerType_WhenInheritanceFromInterface_ThenDifferentInstances()
    {
        // given
        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class,
                                ConstructionPolicy.CONSTRUCTION);

        // when
        InterfaceBasic result1 = testObject.resolve(InterfaceBasic.class);
        InterfaceBasic result2 = testObject.resolve(InterfaceBasic.class);

        // then
        Assertions.assertThat(result1).isNotNull().isInstanceOf(ClassConstructorDefault.class);
        Assertions.assertThat(result2)
                  .isNotNull()
                  .isNotSameAs(result1)
                  .isInstanceOf(ClassConstructorDefault.class);
    }

    @Test
    public void registerType_WhenInheritanceFromInterfaceAsSingleton_ThenSameInstances()
    {
        // given
        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class,
                                ConstructionPolicy.SINGLETON);

        // when
        InterfaceBasic result1 = testObject.resolve(InterfaceBasic.class);
        InterfaceBasic result2 = testObject.resolve(InterfaceBasic.class);

        // then
        Assertions.assertThat(result1).isNotNull().isInstanceOf(ClassConstructorDefault.class);
        Assertions.assertThat(result2)
                  .isNotNull()
                  .isSameAs(result1)
                  .isInstanceOf(ClassConstructorDefault.class);
    }

    @Test
    public void registerType_WhenInheritanceFromInterfaceChangesSingleton_ThenChangeInstances()
    {
        // given 1
        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class,
                                ConstructionPolicy.SINGLETON);

        // when 1
        InterfaceBasic result11 = testObject.resolve(InterfaceBasic.class);
        InterfaceBasic result12 = testObject.resolve(InterfaceBasic.class);

        // then 1
        Assertions.assertThat(result11).isNotNull().isInstanceOf(ClassConstructorDefault.class);
        Assertions.assertThat(result12)
                  .isNotNull()
                  .isSameAs(result11)
                  .isInstanceOf(ClassConstructorDefault.class);

        // given 2
        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class,
                                ConstructionPolicy.CONSTRUCTION);

        // when 2
        InterfaceBasic result21 = testObject.resolve(InterfaceBasic.class);
        InterfaceBasic result22 = testObject.resolve(InterfaceBasic.class);

        // then 2
        Assertions.assertThat(result21).isNotNull().isInstanceOf(ClassConstructorDefault.class);
        Assertions.assertThat(result22)
                  .isNotNull()
                  .isNotSameAs(result21)
                  .isInstanceOf(ClassConstructorDefault.class);
    }

    @Test
    public void registerType_WhenInheritanceFromInterfaceChangesClass_ThenInstanceIsDerived()
    {
        // given 1
        testObject.registerType(InterfaceBasic.class, ClassConstructorDefault.class,
                                ConstructionPolicy.CONSTRUCTION);

        // when 1
        InterfaceBasic result1 = testObject.resolve(InterfaceBasic.class);

        // then 1
        Assertions.assertThat(result1).isNotNull().isInstanceOf(ClassConstructorDefault.class);

        // given 2
        testObject.registerType(InterfaceBasic.class, ClassConstructorDefaultAndParameterized.class,
                                ConstructionPolicy.CONSTRUCTION);

        // when 2
        InterfaceBasic result3 = testObject.resolve(InterfaceBasic.class);

        // then 2
        Assertions.assertThat(result3)
                  .isNotNull()
                  .isInstanceOf(ClassConstructorDefaultAndParameterized.class);
    }

    @Test
    public void registerType_WhenInheritanceFromAbstractClass_ThenInstanceIsDerived()
    {
        // given
        testObject.registerType(ClassBasicAbstract.class, ClassBasicInheritsFromAbstract.class,
                                ConstructionPolicy.CONSTRUCTION);

        // when
        ClassBasicAbstract result = testObject.resolve(ClassBasicAbstract.class);

        // then
        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(ClassBasicInheritsFromAbstract.class);
    }

    @Test
    public void registerType_WhenInheritanceFromConcreteClass_ThenInstanceIsDerived()
    {
        // given
        testObject.registerType(ClassConstructorParameterized.class,
                                ClassConstructorSuperParameterized.class,
                                ConstructionPolicy.CONSTRUCTION);

        // when
        ClassConstructorParameterized result =
                testObject.resolve(ClassConstructorParameterized.class);

        // then
        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(ClassConstructorSuperParameterized.class);
    }

    @Test
    public void registerType_WhenTwoStepsOfHierarchy_ThenInstanceIsDerived()
    {
        // given
        DiContainer diContainer =
                testObject.registerType(InterfaceBasic.class, ClassBasicAbstract.class,
                                        ConstructionPolicy.CONSTRUCTION);
        diContainer.registerType(ClassBasicAbstract.class, ClassBasicInheritsFromAbstract.class,
                                 ConstructionPolicy.CONSTRUCTION);

        // when
        InterfaceBasic result = testObject.resolve(InterfaceBasic.class);

        // then
        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(ClassBasicInheritsFromAbstract.class);
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
        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(ClassConstructorDefault.class)
                  .isSameAs(instance);
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
        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(ClassBasicInheritsFromAbstract.class)
                  .isSameAs(instance);
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
        Assertions.assertThat(result).isNotNull().isSameAs(instance);
        Assertions.assertThat(result.getText()).isEqualTo(instance.getText());
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
        Assertions.assertThat(result)
                  .isNotNull()
                  .isSameAs(instance)
                  .isInstanceOf(ClassConstructorSuperParameterized.class);
        Assertions.assertThat(result.getNumber()).isEqualTo(instance.getNumber());
    }

    @Test
    public void registerInstance_WhenInstanceIsNull_ThenNullPointerException()
    {
        Assertions.assertThatThrownBy(
                () -> testObject.registerInstance(ClassConstructorDefaultAndParameterized.class,
                                                  null)).isInstanceOf(NullPointerException.class);
    }

    // endregion
    // region resolve (@Dependency)

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
    public void resolve_WhenDependencySetterNameDoesNotStartWithSet_ThenIncorrectDependencySetterException()
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(ClassSetterIncorrectName.class))
                  .isInstanceOf(IncorrectDependencySetterException.class);
    }

    @Test
    public void resolve_WhenDependencySetterOnly_ThenInstanceIsResolved()
    {
        // given
        DiContainer diContainer =
                testObject.registerType(InterfaceSetter.class, ClassSetterSingle.class,
                                        ConstructionPolicy.CONSTRUCTION);
        diContainer.registerType(InterfaceBasic.class, ClassConstructorDefault.class,
                                 ConstructionPolicy.CONSTRUCTION);

        // when
        InterfaceSetter result = testObject.resolve(InterfaceSetter.class);

        // then
        Assertions.assertThat(result).isNotNull().isInstanceOf(ClassSetterSingle.class);
        Assertions.assertThat(result.getBasicObject()).isNotNull();
    }

    @Test
    public void resolve_WhenDependencySetterAndConstructor_ThenInstanceIsResolved()
    {
        // given
        DiContainer diContainer =
                testObject.registerType(InterfaceSetter.class, ClassSetterConstructor.class,
                                        ConstructionPolicy.CONSTRUCTION);
        diContainer.registerType(InterfaceBasic.class, ClassConstructorDefault.class,
                                 ConstructionPolicy.CONSTRUCTION);

        // when
        InterfaceSetter result = testObject.resolve(InterfaceSetter.class);

        // then
        Assertions.assertThat(result).isNotNull().isInstanceOf(ClassSetterConstructor.class);
        Assertions.assertThat(result.getBasicObject()).isNotNull();
    }

    @Test
    public void resolve_WhenDoubleDependencySetter_ThenIncorrectDependencySetterException()
    {
        // given
        testObject.registerType(InterfaceSetterMultipleParameters.class,
                                ClassSetterMultipleParameters.class,
                                ConstructionPolicy.CONSTRUCTION);

        // then
        Assertions.assertThatThrownBy(
                          () -> testObject.resolve(InterfaceSetterMultipleParameters.class))
                  .isInstanceOf(IncorrectDependencySetterException.class);
    }

    @Test
    public void resolve_WhenMultipleDependencySetters_ThenInstanceIsResolved()
    {
        // given
        String string = "string";

        DiContainer diContainer = testObject.registerInstance(String.class, string);
        DiContainer diContainer2 =
                diContainer.registerType(InterfaceSetterMultiple.class, ClassSetterMultiple.class,
                                         ConstructionPolicy.CONSTRUCTION);
        DiContainer diContainer1 =
                diContainer2.registerType(InterfaceBasic.class, ClassConstructorDefault.class,
                                          ConstructionPolicy.CONSTRUCTION);
        diContainer1.registerType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class,
                                  ConstructionPolicy.CONSTRUCTION);

        // when
        InterfaceSetterMultiple result = testObject.resolve(InterfaceSetterMultiple.class);

        // then
        Assertions.assertThat(result).isNotNull().isInstanceOf(ClassSetterMultiple.class);
        Assertions.assertThat(result.getBasicObject()).isNotNull();
        Assertions.assertThat(result.getStringObject()).isNotNull();
        Assertions.assertThat(result.getStringObject().getString()).isNotNull().isEqualTo(string);
    }

    // endregion
}
