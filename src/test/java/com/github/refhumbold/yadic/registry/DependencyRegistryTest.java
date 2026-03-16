package com.github.refhumbold.yadic.registry;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.github.refhumbold.yadic.ConstructionPolicy;
import com.github.refhumbold.yadic.newer.models.annotations.register.*;
import com.github.refhumbold.yadic.newer.models.annotations.registerself.ClassAbstractRegisterSelf;
import com.github.refhumbold.yadic.newer.models.annotations.registerself.ClassDerivedFromRegisterSelf;
import com.github.refhumbold.yadic.newer.models.annotations.registerself.ClassRegisterSelf;
import com.github.refhumbold.yadic.newer.models.inheritance.ClassAbstract;
import com.github.refhumbold.yadic.newer.models.inheritance.ClassConcrete;
import com.github.refhumbold.yadic.newer.models.inheritance.ClassConcreteDerived;
import com.github.refhumbold.yadic.newer.models.inheritance.InterfaceInheritance;
import com.github.refhumbold.yadic.registry.exception.AbstractTypeException;
import com.github.refhumbold.yadic.registry.exception.AnnotatedTypeRegistrationException;
import com.github.refhumbold.yadic.registry.exception.MixingPoliciesException;
import com.github.refhumbold.yadic.registry.exception.NotDerivedTypeException;
import com.github.refhumbold.yadic.registry.exception.RegistrationException;
import com.github.refhumbold.yadic.registry.valuetypes.Instance;
import com.github.refhumbold.yadic.registry.valuetypes.TypeConstruction;
import com.github.refhumbold.yadic.resolver.exception.MissingDependenciesException;

public class DependencyRegistryTest
{
    private DependencyRegistry testObject;

    @BeforeEach
    public void setUp()
    {
        testObject = new DependencyRegistry();
    }

    @AfterEach
    public void tearDown()
    {
        testObject = null;
    }

    // region addType & addType/findType

    @Test
    public void addType_WhenInterface_ThenAbstractTypeException()
    {
        Assertions.assertThatThrownBy(() -> testObject.addType(InterfaceInheritance.class,
                ConstructionPolicy.CONSTRUCTION)).isInstanceOf(AbstractTypeException.class);
    }

    @Test
    public void addType_WhenAbstractClass_ThenAbstractTypeException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.addType(ClassAbstract.class, ConstructionPolicy.CONSTRUCTION))
                  .isInstanceOf(AbstractTypeException.class);
    }

    @Test
    public void addType_WhenPrimitiveType_ThenRegistrationException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.addType(boolean.class, ConstructionPolicy.CONSTRUCTION))
                  .isInstanceOf(RegistrationException.class);
    }

    @Test
    public void addType_WhenRegisterToNotSubtype_ThenNotDerivedTypeException()
    {
        Assertions.assertThatThrownBy(() -> testObject.addType(ClassRegisterNotOwnSubclass.class,
                ConstructionPolicy.CONSTRUCTION)).isInstanceOf(NotDerivedTypeException.class);
    }

    @Test
    public void addType_WhenRegisterToAbstractClass_ThenAbstractTypeException()
    {
        Assertions.assertThatThrownBy(() -> testObject.addType(ClassRegisterAbstractSubclass.class,
                ConstructionPolicy.CONSTRUCTION)).isInstanceOf(AbstractTypeException.class);
    }

    @Test
    public void addType_WhenRegisterSelfOnAbstractClass_ThenAbstractTypeException()
    {
        Assertions.assertThatThrownBy(() -> testObject.addType(ClassAbstractRegisterSelf.class,
                ConstructionPolicy.CONSTRUCTION)).isInstanceOf(AbstractTypeException.class);
    }

    @Test
    public void addType_WhenSubtypeOfRegisterAnnotation_ThenAnnotatedTypeRegistrationException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.addType(ClassRegister.class, ClassDerivedFromRegister.class,
                                  ConstructionPolicy.CONSTRUCTION))
                  .isInstanceOf(AnnotatedTypeRegistrationException.class);
    }

    @Test
    public void addType_WhenSubtypeOfRegisterSelfAnnotation_ThenAnnotatedTypeRegistrationException()
    {
        Assertions.assertThatThrownBy(() -> testObject.addType(ClassRegisterSelf.class,
                          ClassDerivedFromRegisterSelf.class, ConstructionPolicy.CONSTRUCTION))
                  .isInstanceOf(AnnotatedTypeRegistrationException.class);
    }

    @Test
    public void addType_WhenAlreadyRegisteredInstance_ThenRegistrationException()
    {
        // given
        Class<ClassConcrete> type = ClassConcrete.class;
        ClassConcrete instance = new ClassConcrete();

        testObject.addInstance(type, instance);

        // then
        Assertions.assertThatThrownBy(
                          () -> testObject.addType(type, ConstructionPolicy.CONSTRUCTION))
                  .isInstanceOf(RegistrationException.class);
    }

    @Test
    public void addType_findType_WhenSubtypeAndPolicy_ThenSubtypeInserted()
    {
        // given
        Class<ClassAbstract> type = ClassAbstract.class;
        Class<ClassConcrete> subtype = ClassConcrete.class;

        // when
        testObject.addType(type, subtype, ConstructionPolicy.SINGLETON);

        TypeConstruction<? extends InterfaceInheritance> result = testObject.findType(type);

        // then
        Assertions.assertThat(result.type()).isEqualTo(subtype);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.SINGLETON);
    }

    @Test
    public void addType_findType_WhenPolicy_ThenThisTypeInserted()
    {
        // given
        Class<ClassConcrete> type = ClassConcrete.class;

        // when
        testObject.addType(type, ConstructionPolicy.SINGLETON);

        TypeConstruction<? extends ClassConcrete> result = testObject.findType(type);

        // then
        Assertions.assertThat(result.type()).isEqualTo(type);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.SINGLETON);
    }

    @Test
    public void addType_findType_WhenRegisterSelfAnnotation_ThenThisTypeInserted()
    {
        // given
        Class<ClassRegisterSelf> type = ClassRegisterSelf.class;

        // when
        testObject.addType(type, ConstructionPolicy.CONSTRUCTION);

        TypeConstruction<? extends ClassRegisterSelf> result = testObject.findType(type);

        // then
        Assertions.assertThat(result.type()).isEqualTo(type);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.CONSTRUCTION);
    }

    @Test
    public void addType_findType_WhenRegisterAnnotation_ThenRegisteredSubtypeInserted()
    {
        // given
        Class<ClassRegister> type = ClassRegister.class;

        // when
        testObject.addType(type, ConstructionPolicy.CONSTRUCTION);

        TypeConstruction<? extends ClassRegister> result = testObject.findType(type);

        // then
        Assertions.assertThat(result.type()).isEqualTo(ClassDerivedFromRegister.class);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.CONSTRUCTION);
    }

    // endregion
    // region addInstance & addInstance/findInstance

    @Test
    public void addInstance_WhenAlreadyRegisteredType_ThenRegistrationException()
    {
        // given
        Class<ClassConcrete> type = ClassConcrete.class;
        ClassConcrete instance = new ClassConcrete();

        testObject.addType(type, ConstructionPolicy.CONSTRUCTION);

        // then
        Assertions.assertThatThrownBy(() -> testObject.addInstance(type, instance))
                  .isInstanceOf(RegistrationException.class);
    }

    @Test
    public void addInstance_WhenRegisterAnnotation_ThenRegistrationException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.addInstance(ClassRegister.class, new ClassRegister()))
                  .isInstanceOf(RegistrationException.class);
    }

    @Test
    public void addInstance_WhenNullInstance_ThenNullPointerException()
    {
        Assertions.assertThatThrownBy(() -> testObject.addInstance(ClassConcrete.class, null))
                  .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void addInstance_findInstance_WhenInstanceOfSameType_ThenInstance()
    {
        // given
        Class<ClassConcrete> type = ClassConcrete.class;
        ClassConcrete instance = new ClassConcrete();

        // when
        testObject.addInstance(type, instance);

        Instance<ClassConcrete> result = testObject.findInstance(type);

        // then
        Assertions.assertThat(result.exists()).isTrue();
        Assertions.assertThat(result.extract()).isSameAs(instance);
        Assertions.assertThat(result.extract()).isExactlyInstanceOf(type);
    }

    @Test
    public void addInstance_findInstance_WhenInstanceOfDerivedType_ThenInstance()
    {
        // given
        Class<ClassConcrete> type = ClassConcrete.class;
        ClassConcreteDerived instance = new ClassConcreteDerived();

        // when
        testObject.addInstance(type, instance);

        Instance<ClassConcrete> result = testObject.findInstance(type);

        // then
        Assertions.assertThat(result.exists()).isTrue();
        Assertions.assertThat(result.extract()).isSameAs(instance);
        Assertions.assertThat(result.extract()).isInstanceOf(type);
        Assertions.assertThat(result.extract()).isExactlyInstanceOf(ClassConcreteDerived.class);
    }

    @Test
    public void addInstance_findInstance_WhenAddInterfaceWithConcreteInstance_ThenInstance()
    {
        // given
        Class<InterfaceInheritance> type = InterfaceInheritance.class;
        ClassConcrete instance = new ClassConcrete();

        // when
        testObject.addInstance(type, instance);

        Instance<InterfaceInheritance> result = testObject.findInstance(type);

        // then
        Assertions.assertThat(result.exists()).isTrue();
        Assertions.assertThat(result.extract()).isSameAs(instance);
        Assertions.assertThat(result.extract()).isInstanceOf(type);
        Assertions.assertThat(result.extract()).isExactlyInstanceOf(ClassConcrete.class);
    }

    @Test
    public void addInstance_findInstance_WhenAddAbstractClassWithConcreteInstance_ThenInstance()
    {
        // given
        Class<ClassAbstract> type = ClassAbstract.class;
        ClassConcreteDerived instance = new ClassConcreteDerived();

        // when
        testObject.addInstance(type, instance);

        Instance<ClassAbstract> result = testObject.findInstance(type);

        // then
        Assertions.assertThat(result.exists()).isTrue();
        Assertions.assertThat(result.extract()).isSameAs(instance);
        Assertions.assertThat(result.extract()).isInstanceOf(type);
        Assertions.assertThat(result.extract()).isExactlyInstanceOf(ClassConcreteDerived.class);
    }

    // endregion
    // region addSingleton/findInstance

    @Test
    public void addSingleton_findInstance_WhenAddedTypeWithSingletonPolicy_ThenSingleton()
    {
        // given
        ClassConcrete singleton = new ClassConcrete();

        testObject.addType(ClassAbstract.class, ClassConcrete.class, ConstructionPolicy.SINGLETON);

        // when
        testObject.addSingleton(ClassAbstract.class, singleton);

        Instance<ClassAbstract> result = testObject.findInstance(ClassAbstract.class);

        // then
        Assertions.assertThat(result.exists()).isTrue();
        Assertions.assertThat(result.extract()).isSameAs(singleton);
    }

    @Test
    public void addSingleton_findInstance_WhenAddedTypeWithConstructionPolicy_ThenNoInstance()
    {
        // given
        ClassConcrete singleton = new ClassConcrete();

        testObject.addType(ClassAbstract.class, ClassConcrete.class,
                ConstructionPolicy.CONSTRUCTION);

        // when
        testObject.addSingleton(ClassAbstract.class, singleton);

        Instance<ClassAbstract> result = testObject.findInstance(ClassAbstract.class);

        // then
        Assertions.assertThat(result.exists()).isFalse();
    }

    @Test
    public void addSingleton_findInstance_WhenNotAddedType_ThenNoInstance()
    {
        // given
        ClassConcrete singleton = new ClassConcrete();

        // when
        testObject.addSingleton(ClassAbstract.class, singleton);

        Instance<ClassAbstract> result = testObject.findInstance(ClassAbstract.class);

        // then
        Assertions.assertThat(result.exists()).isFalse();
    }

    // endregion
    // region findType & findInstance

    @Test
    public void findType_WhenPrimitive_ThenMappingToSelf()
    {
        // when
        TypeConstruction<?> result = testObject.findType(double.class);

        // then
        Assertions.assertThat(result.type()).isEqualTo(double.class);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.CONSTRUCTION);
    }

    @Test
    public void findType_WhenAddedFromInterface_ThenMapping()
    {
        // given
        testObject.addType(InterfaceInheritance.class, ClassConcrete.class,
                ConstructionPolicy.CONSTRUCTION);

        // when
        TypeConstruction<? extends InterfaceInheritance> result =
                testObject.findType(InterfaceInheritance.class);

        // then
        Assertions.assertThat(result.type()).isEqualTo(ClassConcrete.class);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.CONSTRUCTION);
    }

    @Test
    public void findType_WhenAddedFromAbstractClass_ThenMapping()
    {
        // given
        testObject.addType(ClassAbstract.class, ClassConcrete.class,
                ConstructionPolicy.CONSTRUCTION);

        // when
        TypeConstruction<? extends ClassAbstract> result = testObject.findType(ClassAbstract.class);

        // then
        Assertions.assertThat(result.type()).isEqualTo(ClassConcrete.class);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.CONSTRUCTION);
    }

    @Test
    public void findType_WhenAddedFromConcreteClass_ThenMapping()
    {
        // given
        testObject.addType(ClassConcrete.class, ClassConcreteDerived.class,
                ConstructionPolicy.CONSTRUCTION);

        // when
        TypeConstruction<? extends ClassConcrete> result = testObject.findType(ClassConcrete.class);

        // then
        Assertions.assertThat(result.type()).isEqualTo(ClassConcreteDerived.class);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.CONSTRUCTION);
    }

    @Test
    public void findType_WhenAddedMultipleAbstractionLevels_ThenMapping()
    {
        // given
        testObject.addType(InterfaceInheritance.class, ClassAbstract.class,
                ConstructionPolicy.CONSTRUCTION);
        testObject.addType(ClassAbstract.class, ClassConcrete.class,
                ConstructionPolicy.CONSTRUCTION);

        // when
        TypeConstruction<? extends InterfaceInheritance> result =
                testObject.findType(InterfaceInheritance.class);

        // then
        Assertions.assertThat(result.type()).isEqualTo(ClassConcrete.class);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.CONSTRUCTION);
    }

    @Test
    public void findType_WhenInterfaceNotAdded_ThenMissingDependenciesException()
    {
        Assertions.assertThatThrownBy(() -> testObject.findType(InterfaceInheritance.class))
                  .isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void findType_WhenAbstractClassNotAdded_ThenMissingDependenciesException()
    {
        Assertions.assertThatThrownBy(() -> testObject.findType(ClassAbstract.class))
                  .isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void findType_WhenConcreteTypeNotAdded_ThenMappingToSelf()
    {
        // given
        Class<ClassConcrete> type = ClassConcrete.class;

        // when
        TypeConstruction<? extends ClassConcrete> result = testObject.findType(type);

        // then
        Assertions.assertThat(result.type()).isEqualTo(type);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.CONSTRUCTION);
    }

    @Test
    public void findType_WhenRegisterAnnotation_ThenMappingFromAnnotation()
    {
        // when
        TypeConstruction<? extends ClassRegister> result = testObject.findType(ClassRegister.class);

        // then
        Assertions.assertThat(result.type()).isEqualTo(ClassDerivedFromRegister.class);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.CONSTRUCTION);
    }

    @Test
    public void findType_WhenRegisterSelfAnnotation_ThenMappingToSelf()
    {
        // when
        TypeConstruction<? extends ClassRegisterSelf> result =
                testObject.findType(ClassRegisterSelf.class);

        // then
        Assertions.assertThat(result.type()).isEqualTo(ClassRegisterSelf.class);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.CONSTRUCTION);
    }

    @Test
    public void findType_WhenMultipleAnnotationLayers_ThenMappingFromAnnotationsChain()
    {
        // when
        TypeConstruction<? extends ClassAbstractRegisterChain> result =
                testObject.findType(ClassAbstractRegisterChain.class);

        // then
        Assertions.assertThat(result.type()).isEqualTo(ClassRegisterChainDerived.class);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.CONSTRUCTION);
    }

    @Test
    public void findType_WhenDifferentPolicy_ThenMixingPoliciesException()
    {
        // given
        testObject.addType(InterfaceInheritance.class, ClassAbstract.class,
                ConstructionPolicy.CONSTRUCTION);
        testObject.addType(ClassAbstract.class, ClassConcrete.class, ConstructionPolicy.SINGLETON);

        // then
        Assertions.assertThatThrownBy(() -> testObject.findType(InterfaceInheritance.class))
                  .isInstanceOf(MixingPoliciesException.class);
    }

    @Test
    public void findType_whenAddedInstance_ThenMappingToSelf()
    {
        // given
        Class<ClassConcrete> type = ClassConcrete.class;

        testObject.addInstance(type, new ClassConcrete());

        // when
        TypeConstruction<? extends ClassConcrete> result = testObject.findType(type);

        // then
        Assertions.assertThat(result.type()).isEqualTo(type);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.SINGLETON);
    }

    @Test
    public void findInstance_WhenInstanceAbsent_ThenNoMapping()
    {
        // when
        Instance<ClassConcrete> result = testObject.findInstance(ClassConcrete.class);

        // then
        Assertions.assertThat(result.exists()).isFalse();
    }

    // endregion
    // region contains

    @Test
    public void contains_WhenAddedInstance_ThenTrue()
    {
        // given
        testObject.addInstance(ClassConcrete.class, new ClassConcrete());

        // when
        boolean result = testObject.contains(ClassConcrete.class);

        // then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void contains_WhenInterfaceAdded_ThenTrue()
    {
        // given
        Class<InterfaceInheritance> type = InterfaceInheritance.class;

        testObject.addType(type, ClassAbstract.class, ConstructionPolicy.CONSTRUCTION);

        // when
        boolean result = testObject.contains(type);

        // then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void contains_WhenInterfaceNotAdded_ThenFalse()
    {
        // when
        boolean result = testObject.contains(InterfaceInheritance.class);

        // then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void contains_WhenAbstractClassAdded_ThenTrue()
    {
        // given
        Class<ClassAbstract> type = ClassAbstract.class;

        testObject.addType(type, ClassConcrete.class, ConstructionPolicy.CONSTRUCTION);

        // when
        boolean result = testObject.contains(type);

        // then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void contains_WhenAbstractClassNotAdded_ThenFalse()
    {
        // when
        boolean result = testObject.contains(ClassAbstract.class);

        // then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void contains_WhenConcreteClassAdded_ThenFalse()
    {
        // given
        Class<ClassConcrete> type = ClassConcrete.class;

        testObject.addType(type, ConstructionPolicy.CONSTRUCTION);

        // when
        boolean result = testObject.contains(type);

        // then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void contains_WhenConcreteClassNotAdded_ThenFalse()
    {
        // when
        boolean result = testObject.contains(ClassConcrete.class);

        // then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void contains_WhenAnnotatedClassAdded_ThenTrue()
    {
        // given
        Class<ClassRegister> type = ClassRegister.class;

        testObject.addType(type, ConstructionPolicy.CONSTRUCTION);

        // when
        boolean result = testObject.contains(type);

        // then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void contains_WhenAnnotatedClassNotAdded_ThenTrue()
    {
        // when
        boolean result = testObject.contains(ClassRegister.class);

        // then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void contains_WhenSelfAnnotatedClassNotAdded_ThenTrue()
    {
        // when
        boolean result = testObject.contains(ClassRegisterSelf.class);

        // then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void contains_WhenIncorrectlyAnnotatedClass_ThenFalse()
    {
        // when
        boolean result = testObject.contains(ClassAbstractRegisterSelf.class);

        // then
        Assertions.assertThat(result).isFalse();
    }

    // endregion
}
