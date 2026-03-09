package com.github.refhumbold.yadic.newer.registry;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.github.refhumbold.yadic.ConstructionPolicy;
import com.github.refhumbold.yadic.newer.models.register.ClassAbstractRegister;
import com.github.refhumbold.yadic.newer.models.register.ClassRegister;
import com.github.refhumbold.yadic.newer.models.register.ClassRegisterDerived;
import com.github.refhumbold.yadic.newer.models.register.invalid.ClassRegisterAbstractSubclass;
import com.github.refhumbold.yadic.newer.models.register.invalid.ClassRegisterOutOfHierarchy;
import com.github.refhumbold.yadic.newer.models.register.self.ClassRegisterSelf;
import com.github.refhumbold.yadic.newer.models.register.self.invalid.ClassAbstractRegisterSelf;
import com.github.refhumbold.yadic.newer.models.register.self.invalid.ClassRegisterSelfDerived;
import com.github.refhumbold.yadic.newer.models.standard.ClassAbstractStandard;
import com.github.refhumbold.yadic.newer.models.standard.ClassStandard;
import com.github.refhumbold.yadic.newer.models.standard.ClassStandardDerived;
import com.github.refhumbold.yadic.newer.models.standard.InterfaceStandard;
import com.github.refhumbold.yadic.registry.DependencyRegistry;
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
        Assertions.assertThatThrownBy(
                          () -> testObject.addType(InterfaceStandard.class, ConstructionPolicy.CONSTRUCTION))
                  .isInstanceOf(AbstractTypeException.class);
    }

    @Test
    public void addType_WhenAbstractClass_ThenAbstractTypeException()
    {
        Assertions.assertThatThrownBy(() -> testObject.addType(ClassAbstractStandard.class,
                ConstructionPolicy.CONSTRUCTION)).isInstanceOf(AbstractTypeException.class);
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
        Assertions.assertThatThrownBy(() -> testObject.addType(ClassRegisterOutOfHierarchy.class,
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
                          () -> testObject.addType(ClassRegister.class, ClassRegisterDerived.class,
                                  ConstructionPolicy.CONSTRUCTION))
                  .isInstanceOf(AnnotatedTypeRegistrationException.class);
    }

    @Test
    public void addType_WhenSubtypeOfRegisterSelfAnnotation_ThenAnnotatedTypeRegistrationException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.addType(ClassRegisterSelf.class, ClassRegisterSelfDerived.class,
                                  ConstructionPolicy.CONSTRUCTION))
                  .isInstanceOf(AnnotatedTypeRegistrationException.class);
    }

    @Test
    public void addType_WhenAlreadyRegisteredInstance_ThenRegistrationException()
    {
        // given
        Class<ClassStandard> type = ClassStandard.class;
        ClassStandard instance = new ClassStandard();

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
        Class<ClassAbstractStandard> type = ClassAbstractStandard.class;
        Class<ClassStandard> subtype = ClassStandard.class;

        // when
        testObject.addType(type, subtype, ConstructionPolicy.SINGLETON);

        TypeConstruction<? extends InterfaceStandard> result = testObject.findType(type);

        // then
        Assertions.assertThat(result.type()).isEqualTo(subtype);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.SINGLETON);
    }

    @Test
    public void addType_findType_WhenPolicy_ThenThisTypeInserted()
    {
        // given
        Class<ClassStandard> type = ClassStandard.class;

        // when
        testObject.addType(type, ConstructionPolicy.SINGLETON);

        TypeConstruction<? extends ClassStandard> result = testObject.findType(type);

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
        Assertions.assertThat(result.type()).isEqualTo(ClassRegisterDerived.class);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.CONSTRUCTION);
    }

    // endregion
    // region addInstance & addInstance/findInstance

    @Test
    public void addInstance_WhenAlreadyRegisteredType_ThenRegistrationException()
    {
        // given
        Class<ClassStandard> type = ClassStandard.class;
        ClassStandard instance = new ClassStandard();

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
        Assertions.assertThatThrownBy(() -> testObject.addInstance(ClassStandard.class, null))
                  .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void addInstance_findInstance_WhenInstanceOfSameType_ThenInstance()
    {
        // given
        Class<ClassStandard> type = ClassStandard.class;
        ClassStandard instance = new ClassStandard();

        // when
        testObject.addInstance(type, instance);

        Instance<ClassStandard> result = testObject.findInstance(type);

        // then
        Assertions.assertThat(result.exists()).isTrue();
        Assertions.assertThat(result.extract()).isSameAs(instance);
        Assertions.assertThat(result.extract()).isExactlyInstanceOf(type);
    }

    @Test
    public void addInstance_findInstance_WhenInstanceOfDerivedType_ThenInstance()
    {
        // given
        Class<ClassStandard> type = ClassStandard.class;
        ClassStandard instance = new ClassStandardDerived();

        // when
        testObject.addInstance(type, instance);

        Instance<ClassStandard> result = testObject.findInstance(type);

        // then
        Assertions.assertThat(result.exists()).isTrue();
        Assertions.assertThat(result.extract()).isSameAs(instance);
        Assertions.assertThat(result.extract()).isInstanceOf(type);
        Assertions.assertThat(result.extract()).isExactlyInstanceOf(ClassStandardDerived.class);
    }

    @Test
    public void addInstance_findInstance_WhenAddInterfaceWithConcreteInstance_ThenInstance()
    {
        // given
        Class<InterfaceStandard> type = InterfaceStandard.class;
        ClassStandard instance = new ClassStandard();

        // when
        testObject.addInstance(type, instance);

        Instance<InterfaceStandard> result = testObject.findInstance(type);

        // then
        Assertions.assertThat(result.exists()).isTrue();
        Assertions.assertThat(result.extract()).isSameAs(instance);
        Assertions.assertThat(result.extract()).isInstanceOf(type);
        Assertions.assertThat(result.extract()).isExactlyInstanceOf(ClassStandard.class);
    }

    @Test
    public void addInstance_findInstance_WhenAddAbstractClassWithConcreteInstance_ThenInstance()
    {
        // given
        Class<ClassAbstractStandard> type = ClassAbstractStandard.class;
        ClassStandard instance = new ClassStandardDerived();

        // when
        testObject.addInstance(type, instance);

        Instance<ClassAbstractStandard> result = testObject.findInstance(type);

        // then
        Assertions.assertThat(result.exists()).isTrue();
        Assertions.assertThat(result.extract()).isSameAs(instance);
        Assertions.assertThat(result.extract()).isInstanceOf(type);
        Assertions.assertThat(result.extract()).isExactlyInstanceOf(ClassStandardDerived.class);
    }

    // endregion
    // region addSingleton/findInstance

    @Test
    public void addSingleton_findInstance_WhenAddedTypeWithSingletonPolicy_ThenSingleton()
    {
        // given
        ClassStandard singleton = new ClassStandard();

        testObject.addType(ClassAbstractStandard.class, ClassStandard.class,
                ConstructionPolicy.SINGLETON);

        // when
        testObject.addSingleton(ClassAbstractStandard.class, singleton);

        Instance<ClassAbstractStandard> result =
                testObject.findInstance(ClassAbstractStandard.class);

        // then
        Assertions.assertThat(result.exists()).isTrue();
        Assertions.assertThat(result.extract()).isSameAs(singleton);
    }

    @Test
    public void addSingleton_findInstance_WhenAddedTypeWithConstructionPolicy_ThenNoInstance()
    {
        // given
        ClassStandard singleton = new ClassStandard();

        testObject.addType(ClassAbstractStandard.class, ClassStandard.class,
                ConstructionPolicy.CONSTRUCTION);

        // when
        testObject.addSingleton(ClassAbstractStandard.class, singleton);

        Instance<ClassAbstractStandard> result =
                testObject.findInstance(ClassAbstractStandard.class);

        // then
        Assertions.assertThat(result.exists()).isFalse();
    }

    @Test
    public void addSingleton_findInstance_WhenNotAddedType_ThenNoInstance()
    {
        // given
        ClassStandard singleton = new ClassStandard();

        // when
        testObject.addSingleton(ClassAbstractStandard.class, singleton);

        Instance<ClassAbstractStandard> result =
                testObject.findInstance(ClassAbstractStandard.class);

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
        testObject.addType(InterfaceStandard.class, ClassStandard.class,
                ConstructionPolicy.CONSTRUCTION);

        // when
        TypeConstruction<? extends InterfaceStandard> result =
                testObject.findType(InterfaceStandard.class);

        // then
        Assertions.assertThat(result.type()).isEqualTo(ClassStandard.class);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.CONSTRUCTION);
    }

    @Test
    public void findType_WhenAddedFromAbstractClass_ThenMapping()
    {
        // given
        testObject.addType(ClassAbstractStandard.class, ClassStandard.class,
                ConstructionPolicy.CONSTRUCTION);

        // when
        TypeConstruction<? extends ClassAbstractStandard> result =
                testObject.findType(ClassAbstractStandard.class);

        // then
        Assertions.assertThat(result.type()).isEqualTo(ClassStandard.class);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.CONSTRUCTION);
    }

    @Test
    public void findType_WhenAddedFromConcreteClass_ThenMapping()
    {
        // given
        testObject.addType(ClassStandard.class, ClassStandardDerived.class,
                ConstructionPolicy.CONSTRUCTION);

        // when
        TypeConstruction<? extends ClassStandard> result = testObject.findType(ClassStandard.class);

        // then
        Assertions.assertThat(result.type()).isEqualTo(ClassStandardDerived.class);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.CONSTRUCTION);
    }

    @Test
    public void findType_WhenAddedMultipleAbstractionLevels_ThenMapping()
    {
        // given
        testObject.addType(InterfaceStandard.class, ClassAbstractStandard.class,
                ConstructionPolicy.CONSTRUCTION);
        testObject.addType(ClassAbstractStandard.class, ClassStandard.class,
                ConstructionPolicy.CONSTRUCTION);

        // when
        TypeConstruction<? extends InterfaceStandard> result =
                testObject.findType(InterfaceStandard.class);

        // then
        Assertions.assertThat(result.type()).isEqualTo(ClassStandard.class);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.CONSTRUCTION);
    }

    @Test
    public void findType_WhenInterfaceNotAdded_ThenMissingDependenciesException()
    {
        Assertions.assertThatThrownBy(() -> testObject.findType(InterfaceStandard.class))
                  .isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void findType_WhenAbstractClassNotAdded_ThenMissingDependenciesException()
    {
        Assertions.assertThatThrownBy(() -> testObject.findType(ClassAbstractStandard.class))
                  .isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void findType_WhenConcreteTypeNotAdded_ThenMappingToSelf()
    {
        // given
        Class<ClassStandard> type = ClassStandard.class;

        // when
        TypeConstruction<? extends ClassStandard> result = testObject.findType(type);

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
        Assertions.assertThat(result.type()).isEqualTo(ClassRegisterDerived.class);
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
        TypeConstruction<? extends ClassAbstractRegister> result =
                testObject.findType(ClassAbstractRegister.class);

        // then
        Assertions.assertThat(result.type()).isEqualTo(ClassRegisterDerived.class);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.CONSTRUCTION);
    }

    @Test
    public void findType_WhenDifferentPolicy_ThenMixingPoliciesException()
    {
        // given
        testObject.addType(InterfaceStandard.class, ClassAbstractStandard.class,
                ConstructionPolicy.CONSTRUCTION);
        testObject.addType(ClassAbstractStandard.class, ClassStandard.class,
                ConstructionPolicy.SINGLETON);

        // then
        Assertions.assertThatThrownBy(() -> testObject.findType(InterfaceStandard.class))
                  .isInstanceOf(MixingPoliciesException.class);
    }

    @Test
    public void findType_whenAddedInstance_ThenMappingToSelf()
    {
        // given
        Class<ClassStandard> type = ClassStandard.class;

        testObject.addInstance(type, new ClassStandard());

        // when
        TypeConstruction<? extends ClassStandard> result = testObject.findType(type);

        // then
        Assertions.assertThat(result.type()).isEqualTo(type);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.SINGLETON);
    }

    @Test
    public void findInstance_WhenInstanceAbsent_ThenNoMapping()
    {
        // when
        Instance<ClassStandard> result = testObject.findInstance(ClassStandard.class);

        // then
        Assertions.assertThat(result.exists()).isFalse();
    }

    // endregion
    // region contains

    @Test
    public void contains_WhenAddedInstance_ThenTrue()
    {
        // given
        testObject.addInstance(ClassStandard.class, new ClassStandard());

        // when
        boolean result = testObject.contains(ClassStandard.class);

        // then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void contains_WhenInterfaceAdded_ThenTrue()
    {
        // given
        Class<InterfaceStandard> type = InterfaceStandard.class;

        testObject.addType(type, ClassAbstractStandard.class, ConstructionPolicy.CONSTRUCTION);

        // when
        boolean result = testObject.contains(type);

        // then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void contains_WhenInterfaceNotAdded_ThenFalse()
    {
        // when
        boolean result = testObject.contains(InterfaceStandard.class);

        // then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void contains_WhenAbstractClassAdded_ThenTrue()
    {
        // given
        Class<ClassAbstractStandard> type = ClassAbstractStandard.class;

        testObject.addType(type, ClassStandard.class, ConstructionPolicy.CONSTRUCTION);

        // when
        boolean result = testObject.contains(type);

        // then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void contains_WhenAbstractClassNotAdded_ThenFalse()
    {
        // when
        boolean result = testObject.contains(ClassAbstractStandard.class);

        // then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void contains_WhenConcreteClassAdded_ThenFalse()
    {
        // given
        Class<ClassStandard> type = ClassStandard.class;

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
        boolean result = testObject.contains(ClassStandard.class);

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
