package dicontainer.registry;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dicontainer.ConstructionPolicy;
import dicontainer.models.basic.ClassBasicAbstract;
import dicontainer.models.basic.ClassBasicInheritsFromAbstract;
import dicontainer.models.basic.ClassBasicStringGetter;
import dicontainer.models.basic.InterfaceBasic;
import dicontainer.models.register.*;
import dicontainer.registry.exception.AbstractTypeException;
import dicontainer.registry.exception.AnnotatedTypeRegistrationException;
import dicontainer.registry.exception.MixingPoliciesException;
import dicontainer.registry.exception.NotDerivedTypeException;
import dicontainer.registry.exception.RegistrationException;
import dicontainer.registry.valuetypes.Instance;
import dicontainer.registry.valuetypes.TypeConstruction;

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

    // region addType/findType

    @Test
    public void addType_findType_WhenSpecifyTypeWithSubtypeAndPolicy_ThenSubtypeInserted()
    {
        // given
        Class<ClassBasicAbstract> type = ClassBasicAbstract.class;
        Class<ClassBasicInheritsFromAbstract> subtype = ClassBasicInheritsFromAbstract.class;

        // when
        testObject.addType(type, subtype, ConstructionPolicy.SINGLETON);

        TypeConstruction<? extends InterfaceBasic> result = testObject.findType(type);

        // then
        Assertions.assertThat(result.type()).isEqualTo(subtype);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.SINGLETON);
    }

    @Test
    public void addType_findType_WhenSpecifyTypeAndPolicy_ThenThisTypeInserted()
    {
        // given
        Class<ClassBasicInheritsFromAbstract> type = ClassBasicInheritsFromAbstract.class;

        // when
        testObject.addType(type, ConstructionPolicy.SINGLETON);

        TypeConstruction<? extends ClassBasicInheritsFromAbstract> result =
                testObject.findType(type);

        // then
        Assertions.assertThat(result.type()).isEqualTo(type);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.SINGLETON);
    }

    @Test
    public void addType_findType_WhenTypeHasSelfRegisterAnnotation_ThenThisTypeInserted()
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
    public void addType_findType_WhenTypeHasRegisterAnnotation_ThenSubtypeInserted()
    {
        // given
        Class<ClassRegisterConcrete> type = ClassRegisterConcrete.class;

        // when
        testObject.addType(type, ConstructionPolicy.CONSTRUCTION);

        TypeConstruction<? extends ClassRegisterConcrete> result = testObject.findType(type);

        // then
        Assertions.assertThat(result.type()).isEqualTo(ClassRegisterDerivedFromRegister.class);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.CONSTRUCTION);
    }

    // endregion
    // region addType

    @Test
    public void addType_WhenSingleInterface_ThenAbstractTypeException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.addType(InterfaceBasic.class, ConstructionPolicy.CONSTRUCTION))
                  .isInstanceOf(AbstractTypeException.class);
    }

    @Test
    public void addType_WhenSingleAbstractClass_ThenAbstractTypeException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.addType(ClassBasicAbstract.class, ConstructionPolicy.CONSTRUCTION))
                  .isInstanceOf(AbstractTypeException.class);
    }

    @Test
    public void addType_WhenRegisterAnnotatedTypeAndSubtype_ThenAnnotatedTypeRegistrationException()
    {
        Assertions.assertThatThrownBy(() -> testObject.addType(ClassRegisterConcrete.class,
                                                               ClassRegisterDerivedFromRegister.class,
                                                               ConstructionPolicy.CONSTRUCTION))
                  .isInstanceOf(AnnotatedTypeRegistrationException.class);
    }

    @Test
    public void addType_WhenSelfRegisterAnnotatedTypeAndSubtype_ThenAnnotatedTypeRegistrationException()
    {
        Assertions.assertThatThrownBy(() -> testObject.addType(ClassRegisterSelf.class,
                                                               ClassRegisterDerivedFromSelfRegister.class,
                                                               ConstructionPolicy.CONSTRUCTION))
                  .isInstanceOf(AnnotatedTypeRegistrationException.class);
    }

    @Test
    public void addType_WhenRegisterTypeAndNotSubtype_ThenNotDerivedTypeException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.addType(ClassRegisterIncorrectOtherClass.class,
                                                   ConstructionPolicy.CONSTRUCTION))
                  .isInstanceOf(NotDerivedTypeException.class);
    }

    @Test
    public void addType_WhenRegisterTypeAndAbstract_ThenAbstractTypeException()
    {
        Assertions.assertThatThrownBy(() -> testObject.addType(ClassRegisterAbstractIncorrect.class,
                                                               ConstructionPolicy.CONSTRUCTION))
                  .isInstanceOf(AbstractTypeException.class);
    }

    @Test
    public void addType_WhenSelfRegisterAbstract_ThenAbstractTypeException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.addType(ClassRegisterSelfAbstractIncorrect.class,
                                                   ConstructionPolicy.CONSTRUCTION))
                  .isInstanceOf(AbstractTypeException.class);
    }

    @Test
    public void addType_WhenRegisteredInstance_ThenRegistrationException()
    {
        // given
        String string = "String";
        Class<ClassBasicStringGetter> type = ClassBasicStringGetter.class;
        ClassBasicStringGetter instance = new ClassBasicStringGetter(string);

        testObject.addInstance(type, instance);

        // then
        Assertions.assertThatThrownBy(
                          () -> testObject.addType(type, ConstructionPolicy.CONSTRUCTION))
                  .isInstanceOf(RegistrationException.class);
    }

    @Test
    public void addType_WhenPrimitiveType_ThenRegistrationException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.addType(boolean.class, ConstructionPolicy.CONSTRUCTION))
                  .isInstanceOf(RegistrationException.class);
    }

    // endregion
    // region addInstance/findInstance

    @Test
    public void addInstance_findInstance_WhenInstancePresent_ThenInstance()
    {
        // given
        String string = "String";
        ClassBasicStringGetter instance = new ClassBasicStringGetter(string);

        // when
        testObject.addInstance(ClassBasicStringGetter.class, instance);

        Instance<ClassBasicStringGetter> result =
                testObject.findInstance(ClassBasicStringGetter.class);

        // then
        Assertions.assertThat(result.exists()).isTrue();
        Assertions.assertThat(result.extract()).isSameAs(instance);
        Assertions.assertThat(result.extract().getString()).isEqualTo(string);
    }

    // endregion
    // region addInstance

    @Test
    public void addInstance_WhenRegisteredType_ThenRegistrationException()
    {
        // given
        String string = "String";
        Class<ClassBasicStringGetter> type = ClassBasicStringGetter.class;
        ClassBasicStringGetter instance = new ClassBasicStringGetter(string);

        testObject.addType(type, ConstructionPolicy.CONSTRUCTION);

        // then
        Assertions.assertThatThrownBy(() -> testObject.addInstance(type, instance))
                  .isInstanceOf(RegistrationException.class);
    }

    @Test
    public void addInstance_WhenAnnotatedType_ThenRegistrationException()
    {
        Assertions.assertThatThrownBy(() -> testObject.addInstance(ClassRegisterConcrete.class,
                                                                   new ClassRegisterConcrete()))
                  .isInstanceOf(RegistrationException.class);
    }

    @Test
    public void addInstance_WhenNullInstance_ThenNullPointerException()
    {
        Assertions.assertThatThrownBy(() -> testObject.addInstance(ClassBasicAbstract.class, null))
                  .isInstanceOf(NullPointerException.class);
    }

    // endregion
    // region findType

    @Test
    public void findType_WhenPrimitive_ThenFoundMapping()
    {
        // when
        TypeConstruction<?> result = testObject.findType(double.class);

        // then
        Assertions.assertThat(result.type()).isEqualTo(double.class);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.CONSTRUCTION);
    }

    @Test
    public void findType_WhenAbstractClass_ThenFoundMapping()
    {
        // given
        testObject.addType(InterfaceBasic.class, ClassBasicAbstract.class,
                           ConstructionPolicy.CONSTRUCTION);
        testObject.addType(ClassBasicAbstract.class, ClassBasicInheritsFromAbstract.class,
                           ConstructionPolicy.CONSTRUCTION);

        // when
        TypeConstruction<? extends InterfaceBasic> result =
                testObject.findType(InterfaceBasic.class);

        // then
        Assertions.assertThat(result.type()).isEqualTo(ClassBasicInheritsFromAbstract.class);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.CONSTRUCTION);
    }

    @Test
    public void findType_WhenConcreteClassNotRegistered_ThenFoundNewMapping()
    {
        // given
        Class<ClassBasicInheritsFromAbstract> type = ClassBasicInheritsFromAbstract.class;

        // when
        TypeConstruction<? extends ClassBasicInheritsFromAbstract> result =
                testObject.findType(type);

        // then
        Assertions.assertThat(result.type()).isEqualTo(type);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.CONSTRUCTION);
    }

    @Test
    public void findType_WhenAnnotatedClass_ThenMappingFromAnnotation()
    {
        // when
        TypeConstruction<? extends ClassRegisterAbstract> result =
                testObject.findType(ClassRegisterAbstract.class);

        // then
        Assertions.assertThat(result.type()).isEqualTo(ClassRegisterDerivedFromRegister.class);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.CONSTRUCTION);
    }

    @Test
    public void findType_WhenDifferentPolicy_ThenMixingPoliciesException()
    {
        // given
        testObject.addType(InterfaceBasic.class, ClassBasicAbstract.class,
                           ConstructionPolicy.CONSTRUCTION);
        testObject.addType(ClassBasicAbstract.class, ClassBasicInheritsFromAbstract.class,
                           ConstructionPolicy.SINGLETON);

        // then
        Assertions.assertThatThrownBy(() -> testObject.findType(InterfaceBasic.class))
                  .isInstanceOf(MixingPoliciesException.class);
    }

    @Test
    public void findType_WhenSelfRegisterClass_ThenMapping()
    {
        // when
        TypeConstruction<? extends ClassRegisterSelf> result =
                testObject.findType(ClassRegisterSelf.class);

        // then
        Assertions.assertThat(result.type()).isEqualTo(ClassRegisterSelf.class);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.CONSTRUCTION);
    }

    @Test
    public void findType_whenRegisteredInstance_ThenMappingToThisType()
    {
        // given
        Class<ClassBasicInheritsFromAbstract> type = ClassBasicInheritsFromAbstract.class;

        testObject.addInstance(type, new ClassBasicInheritsFromAbstract());

        // when
        TypeConstruction<? extends ClassBasicInheritsFromAbstract> result =
                testObject.findType(type);

        // then
        Assertions.assertThat(result.type()).isEqualTo(type);
        Assertions.assertThat(result.policy()).isEqualTo(ConstructionPolicy.SINGLETON);
    }

    // endregion
    // region findInstance

    @Test
    public void findInstance_WhenInstanceAbsent_ThenMappingNone()
    {
        // when
        Instance<ClassBasicStringGetter> result =
                testObject.findInstance(ClassBasicStringGetter.class);

        // then
        Assertions.assertThat(result.exists()).isFalse();
    }

    // endregion
    // region contains

    @Test
    public void contains_WhenAddedInstance_ThenTrue()
    {
        // given
        testObject.addInstance(ClassBasicStringGetter.class, new ClassBasicStringGetter("String"));

        // when
        boolean result = testObject.contains(ClassBasicStringGetter.class);

        // then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void contains_WhenAbstractTypeIsAbsent_ThenFalse()
    {
        // when
        boolean result = testObject.contains(InterfaceBasic.class);

        // then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void contains_WhenConcreteTypeIsAbsent_ThenFalse()
    {
        // when
        boolean result = testObject.contains(ClassBasicInheritsFromAbstract.class);

        // then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void contains_WhenAnnotatedTypeIsAbsent_ThenTrue()
    {
        // when
        boolean result = testObject.contains(ClassRegisterConcrete.class);

        // then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void contains_WhenAnnotatedTypeIsPresent_ThenTrue()
    {
        // given
        Class<ClassRegisterConcrete> type = ClassRegisterConcrete.class;

        testObject.addType(type, ConstructionPolicy.CONSTRUCTION);

        // when
        boolean result = testObject.contains(type);

        // then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void contains_WhenInsertedTypeIsPresent_ThenTrue()
    {
        // given
        Class<InterfaceBasic> type = InterfaceBasic.class;

        testObject.addType(type, ClassBasicAbstract.class, ConstructionPolicy.CONSTRUCTION);

        // when
        boolean result = testObject.contains(type);

        // then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void contains_WhenAnnotatedTypeIncorrect_ThenFalse()
    {
        // when
        boolean result = testObject.contains(ClassRegisterSelfAbstractIncorrect.class);

        // then
        Assertions.assertThat(result).isFalse();
    }

    // endregion
    // region addSingleton/findInstance

    @Test
    public void addSingleton_findInstance_WhenRegisteredTypeAsSingleton_ThenSingleton()
    {
        // given
        ClassBasicInheritsFromAbstract singleton = new ClassBasicInheritsFromAbstract();

        testObject.addType(ClassBasicAbstract.class, ClassBasicInheritsFromAbstract.class,
                           ConstructionPolicy.SINGLETON);

        // when
        testObject.addSingleton(ClassBasicAbstract.class, singleton);

        Instance<ClassBasicAbstract> result = testObject.findInstance(ClassBasicAbstract.class);

        // then
        Assertions.assertThat(result.exists()).isTrue();
        Assertions.assertThat(result.extract()).isSameAs(singleton);
    }

    @Test
    public void addSingleton_findInstance_WhenRegisteredTypeAsNotSingleton_ThenNoInstance()
    {
        // given
        ClassBasicInheritsFromAbstract singleton = new ClassBasicInheritsFromAbstract();

        testObject.addType(ClassBasicAbstract.class, ClassBasicInheritsFromAbstract.class,
                           ConstructionPolicy.CONSTRUCTION);

        // when
        testObject.addSingleton(ClassBasicAbstract.class, singleton);

        Instance<ClassBasicAbstract> result = testObject.findInstance(ClassBasicAbstract.class);

        // then
        Assertions.assertThat(result.exists()).isFalse();
    }

    @Test
    public void addSingleton_findInstance_WhenNotRegisteredType_ThenNoInstance()
    {
        // given
        ClassBasicInheritsFromAbstract singleton = new ClassBasicInheritsFromAbstract();

        // when
        testObject.addSingleton(ClassBasicAbstract.class, singleton);

        Instance<ClassBasicAbstract> result = testObject.findInstance(ClassBasicAbstract.class);

        // then
        Assertions.assertThat(result.exists()).isFalse();
    }

    // endregion
}
