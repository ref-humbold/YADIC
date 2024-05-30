package dicontainer.dictionary;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dicontainer.ConstructionPolicy;
import dicontainer.auxiliary.basic.ClassBasicAbstract;
import dicontainer.auxiliary.basic.ClassBasicInheritsFromAbstract;
import dicontainer.auxiliary.basic.ClassBasicStringGetter;
import dicontainer.auxiliary.basic.InterfaceBasic;
import dicontainer.auxiliary.register.*;
import dicontainer.dictionary.exception.AbstractTypeException;
import dicontainer.dictionary.exception.AnnotatedTypeRegistrationException;
import dicontainer.dictionary.exception.MixingPoliciesException;
import dicontainer.dictionary.exception.NotDerivedTypeException;
import dicontainer.dictionary.exception.RegistrationException;
import dicontainer.dictionary.valuetypes.Instance;
import dicontainer.dictionary.valuetypes.NullInstanceException;
import dicontainer.dictionary.valuetypes.Subtype;

public class DiDictionaryTest
{
    private DiDictionary testObject;

    @BeforeEach
    public void setUp()
    {
        testObject = new DiDictionary();
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

        Subtype<? extends InterfaceBasic> result = testObject.findType(type);
        // then
        Assertions.assertThat(result.subtype).isEqualTo(subtype);
        Assertions.assertThat(result.policy).isEqualTo(ConstructionPolicy.SINGLETON);
    }

    @Test
    public void addType_findType_WhenSpecifyTypeAndPolicy_ThenThisTypeInserted()
    {
        // given
        Class<ClassBasicInheritsFromAbstract> type = ClassBasicInheritsFromAbstract.class;
        // when
        testObject.addType(type, ConstructionPolicy.SINGLETON);

        Subtype<? extends ClassBasicInheritsFromAbstract> result = testObject.findType(type);
        // then
        Assertions.assertThat(result.subtype).isEqualTo(type);
        Assertions.assertThat(result.policy).isEqualTo(ConstructionPolicy.SINGLETON);
    }

    @Test
    public void addType_findType_WhenTypeHasSelfRegisterAnnotation_ThenThisTypeInserted()
    {
        // given
        Class<ClassRegisterSelf> type = ClassRegisterSelf.class;
        // when
        testObject.addType(type);

        Subtype<? extends ClassRegisterSelf> result = testObject.findType(type);
        // then
        Assertions.assertThat(result.subtype).isEqualTo(type);
        Assertions.assertThat(result.policy).isEqualTo(ConstructionPolicy.defaultPolicy);
    }

    @Test
    public void addType_findType_WhenTypeHasRegisterAnnotation_ThenSubtypeInserted()
    {
        // given
        Class<ClassRegisterConcrete> type = ClassRegisterConcrete.class;
        // when
        testObject.addType(type);

        Subtype<? extends ClassRegisterConcrete> result = testObject.findType(type);
        // then
        Assertions.assertThat(result.subtype).isEqualTo(ClassRegisterDerivedFromRegister.class);
        Assertions.assertThat(result.policy).isEqualTo(ConstructionPolicy.defaultPolicy);
    }

    // endregion
    // region addType

    @Test
    public void addType_WhenSingleInterface_ThenAbstractTypeException()
    {
        Assertions.assertThatThrownBy(() -> testObject.addType(InterfaceBasic.class))
                  .isInstanceOf(AbstractTypeException.class);
    }

    @Test
    public void addType_WhenSingleAbstractClass_ThenAbstractTypeException()
    {
        Assertions.assertThatThrownBy(() -> testObject.addType(ClassBasicAbstract.class))
                  .isInstanceOf(AbstractTypeException.class);
    }

    @Test
    public void addType_WhenRegisterAnnotatedTypeAndSubtype_ThenAnnotatedTypeRegistrationException()
    {
        Assertions.assertThatThrownBy(() -> testObject.addType(ClassRegisterConcrete.class,
                                                               ClassRegisterDerivedFromRegister.class,
                                                               ConstructionPolicy.defaultPolicy))
                  .isInstanceOf(AnnotatedTypeRegistrationException.class);
    }

    @Test
    public void addType_WhenSelfRegisterAnnotatedTypeAndSubtype_ThenAnnotatedTypeRegistrationException()
    {
        Assertions.assertThatThrownBy(() -> testObject.addType(ClassRegisterSelf.class,
                                                               ClassRegisterDerivedFromSelfRegister.class,
                                                               ConstructionPolicy.defaultPolicy))
                  .isInstanceOf(AnnotatedTypeRegistrationException.class);
    }

    @Test
    public void addType_WhenRegisterTypeAndNotSubtype_ThenNotDerivedTypeException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.addType(ClassRegisterIncorrectOtherClass.class,
                                                   ConstructionPolicy.defaultPolicy))
                  .isInstanceOf(NotDerivedTypeException.class);
    }

    @Test
    public void addType_WhenRegisterTypeAndAbstract_ThenAbstractTypeException()
    {
        Assertions.assertThatThrownBy(() -> testObject.addType(ClassRegisterAbstractIncorrect.class,
                                                               ConstructionPolicy.defaultPolicy))
                  .isInstanceOf(AbstractTypeException.class);
    }

    @Test
    public void addType_WhenSelfRegisterAbstract_ThenAbstractTypeException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.addType(ClassRegisterSelfAbstractIncorrect.class,
                                                   ConstructionPolicy.defaultPolicy))
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
        Assertions.assertThatThrownBy(() -> testObject.addType(type))
                  .isInstanceOf(RegistrationException.class);
    }

    @Test
    public void addType_WhenPrimitiveType_ThenRegistrationException()
    {
        Assertions.assertThatThrownBy(() -> testObject.addType(boolean.class))
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

        testObject.addType(type);
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
    public void addInstance_WhenNullInstance_ThenNullInstanceException()
    {
        Assertions.assertThatThrownBy(() -> testObject.addInstance(ClassBasicAbstract.class, null))
                  .isInstanceOf(NullInstanceException.class);
    }

    // endregion
    // region findType

    @Test
    public void findType_WhenPrimitive_ThenFoundMapping()
    {
        // when
        Subtype<?> result = testObject.findType(double.class);
        // then
        Assertions.assertThat(result.subtype).isEqualTo(double.class);
        Assertions.assertThat(result.policy).isEqualTo(ConstructionPolicy.defaultPolicy);
    }

    @Test
    public void findType_WhenAbstractClass_ThenFoundMapping()
    {
        // given
        testObject.addType(InterfaceBasic.class, ClassBasicAbstract.class,
                           ConstructionPolicy.defaultPolicy);
        testObject.addType(ClassBasicAbstract.class, ClassBasicInheritsFromAbstract.class,
                           ConstructionPolicy.defaultPolicy);
        // when
        Subtype<? extends InterfaceBasic> result = testObject.findType(InterfaceBasic.class);
        // then
        Assertions.assertThat(result.subtype).isEqualTo(ClassBasicInheritsFromAbstract.class);
        Assertions.assertThat(result.policy).isEqualTo(ConstructionPolicy.defaultPolicy);
    }

    @Test
    public void findType_WhenConcreteClassNotRegistered_ThenFoundNewMapping()
    {
        // given
        Class<ClassBasicInheritsFromAbstract> type = ClassBasicInheritsFromAbstract.class;
        // when
        Subtype<? extends ClassBasicInheritsFromAbstract> result = testObject.findType(type);
        // then
        Assertions.assertThat(result.subtype).isEqualTo(type);
        Assertions.assertThat(result.policy).isEqualTo(ConstructionPolicy.defaultPolicy);
    }

    @Test
    public void findType_WhenAnnotatedClass_ThenMappingFromAnnotation()
    {
        // when
        Subtype<? extends ClassRegisterAbstract> result =
                testObject.findType(ClassRegisterAbstract.class);
        // then
        Assertions.assertThat(result.subtype).isEqualTo(ClassRegisterDerivedFromRegister.class);
        Assertions.assertThat(result.policy).isEqualTo(ConstructionPolicy.defaultPolicy);
    }

    @Test
    public void findType_WhenDifferentPolicy_ThenMixingPoliciesException()
    {
        // given
        testObject.addType(InterfaceBasic.class, ClassBasicAbstract.class,
                           ConstructionPolicy.CONSTRUCT);
        testObject.addType(ClassBasicAbstract.class, ClassBasicInheritsFromAbstract.class,
                           ConstructionPolicy.SINGLETON);
        Assertions.assertThatThrownBy(() -> testObject.findType(InterfaceBasic.class))
                  .isInstanceOf(MixingPoliciesException.class);
    }

    @Test
    public void findType_WhenSelfRegisterClass_ThenMapping()
    {
        // when
        Subtype<? extends ClassRegisterSelf> result = testObject.findType(ClassRegisterSelf.class);
        // then
        Assertions.assertThat(result.subtype).isEqualTo(ClassRegisterSelf.class);
        Assertions.assertThat(result.policy).isEqualTo(ConstructionPolicy.defaultPolicy);
    }

    @Test
    public void findType_whenRegisteredInstance_ThenMappingToThisType()
    {
        // given
        Class<ClassBasicInheritsFromAbstract> type = ClassBasicInheritsFromAbstract.class;

        testObject.addInstance(type, new ClassBasicInheritsFromAbstract());
        // when
        Subtype<? extends ClassBasicInheritsFromAbstract> result = testObject.findType(type);
        // then
        Assertions.assertThat(result.subtype).isEqualTo(type);
        Assertions.assertThat(result.policy).isEqualTo(ConstructionPolicy.SINGLETON);
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

        testObject.addType(type);
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

        testObject.addType(type, ClassBasicAbstract.class);
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
                           ConstructionPolicy.CONSTRUCT);
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
