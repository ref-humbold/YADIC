package dicontainer.dictionary;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import dicontainer.ConstructionPolicy;
import dicontainer.auxiliary.basic.ClassBasicAbstract;
import dicontainer.auxiliary.basic.ClassBasicInheritsFromAbstract;
import dicontainer.auxiliary.basic.ClassBasicStringGetter;
import dicontainer.auxiliary.basic.InterfaceBasic;
import dicontainer.auxiliary.constructor.ClassConstructorDefault;
import dicontainer.auxiliary.register.*;
import dicontainer.commons.Instance;
import dicontainer.exception.AbstractTypeException;
import dicontainer.exception.MissingDependenciesException;
import dicontainer.exception.NotDerivedTypeException;

class RegistrationDictionaryTest
{
    private RegistrationDictionary testObject;

    @BeforeEach
    public void setUp()
    {
        testObject = new RegistrationDictionary();
    }

    @AfterEach
    public void tearDown()
    {
        testObject = null;
    }

    // region insertType/getType

    @Test
    public void insertType_getType_WhenSpecifyTypeWithSubtype_ThenSubtypeInserted()
    {
        // given
        Class<InterfaceBasic> type = InterfaceBasic.class;
        Class<ClassBasicAbstract> subtype = ClassBasicAbstract.class;
        // when
        testObject.insertType(type, subtype);
        SubtypeMapping<? extends InterfaceBasic> result = testObject.getType(type);
        // then
        Assertions.assertEquals(subtype, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.getDefault(), result.policy);
    }

    @Test
    public void insertType_getType_WhenSpecifyTypeWithSubtypeAndPolicy_ThenSubtypeInserted()
    {
        // given
        Class<InterfaceBasic> type = InterfaceBasic.class;
        Class<ClassBasicAbstract> subtype = ClassBasicAbstract.class;
        // when
        testObject.insertType(type, subtype, ConstructionPolicy.SINGLETON);
        SubtypeMapping<? extends InterfaceBasic> result = testObject.getType(type);
        // then
        Assertions.assertEquals(subtype, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.SINGLETON, result.policy);
    }

    @Test
    public void insertType_getType_WhenSpecifyType_ThenThisTypeInserted()
    {
        // given
        Class<ClassBasicInheritsFromAbstract> type = ClassBasicInheritsFromAbstract.class;
        // when
        testObject.insertType(type);
        SubtypeMapping<? extends ClassBasicInheritsFromAbstract> result = testObject.getType(type);
        // then
        Assertions.assertEquals(type, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.getDefault(), result.policy);
    }

    @Test
    public void insertType_getType_WhenTypeHasSelfRegisterAnnotation_ThenThisTypeInserted()
    {
        // given
        Class<ClassRegisterSelf> type = ClassRegisterSelf.class;
        // when
        testObject.insertType(type);
        SubtypeMapping<? extends ClassRegisterSelf> result = testObject.getType(type);
        // then
        Assertions.assertEquals(type, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.getDefault(), result.policy);
    }

    @Test
    public void insertType_getType_WhenTypeHasRegisterAnnotation_ThenSubtypeInserted()
    {
        // given
        Class<ClassRegisterConcrete> type = ClassRegisterConcrete.class;
        // when
        testObject.insertType(type);
        SubtypeMapping<? extends ClassRegisterConcrete> result = testObject.getType(type);
        // then
        Assertions.assertEquals(ClassRegisterDerivedFromRegister.class, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.getDefault(), result.policy);
    }

    // endregion
    // region insertType

    @Test
    public void insertType_WhenRegisterAnnotatedTypeAndSubtype_ThenChangingAnnotatedRegistrationException()
    {
        // when
        Executable executable = () -> testObject.insertType(ClassRegisterConcrete.class,
                                                            ClassRegisterDerivedFromRegister.class);
        // then
        Assertions.assertThrows(ChangingAnnotatedRegistrationException.class, executable);
    }

    @Test
    public void insertType_WhenSelfRegisterAnnotatedTypeAndSubtype_ThenChangingAnnotatedRegistrationException()
    {
        // when
        Executable executable = () -> testObject.insertType(ClassRegisterSelf.class,
                                                            ClassRegisterDerivedFromSelfRegister.class);
        // then
        Assertions.assertThrows(ChangingAnnotatedRegistrationException.class, executable);
    }

    @Test
    public void insertType_WhenRegisterTypeAndNotSubtype_ThenNotDerivedTypeException()
    {
        // when
        Executable executable = () -> testObject.insertType(ClassRegisterIncorrectOtherClass.class);
        // then
        Assertions.assertThrows(NotDerivedTypeException.class, executable);
    }

    @Test
    public void insertType_WhenRegisterTypeAndAbstract_ThenAbstractTypeException()
    {
        // when
        Executable executable = () -> testObject.insertType(ClassRegisterAbstractIncorrect.class);
        // then
        Assertions.assertThrows(AbstractTypeException.class, executable);
    }

    @Test
    public void insertType_WhenSelfRegisterAbstract_ThenAbstractTypeException()
    {
        // when
        Executable executable =
                () -> testObject.insertType(ClassRegisterSelfAbstractIncorrect.class);
        // then
        Assertions.assertThrows(AbstractTypeException.class, executable);
    }

    // endregion
    //region getType

    @Test
    public void getType_WhenConcreteTypeIsAbsent_ThenCreatedNewMapping()
    {
        // given
        Class<ClassBasicInheritsFromAbstract> type = ClassBasicInheritsFromAbstract.class;
        // when
        SubtypeMapping<? extends ClassBasicInheritsFromAbstract> result = testObject.getType(type);
        // then
        Assertions.assertEquals(type, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.getDefault(), result.policy);
    }

    @Test
    public void getType_WhenAnnotatedTypeIsAbsent_ThenMappingFromAnnotation()
    {
        // when
        SubtypeMapping<? extends ClassRegisterConcrete> result =
                testObject.getType(ClassRegisterConcrete.class);
        // then
        Assertions.assertEquals(ClassRegisterDerivedFromRegister.class, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.getDefault(), result.policy);
    }

    @Test
    public void getType_WhenAbstractTypeIsAbsent_ThenMissingDependenciesException()
    {
        // when
        Executable executable = () -> testObject.getType(InterfaceBasic.class);
        // then
        Assertions.assertThrows(MissingDependenciesException.class, executable);
    }

    // endregion
    // region insertInstance/getInstance

    @Test
    public void insertInstance_getInstance_WhenAbsent_ThenCreatesNewMapping()
    {
        // given
        ClassConstructorDefault instance = new ClassConstructorDefault();
        // when
        testObject.insertInstance(ClassConstructorDefault.class, instance);
        Instance<ClassConstructorDefault> result =
                testObject.getInstance(ClassConstructorDefault.class);
        // then
        Assertions.assertTrue(result.exists());
        Assertions.assertSame(instance, result.extract());
    }

    @Test
    public void insertInstance_getInstance_WhenInstancePresent_ThenMappingUpdated()
    {
        // given
        ClassConstructorDefault oldInstance = new ClassConstructorDefault();
        ClassConstructorDefault newInstance = new ClassConstructorDefault();
        testObject.insertInstance(ClassConstructorDefault.class, oldInstance);
        // when
        testObject.insertInstance(ClassConstructorDefault.class, newInstance);
        Instance<ClassConstructorDefault> result =
                testObject.getInstance(ClassConstructorDefault.class);
        // then
        Assertions.assertTrue(result.exists());
        Assertions.assertSame(newInstance, result.extract());
        Assertions.assertNotSame(oldInstance, result.extract());
    }

    // endregion
    // region insertInstance

    @Test
    public void insertInstance_WhenInstanceNull_ThenNullPointerException()
    {
        // when
        Executable executable = () -> testObject.insertInstance(ClassBasicStringGetter.class, null);
        // then
        Assertions.assertThrows(NullPointerException.class, executable);
    }

    @Test
    public void insertInstance_WhenAnnotated_ThenChangingAnnotatedRegistrationException()
    {
        // given
        ClassRegisterConcrete instance = new ClassRegisterConcrete();
        // when
        Executable executable =
                () -> testObject.insertInstance(ClassRegisterConcrete.class, instance);
        // then
        Assertions.assertThrows(ChangingAnnotatedRegistrationException.class, executable);
    }

    // endregion
    // region getInstance

    @Test
    public void getInstance_WhenInstanceAbsent_ThenMappingNone()
    {
        // when
        Instance<ClassBasicStringGetter> result =
                testObject.getInstance(ClassBasicStringGetter.class);
        // then
        Assertions.assertFalse(result.exists());
    }

    // endregion
    //region containsType

    @Test
    public void containsType_WhenAbstractTypeIsAbsent_ThenFalse()
    {
        // when
        boolean result = testObject.containsType(InterfaceBasic.class);
        // then
        Assertions.assertFalse(result);
    }

    @Test
    public void containsType_WhenConcreteTypeIsAbsent_ThenFalse()
    {
        // when
        boolean result = testObject.containsType(ClassBasicInheritsFromAbstract.class);
        // then
        Assertions.assertFalse(result);
    }

    @Test
    public void containsType_WhenAnnotatedTypeIsAbsent_ThenTrue()
    {
        // when
        boolean result = testObject.containsType(ClassRegisterConcrete.class);
        // then
        Assertions.assertTrue(result);
    }

    @Test
    public void containsType_WhenAnnotatedTypeIsPresent_ThenTrue()
    {
        // given
        Class<ClassRegisterConcrete> type = ClassRegisterConcrete.class;
        testObject.insertType(type);
        // when
        boolean result = testObject.containsType(type);
        // then
        Assertions.assertTrue(result);
    }

    @Test
    public void containsType_WhenInsertedTypeIsPresent_ThenTrue()
    {
        // given
        Class<InterfaceBasic> type = InterfaceBasic.class;
        testObject.insertType(type, ClassBasicAbstract.class);
        // when
        boolean result = testObject.containsType(type);
        // then
        Assertions.assertTrue(result);
    }

    @Test
    public void containsType_WhenAnnotatedTypeIncorrect_ThenFalse()
    {
        // when
        boolean result = testObject.containsType(ClassRegisterSelfAbstractIncorrect.class);
        // then
        Assertions.assertFalse(result);
    }

    // endregion
    // region containsInstance

    @Test
    public void containsInstance_WhenPresent_ThenTrue()
    {
        // given
        ClassConstructorDefault instance = new ClassConstructorDefault();

        testObject.insertInstance(ClassConstructorDefault.class, instance);
        // when
        boolean result = testObject.containsInstance(ClassConstructorDefault.class);
        // then
        Assertions.assertTrue(result);
    }

    @Test
    public void containsInstance_WhenAbsent_ThenTrue()
    {
        // when
        boolean result = testObject.containsInstance(ClassConstructorDefault.class);
        // then
        Assertions.assertFalse(result);
    }

    // endregion
    // region findType

    @Test
    public void findType_WhenAbstractClass_ThenFoundMapping()
    {
        // given
        testObject.insertType(InterfaceBasic.class, ClassBasicAbstract.class);
        testObject.insertType(ClassBasicAbstract.class, ClassBasicInheritsFromAbstract.class);
        // when
        SubtypeMapping<? extends InterfaceBasic> result = testObject.findType(InterfaceBasic.class);
        // then
        Assertions.assertEquals(ClassBasicInheritsFromAbstract.class, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.getDefault(), result.policy);
    }

    @Test
    public void findType_WhenConcreteClassNotRegistered_ThenFoundNewMapping()
    {
        // given
        Class<ClassBasicInheritsFromAbstract> type = ClassBasicInheritsFromAbstract.class;
        // when
        SubtypeMapping<? extends ClassBasicInheritsFromAbstract> result = testObject.findType(type);
        // then
        Assertions.assertEquals(type, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.getDefault(), result.policy);
    }

    @Test
    public void findType_WhenAnnotatedClass_ThenMappingFromAnnotation()
    {
        // when
        SubtypeMapping<? extends ClassRegisterAbstract> result =
                testObject.findType(ClassRegisterAbstract.class);
        // then
        Assertions.assertEquals(ClassRegisterDerivedFromRegister.class, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.getDefault(), result.policy);
    }

    @Test
    public void findType_WhenDifferentPolicy_ThenMixingPoliciesException()
    {
        // given
        testObject.insertType(InterfaceBasic.class, ClassBasicAbstract.class,
                              ConstructionPolicy.CONSTRUCT);
        testObject.insertType(ClassBasicAbstract.class, ClassBasicInheritsFromAbstract.class,
                              ConstructionPolicy.SINGLETON);
        // when
        Executable executable = () -> testObject.findType(InterfaceBasic.class);
        // then
        Assertions.assertThrows(MixingPoliciesException.class, executable);
    }

    @Test
    public void findType_WhenSelfRegisterClass_ThenMapping()
    {
        // when
        SubtypeMapping<? extends ClassRegisterSelf> result =
                testObject.findType(ClassRegisterSelf.class);
        // then
        Assertions.assertEquals(ClassRegisterSelf.class, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.getDefault(), result.policy);
    }

    // endregion
}
