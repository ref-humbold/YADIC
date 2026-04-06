package com.github.refhumbold.yadic;

import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import com.github.refhumbold.yadic.models.annotations.register.ClassDerivedFromRegisterConstruction;
import com.github.refhumbold.yadic.models.annotations.register.ClassDerivedFromRegisterSingleton;
import com.github.refhumbold.yadic.models.annotations.register.ClassRegister;
import com.github.refhumbold.yadic.models.annotations.register.ClassRegisterConstruction;
import com.github.refhumbold.yadic.models.annotations.register.ClassRegisterSingleton;
import com.github.refhumbold.yadic.models.annotations.registerself.ClassRegisterSelf;
import com.github.refhumbold.yadic.models.annotations.registerself.ClassRegisterSelfConstruction;
import com.github.refhumbold.yadic.models.annotations.registerself.ClassRegisterSelfSingleton;
import com.github.refhumbold.yadic.models.constructors.ClassDefaultConstructorOnly;
import com.github.refhumbold.yadic.models.constructors.ClassParameterizedConstructorBoxed;
import com.github.refhumbold.yadic.models.constructors.ClassParameterizedConstructorPrimitive;
import com.github.refhumbold.yadic.models.constructors.ClassParameterizedConstructorString;
import com.github.refhumbold.yadic.models.constructors.annotation.ClassAnnotatedConstructor;
import com.github.refhumbold.yadic.models.constructors.annotation.ClassAnnotatedConstructorOptionalCircular;
import com.github.refhumbold.yadic.models.constructors.comparator.ClassAnnotatedConstructorsSorting;
import com.github.refhumbold.yadic.models.constructors.comparator.ClassConstructorsSorting;
import com.github.refhumbold.yadic.models.dependencies.circular.ClassCircular;
import com.github.refhumbold.yadic.models.dependencies.circular.ClassCircularLeft;
import com.github.refhumbold.yadic.models.dependencies.circular.ClassCircularRight;
import com.github.refhumbold.yadic.models.dependencies.linear.ClassLinear;
import com.github.refhumbold.yadic.models.dependencies.linear.ClassLinearFirst;
import com.github.refhumbold.yadic.models.dependencies.linear.ClassLinearSecond;
import com.github.refhumbold.yadic.models.dependencies.linear.ClassLinearThird;
import com.github.refhumbold.yadic.models.inheritance.ClassAbstract;
import com.github.refhumbold.yadic.models.inheritance.ClassConcrete;
import com.github.refhumbold.yadic.models.inheritance.ClassConcreteDerived;
import com.github.refhumbold.yadic.models.inheritance.InterfaceInheritance;
import com.github.refhumbold.yadic.models.setter.ClassSetterWithConstructor;
import com.github.refhumbold.yadic.registry.exception.AbstractTypeException;
import com.github.refhumbold.yadic.registry.exception.RegistrationException;
import com.github.refhumbold.yadic.resolver.exception.MissingDependenciesException;

public class YadicContainerTest
{
    private YadicContainer testObject;

    @BeforeEach
    public void setUp()
    {
        testObject = new YadicContainer();
    }

    // region registerType & registerType/resolve [same type]

    @AfterEach
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void registerType_resolve_WhenTypeWithConstructionPolicy_ThenDifferentInstances()
    {
        // given
        Class<ClassConcrete> type = ClassConcrete.class;

        // when
        testObject.registerType(type, ConstructionPolicy.CONSTRUCTION);

        ClassConcrete result1 = testObject.resolve(type);
        ClassConcrete result2 = testObject.resolve(type);

        // then
        Assertions.assertThat(result1).isNotNull();
        Assertions.assertThat(result2).isNotNull().isNotSameAs(result1);
    }

    @Test
    public void registerType_resolve_WhenTypeWithSingletonPolicy_ThenSameInstance()
    {
        // given
        Class<ClassConcrete> type = ClassConcrete.class;

        // when
        testObject.registerType(type, ConstructionPolicy.SINGLETON);

        ClassConcrete result1 = testObject.resolve(type);
        ClassConcrete result2 = testObject.resolve(type);

        // then
        Assertions.assertThat(result1).isNotNull();
        Assertions.assertThat(result2).isNotNull().isSameAs(result1);
    }

    @Test
    public void registerType_resolve_WhenTypeRegisteredWithDifferentPolicy_ThenChangesInstances()
    {
        // given
        Class<ClassConcrete> type = ClassConcrete.class;

        // when 1
        testObject.registerType(type, ConstructionPolicy.SINGLETON);

        ClassConcrete result11 = testObject.resolve(type);
        ClassConcrete result12 = testObject.resolve(type);

        // then 1
        Assertions.assertThat(result11).isNotNull();
        Assertions.assertThat(result12).isNotNull().isSameAs(result11);

        // when 2
        testObject.registerType(type, ConstructionPolicy.CONSTRUCTION);

        ClassConcrete result21 = testObject.resolve(type);
        ClassConcrete result22 = testObject.resolve(type);

        // then 2
        Assertions.assertThat(result21).isNotNull();
        Assertions.assertThat(result22).isNotNull().isNotSameAs(result21);
    }

    @Test
    public void registerType_resolve_WhenTypeDependencyWithVariousPolicies_ThenResolveWithPolicies()
    {
        // when
        testObject.registerType(ClassLinear.class, ConstructionPolicy.CONSTRUCTION)
                  .registerType(ClassLinearFirst.class, ConstructionPolicy.SINGLETON)
                  .registerType(ClassLinearSecond.class, ConstructionPolicy.CONSTRUCTION)
                  .registerType(ClassLinearThird.class, ConstructionPolicy.SINGLETON);

        ClassLinear result1 = testObject.resolve(ClassLinear.class);
        ClassLinear result2 = testObject.resolve(ClassLinear.class);

        // then
        Assertions.assertThat(result1).isNotNull();
        Assertions.assertThat(result1.getFirst()).isNotNull();
        Assertions.assertThat(result1.getFirst().getSecond()).isNotNull();
        Assertions.assertThat(result1.getFirst().getSecond().getThird()).isNotNull();
        Assertions.assertThat(result2).isNotNull().isNotSameAs(result1);
        Assertions.assertThat(result2.getFirst()).isNotNull().isSameAs(result1.getFirst());
        Assertions.assertThat(result2.getFirst().getSecond())
                  .isNotNull()
                  .isSameAs(result1.getFirst().getSecond());
        Assertions.assertThat(result2.getFirst().getSecond().getThird())
                  .isNotNull()
                  .isSameAs(result1.getFirst().getSecond().getThird());
    }

    @Test
    public void registerType_WhenRegisteredWithInstance_ThenRegistrationException()
    {
        // given
        testObject.registerInstance(ClassConcrete.class, new ClassConcrete());

        // then
        Assertions.assertThatThrownBy(
                          () -> testObject.registerType(ClassConcrete.class, ConstructionPolicy.CONSTRUCTION))
                  .isInstanceOf(RegistrationException.class);
    }

    @ParameterizedTest
    @ValueSource(classes = { InterfaceInheritance.class, ClassAbstract.class })
    public void registerType_WhenAbstractType_ThenAbstractTypeException(Class<?> cls)
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.registerType(cls, ConstructionPolicy.CONSTRUCTION))
                  .isInstanceOf(AbstractTypeException.class);
    }

    // endregion
    // region resolve [class annotation]

    @ParameterizedTest
    @ValueSource(classes = {
            byte.class, short.class, int.class, long.class, float.class, double.class, char.class,
            boolean.class
    })
    public void registerType_WhenPrimitiveType_ThenRegistrationException(Class<?> cls)
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.registerType(cls, ConstructionPolicy.CONSTRUCTION))
                  .isInstanceOf(RegistrationException.class);
    }

    @Test
    public void resolve_WhenRegisterAnnotationWithConstructionPolicy_ThenDifferentInstances()
    {
        // when
        ClassRegisterConstruction result1 = testObject.resolve(ClassRegisterConstruction.class);
        ClassRegisterConstruction result2 = testObject.resolve(ClassRegisterConstruction.class);

        // then
        Assertions.assertThat(result1)
                  .isNotNull()
                  .isExactlyInstanceOf(ClassDerivedFromRegisterConstruction.class);
        Assertions.assertThat(result2)
                  .isNotNull()
                  .isExactlyInstanceOf(ClassDerivedFromRegisterConstruction.class)
                  .isNotSameAs(result1);
    }

    @Test
    public void resolve_WhenRegisterAnnotationWithSingletonPolicy_ThenSameInstance()
    {
        // when
        ClassRegisterSingleton result1 = testObject.resolve(ClassRegisterSingleton.class);
        ClassRegisterSingleton result2 = testObject.resolve(ClassRegisterSingleton.class);

        // then
        Assertions.assertThat(result1)
                  .isNotNull()
                  .isExactlyInstanceOf(ClassDerivedFromRegisterSingleton.class);
        Assertions.assertThat(result2)
                  .isNotNull()
                  .isExactlyInstanceOf(ClassDerivedFromRegisterSingleton.class)
                  .isSameAs(result1);
    }

    @Test
    public void resolve_WhenRegisterSelfAnnotationWithConstructionPolicy_ThenDifferentInstances()
    {
        // given
        Class<ClassRegisterSelfConstruction> type = ClassRegisterSelfConstruction.class;

        // when
        ClassRegisterSelfConstruction result1 = testObject.resolve(type);
        ClassRegisterSelfConstruction result2 = testObject.resolve(type);

        // then
        Assertions.assertThat(result1).isNotNull().isExactlyInstanceOf(type);
        Assertions.assertThat(result2).isNotNull().isExactlyInstanceOf(type).isNotSameAs(result1);
    }

    @Test
    public void resolve_WhenRegisterSelfAnnotationWithSingletonPolicy_ThenSameInstance()
    {
        // given
        Class<ClassRegisterSelfSingleton> type = ClassRegisterSelfSingleton.class;

        // when
        ClassRegisterSelfSingleton result1 = testObject.resolve(type);
        ClassRegisterSelfSingleton result2 = testObject.resolve(type);

        // then
        Assertions.assertThat(result1).isNotNull().isExactlyInstanceOf(type);
        Assertions.assertThat(result2).isNotNull().isExactlyInstanceOf(type).isSameAs(result1);
    }

    // endregion
    // region resolve [constructor annotation]

    @Test
    public void resolve_WhenDependencyAnnotationHasAddedTypes_ThenInstanceIsResolved()
    {

        // given
        testObject.registerType(ClassAnnotatedConstructor.class, ConstructionPolicy.CONSTRUCTION)
                  .registerType(ClassLinear.class, ConstructionPolicy.CONSTRUCTION)
                  .registerType(ClassLinearFirst.class, ConstructionPolicy.CONSTRUCTION)
                  .registerType(ClassLinearSecond.class, ConstructionPolicy.CONSTRUCTION)
                  .registerType(ClassLinearThird.class, ConstructionPolicy.CONSTRUCTION);

        // when
        ClassAnnotatedConstructor result = testObject.resolve(ClassAnnotatedConstructor.class);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getLinear()).isNotNull();
        Assertions.assertThat(result.getDiamond()).isNull();
    }

    @Test
    public void resolve_WhenDependencyAnnotationHasAddedInstance_ThenInstanceIsResolved()
    {

        // given
        testObject.registerType(ClassAnnotatedConstructor.class, ConstructionPolicy.CONSTRUCTION)
                  .registerInstance(ClassLinear.class, new ClassLinear(null));

        // when
        ClassAnnotatedConstructor result = testObject.resolve(ClassAnnotatedConstructor.class);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getLinear()).isNotNull();
        Assertions.assertThat(result.getLinear().getFirst()).isNull();
        Assertions.assertThat(result.getDiamond()).isNull();
    }

    @Test
    public void resolve_WhenAnnotationHasMissingDependency_ThenMissingDependenciesException()
    {

        // given
        testObject.registerType(ClassAnnotatedConstructor.class, ConstructionPolicy.CONSTRUCTION)
                  .registerType(ClassLinear.class, ConstructionPolicy.CONSTRUCTION)
                  .registerType(ClassLinearFirst.class, ConstructionPolicy.CONSTRUCTION);

        // then
        Assertions.assertThatThrownBy(() -> testObject.resolve(ClassAnnotatedConstructor.class))
                  .isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void resolve_WhenAnnotationOmitsCircularDependency_ThenInstanceIsResolved()
    {
        // given
        testObject.registerType(ClassAnnotatedConstructorOptionalCircular.class,
                          ConstructionPolicy.CONSTRUCTION)
                  .registerType(ClassLinear.class, ConstructionPolicy.CONSTRUCTION)
                  .registerType(ClassLinearFirst.class, ConstructionPolicy.CONSTRUCTION)
                  .registerType(ClassLinearSecond.class, ConstructionPolicy.CONSTRUCTION)
                  .registerType(ClassLinearThird.class, ConstructionPolicy.CONSTRUCTION)
                  .registerType(ClassCircular.class, ConstructionPolicy.CONSTRUCTION)
                  .registerType(ClassCircularLeft.class, ConstructionPolicy.CONSTRUCTION)
                  .registerType(ClassCircularRight.class, ConstructionPolicy.CONSTRUCTION);

        // when
        ClassAnnotatedConstructorOptionalCircular result =
                testObject.resolve(ClassAnnotatedConstructorOptionalCircular.class);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getLinear()).isNotNull();
        Assertions.assertThat(result.getCircular()).isNull();
    }

    @Test
    public void resolve_WhenAnnotatedConstructorPresent_ThenUseThisConstructor()
    {
        // given
        int number = 10;
        String string = "qwertyuiop";

        testObject.registerInstance(int.class, number)
                  .registerInstance(String.class, string)
                  .registerInstance(List.class, new ArrayList<Double>());

        // when
        ClassAnnotatedConstructorsSorting result =
                testObject.resolve(ClassAnnotatedConstructorsSorting.class);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getNumber()).isEqualTo(number);
        Assertions.assertThat(result.getString()).isEqualTo(string);
        Assertions.assertThat(result.getDoubles()).isEmpty();
        Assertions.assertThat(result.getInheritance())
                  .isNotNull()
                  .isExactlyInstanceOf(ClassConcrete.class);
        Assertions.assertThat(result.getLinear()).isNull();
    }

    @Test
    public void resolve_WhenNoAnnotatedConstructorAndAllDependenciesPresent_ThenUseProperConstructor()
    {
        // given
        int number = 10;
        String string = "qwertyuiop";
        ClassConcreteDerived concreteDerived = new ClassConcreteDerived();
        ClassLinear linear = new ClassLinear(null);

        testObject.registerInstance(int.class, number)
                  .registerInstance(String.class, string)
                  .registerInstance(List.class, new ArrayList<Double>())
                  .registerInstance(InterfaceInheritance.class, concreteDerived)
                  .registerInstance(ClassLinear.class, linear);

        // when
        ClassConstructorsSorting result = testObject.resolve(ClassConstructorsSorting.class);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getNumber()).isEqualTo(number);
        Assertions.assertThat(result.getString()).isEqualTo(string);
        Assertions.assertThat(result.getDoubles()).isEmpty();
        Assertions.assertThat(result.getInheritance()).isNotNull().isSameAs(concreteDerived);
        Assertions.assertThat(result.getLinear()).isNotNull().isSameAs(linear);
    }

    @Test
    public void resolve_WhenNoAnnotatedConstructorAndSomeDependenciesPresent_ThenUseProperConstructor()
    {
        // given
        int number = 10;
        String string = "qwertyuiop";

        testObject.registerInstance(int.class, number).registerInstance(String.class, string);

        // when
        ClassConstructorsSorting result = testObject.resolve(ClassConstructorsSorting.class);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getNumber()).isEqualTo(number);
        Assertions.assertThat(result.getString()).isEqualTo(string);
        Assertions.assertThat(result.getDoubles()).hasSize(2);
        Assertions.assertThat(result.getInheritance())
                  .isNotNull()
                  .isExactlyInstanceOf(ClassConcrete.class);
        Assertions.assertThat(result.getLinear()).isNull();
    }

    // endregion
    // region resolve [setter annotation]

    @Test
    public void resolve_WhenDependencySetterAndConstructor_ThenInstanceIsResolved()
    {
        // given
        testObject.registerType(ClassSetterWithConstructor.class, ConstructionPolicy.CONSTRUCTION)
                  .registerType(InterfaceInheritance.class, ClassConcrete.class,
                          ConstructionPolicy.CONSTRUCTION)
                  .registerType(ClassLinear.class, ConstructionPolicy.CONSTRUCTION)
                  .registerInstance(ClassLinearFirst.class, new ClassLinearFirst(null));

        // when
        ClassSetterWithConstructor result = testObject.resolve(ClassSetterWithConstructor.class);

        // then
        Assertions.assertThat(result)
                  .isNotNull()
                  .isExactlyInstanceOf(ClassSetterWithConstructor.class);
        Assertions.assertThat(result.getInheritance())
                  .isNotNull()
                  .isExactlyInstanceOf(ClassConcrete.class);
        Assertions.assertThat(result.getLinear())
                  .isNotNull()
                  .isExactlyInstanceOf(ClassLinear.class);
        Assertions.assertThat(result.getLinear().getFirst().getSecond()).isNull();
    }

    // endregion
    // region registerType/resolve [subtype]

    @Test
    public void registerType_resolve_WhenInterfaceWithConstructionPolicy_ThenDifferentInstances()
    {
        // given
        Class<InterfaceInheritance> type = InterfaceInheritance.class;
        Class<ClassConcrete> subtype = ClassConcrete.class;

        // when
        testObject.registerType(type, subtype, ConstructionPolicy.CONSTRUCTION);

        InterfaceInheritance result1 = testObject.resolve(type);
        InterfaceInheritance result2 = testObject.resolve(type);

        // then
        Assertions.assertThat(result1).isNotNull().isExactlyInstanceOf(subtype);
        Assertions.assertThat(result2)
                  .isNotNull()
                  .isExactlyInstanceOf(subtype)
                  .isNotSameAs(result1);
    }

    @Test
    public void registerType_resolve_WhenInterfaceWithSingletonPolicy_ThenSameInstance()
    {
        // given
        Class<InterfaceInheritance> type = InterfaceInheritance.class;
        Class<ClassConcrete> subtype = ClassConcrete.class;

        // when
        testObject.registerType(type, subtype, ConstructionPolicy.SINGLETON);

        InterfaceInheritance result1 = testObject.resolve(type);
        InterfaceInheritance result2 = testObject.resolve(type);

        // then
        Assertions.assertThat(result1).isNotNull().isExactlyInstanceOf(subtype);
        Assertions.assertThat(result2).isNotNull().isExactlyInstanceOf(subtype).isSameAs(result1);
    }

    @Test
    public void registerType_resolve_WhenInterfaceRegisteredWithDifferentPolicy_ThenChangesInstances()
    {
        // given
        Class<InterfaceInheritance> type = InterfaceInheritance.class;
        Class<ClassConcrete> subtype = ClassConcrete.class;

        // when 1
        testObject.registerType(type, subtype, ConstructionPolicy.SINGLETON);

        InterfaceInheritance result11 = testObject.resolve(type);
        InterfaceInheritance result12 = testObject.resolve(type);

        // then 1
        Assertions.assertThat(result11).isNotNull().isExactlyInstanceOf(subtype);
        Assertions.assertThat(result12).isNotNull().isExactlyInstanceOf(subtype).isSameAs(result11);

        // when 2
        testObject.registerType(type, subtype, ConstructionPolicy.CONSTRUCTION);

        InterfaceInheritance result21 = testObject.resolve(type);
        InterfaceInheritance result22 = testObject.resolve(type);

        // then 2
        Assertions.assertThat(result21).isNotNull().isExactlyInstanceOf(subtype);
        Assertions.assertThat(result22)
                  .isNotNull()
                  .isExactlyInstanceOf(subtype)
                  .isNotSameAs(result21);
    }

    @Test
    public void registerType_resolve_WhenInterfaceChangesClass_ThenChangesInstances()
    {
        // given
        Class<InterfaceInheritance> type = InterfaceInheritance.class;
        Class<ClassConcrete> subtype1 = ClassConcrete.class;
        Class<ClassConcreteDerived> subtype2 = ClassConcreteDerived.class;

        // when 1
        testObject.registerType(type, subtype1, ConstructionPolicy.CONSTRUCTION);

        InterfaceInheritance result1 = testObject.resolve(type);

        // then 1
        Assertions.assertThat(result1).isNotNull().isExactlyInstanceOf(subtype1);

        // when 2
        testObject.registerType(type, subtype2, ConstructionPolicy.CONSTRUCTION);

        InterfaceInheritance result3 = testObject.resolve(type);

        // then 2
        Assertions.assertThat(result3).isNotNull().isExactlyInstanceOf(subtype2);
    }

    @Test
    public void registerType_resolve_WhenAbstractClass_ThenOfSubtype()
    {
        // given
        Class<ClassAbstract> type = ClassAbstract.class;
        Class<ClassConcreteDerived> subtype = ClassConcreteDerived.class;

        // when
        testObject.registerType(type, subtype, ConstructionPolicy.CONSTRUCTION);

        ClassAbstract result = testObject.resolve(type);

        // then
        Assertions.assertThat(result).isNotNull().isExactlyInstanceOf(subtype);
    }

    @Test
    public void registerType_resolve_WhenConcreteClass_ThenInstanceOfSubtype()
    {
        // given
        Class<ClassConcrete> type = ClassConcrete.class;
        Class<ClassConcreteDerived> subtype = ClassConcreteDerived.class;

        // when
        testObject.registerType(type, subtype, ConstructionPolicy.CONSTRUCTION);

        ClassConcrete result = testObject.resolve(type);

        // then
        Assertions.assertThat(result).isNotNull().isExactlyInstanceOf(subtype);
    }

    @Test
    public void registerType_resolve_WhenTwoAbstractTypes_ThenInstanceOfConcreteClass()
    {
        // given
        Class<InterfaceInheritance> supertype = InterfaceInheritance.class;
        Class<ClassAbstract> type = ClassAbstract.class;
        Class<ClassConcrete> subtype = ClassConcrete.class;

        // when
        testObject.registerType(supertype, type, ConstructionPolicy.CONSTRUCTION)
                  .registerType(type, subtype, ConstructionPolicy.CONSTRUCTION);

        InterfaceInheritance result = testObject.resolve(type);

        // then
        Assertions.assertThat(result).isNotNull().isExactlyInstanceOf(subtype);
    }

    // endregion
    // region registerInstance & registerInstance/resolve

    @Test
    public void registerInstance_resolve_WhenInterface_ThenInstance()
    {
        // given
        Class<InterfaceInheritance> type = InterfaceInheritance.class;
        ClassConcrete instance = new ClassConcrete();

        // when
        testObject.registerInstance(type, instance);

        InterfaceInheritance result1 = testObject.resolve(type);
        InterfaceInheritance result2 = testObject.resolve(type);

        // then
        Assertions.assertThat(result1).isNotNull().isExactlyInstanceOf(ClassConcrete.class);
        Assertions.assertThat(result2)
                  .isNotNull()
                  .isExactlyInstanceOf(ClassConcrete.class)
                  .isSameAs(result1);
    }

    @Test
    public void registerInstance_resolve_WhenAbstractClass_ThenInstance()
    {
        // given
        Class<ClassAbstract> type = ClassAbstract.class;
        ClassConcreteDerived instance = new ClassConcreteDerived();

        // when
        testObject.registerInstance(type, instance);

        ClassAbstract result1 = testObject.resolve(type);
        ClassAbstract result2 = testObject.resolve(type);

        // then
        Assertions.assertThat(result1).isNotNull().isExactlyInstanceOf(ClassConcreteDerived.class);
        Assertions.assertThat(result2)
                  .isNotNull()
                  .isExactlyInstanceOf(ClassConcreteDerived.class)
                  .isSameAs(result1);
    }

    @Test
    public void registerInstance_resolve_WhenSameConcreteClass_ThenInstance()
    {
        // given
        Class<ClassConcrete> type = ClassConcrete.class;
        ClassConcrete instance = new ClassConcrete();

        // when
        testObject.registerInstance(type, instance);

        ClassAbstract result1 = testObject.resolve(type);
        ClassAbstract result2 = testObject.resolve(type);

        // then
        Assertions.assertThat(result1).isNotNull().isExactlyInstanceOf(ClassConcrete.class);
        Assertions.assertThat(result2)
                  .isNotNull()
                  .isExactlyInstanceOf(ClassConcrete.class)
                  .isSameAs(result1);
    }

    @Test
    public void registerInstance_resolve_WhenDerivedConcreteClass_ThenInstance()
    {
        // given
        Class<ClassConcrete> type = ClassConcrete.class;
        ClassConcreteDerived instance = new ClassConcreteDerived();

        // when
        testObject.registerInstance(type, instance);

        ClassAbstract result1 = testObject.resolve(type);
        ClassAbstract result2 = testObject.resolve(type);

        // then
        Assertions.assertThat(result1).isNotNull().isExactlyInstanceOf(ClassConcreteDerived.class);
        Assertions.assertThat(result2)
                  .isNotNull()
                  .isExactlyInstanceOf(ClassConcreteDerived.class)
                  .isSameAs(result1);
    }

    @Test
    public void registerInstance_resolve_WhenPrimitiveType_ThenBoxedInstance()
    {
        // when
        testObject.registerInstance(int.class, 10);

        int result1 = testObject.resolve(int.class);
        int result2 = testObject.resolve(int.class);

        // then
        Assertions.assertThat(result1).isExactlyInstanceOf(Integer.class);
        Assertions.assertThat(result2).isExactlyInstanceOf(Integer.class).isSameAs(result1);
    }

    @Test
    public void registerInstance_WhenAnnotatedType_ThenRegistrationException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.registerInstance(ClassRegisterSelf.class, new ClassRegisterSelf()))
                  .isInstanceOf(RegistrationException.class);
        Assertions.assertThatThrownBy(
                          () -> testObject.registerInstance(ClassRegister.class, new ClassRegister()))
                  .isInstanceOf(RegistrationException.class);
    }

    @Test
    public void registerInstance_WhenRegisteredWithType_ThenRegistrationException()
    {
        // given
        testObject.registerType(ClassConcrete.class, ConstructionPolicy.CONSTRUCTION);

        // then
        Assertions.assertThatThrownBy(
                          () -> testObject.registerInstance(ClassConcrete.class, new ClassConcrete()))
                  .isInstanceOf(RegistrationException.class);
    }

    @ParameterizedTest
    @ValueSource(classes = { InterfaceInheritance.class, ClassAbstract.class, ClassConcrete.class })
    public void registerInstance_WhenInstanceIsNull_ThenNullPointerException(Class<?> cls)
    {
        Assertions.assertThatThrownBy(() -> testObject.registerInstance(cls, null))
                  .isInstanceOf(NullPointerException.class);
    }

    // endregion
    // region resolveOrNull

    @Test
    public void resolveOrNull_WhenTypeRegistered_ThenTypeInstance()
    {
        // given
        Class<InterfaceInheritance> type = InterfaceInheritance.class;
        Class<ClassConcrete> subtype = ClassConcrete.class;

        testObject.registerType(type, subtype, ConstructionPolicy.CONSTRUCTION);

        // when
        InterfaceInheritance result = testObject.resolveOrNull(type);

        // then
        Assertions.assertThat(result).isNotNull().isExactlyInstanceOf(subtype);
    }

    @Test
    public void resolveOrNull_WhenTypeCanBeResolved_ThenTypeInstance()
    {
        // given
        Class<ClassDefaultConstructorOnly> type = ClassDefaultConstructorOnly.class;

        // when
        ClassDefaultConstructorOnly result = testObject.resolveOrNull(type);

        // then
        Assertions.assertThat(result).isNotNull().isExactlyInstanceOf(type);
    }

    @Test
    public void resolveOrNull_WhenInstanceRegistered_ThenThisInstance()
    {
        // given
        Class<ClassConcrete> type = ClassConcrete.class;
        ClassConcrete instance = new ClassConcrete();

        testObject.registerInstance(type, instance);

        // when
        ClassConcrete result = testObject.resolveOrNull(type);

        // then
        Assertions.assertThat(result).isNotNull().isExactlyInstanceOf(type).isSameAs(instance);
    }

    @ParameterizedTest
    @ValueSource(classes = {
            ClassParameterizedConstructorString.class, ClassParameterizedConstructorBoxed.class,
            ClassParameterizedConstructorPrimitive.class
    })
    public void resolveOrNull_WhenTypeHasMissingDependencies_ThenNull(Class<?> cls)
    {
        // when
        Object result = testObject.resolveOrNull(cls);

        // then
        Assertions.assertThat(result).isNull();
    }

    @ParameterizedTest
    @ValueSource(classes = { InterfaceInheritance.class, ClassAbstract.class, int.class })
    public void resolveOrNull_WhenTypeCannotBeResolved_ThenNull(Class<?> cls)
    {
        // when
        Object result = testObject.resolveOrNull(cls);

        // then
        Assertions.assertThat(result).isNull();
    }

    // endregion
}
