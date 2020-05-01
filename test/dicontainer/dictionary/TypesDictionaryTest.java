package dicontainer.dictionary;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import dicontainer.ConstructionPolicy;
import dicontainer.auxiliary.basic.ClassBasicAbstract;
import dicontainer.auxiliary.basic.ClassBasicInheritsFromAbstract;
import dicontainer.auxiliary.basic.InterfaceBasic;
import dicontainer.auxiliary.register.*;
import dicontainer.commons.Instance;
import dicontainer.exception.AbstractTypeException;
import dicontainer.exception.MissingDependenciesException;
import dicontainer.exception.NotDerivedTypeException;

class TypesDictionaryTest
{
    private TypesDictionary testObject;

    @BeforeEach
    public void setUp()
    {
        testObject = new TypesDictionary();
    }

    @AfterEach
    public void tearDown()
    {
        testObject = null;
    }

    // region insert/get

    @Test
    public void insert_get_WhenSpecifyTypeWithSubtypeAndPolicy_ThenSubtypeInserted()
    {
        // given
        Class<InterfaceBasic> type = InterfaceBasic.class;
        Class<ClassBasicAbstract> subtype = ClassBasicAbstract.class;
        // when
        testObject.insert(type, subtype, ConstructionPolicy.SINGLETON);
        SubtypeMapping<? extends InterfaceBasic> result = testObject.get(type);
        // then
        Assertions.assertEquals(subtype, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.SINGLETON, result.policy);
    }

    @Test
    public void insert_get_WhenSpecifyTypeAndPolicy_ThenThisTypeInserted()
    {
        // given
        Class<ClassBasicInheritsFromAbstract> type = ClassBasicInheritsFromAbstract.class;
        // when
        testObject.insert(type, ConstructionPolicy.SINGLETON);
        SubtypeMapping<? extends ClassBasicInheritsFromAbstract> result = testObject.get(type);
        // then
        Assertions.assertEquals(type, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.SINGLETON, result.policy);
    }

    @Test
    public void insert_get_WhenTypeHasSelfRegisterAnnotation_ThenThisTypeInserted()
    {
        // given
        Class<ClassRegisterSelf> type = ClassRegisterSelf.class;
        // when
        testObject.insert(type, ConstructionPolicy.getDefault());
        SubtypeMapping<? extends ClassRegisterSelf> result = testObject.get(type);
        // then
        Assertions.assertEquals(type, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.getDefault(), result.policy);
    }

    @Test
    public void insert_get_WhenTypeHasRegisterAnnotation_ThenSubtypeInserted()
    {
        // given
        Class<ClassRegisterConcrete> type = ClassRegisterConcrete.class;
        // when
        testObject.insert(type, ConstructionPolicy.getDefault());
        SubtypeMapping<? extends ClassRegisterConcrete> result = testObject.get(type);
        // then
        Assertions.assertEquals(ClassRegisterDerivedFromRegister.class, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.getDefault(), result.policy);
    }

    // endregion
    // region insert

    @Test
    public void insert_WhenRegisterAnnotatedTypeAndSubtype_ThenAnnotatedTypeRegistrationException()
    {
        // when
        Executable executable = () -> testObject.insert(ClassRegisterConcrete.class,
                                                        ClassRegisterDerivedFromRegister.class,
                                                        ConstructionPolicy.getDefault());
        // then
        Assertions.assertThrows(AnnotatedTypeRegistrationException.class, executable);
    }

    @Test
    public void insert_WhenSelfRegisterAnnotatedTypeAndSubtype_ThenAnnotatedTypeRegistrationException()
    {
        // when
        Executable executable = () -> testObject.insert(ClassRegisterSelf.class,
                                                        ClassRegisterDerivedFromSelfRegister.class,
                                                        ConstructionPolicy.getDefault());
        // then
        Assertions.assertThrows(AnnotatedTypeRegistrationException.class, executable);
    }

    @Test
    public void insert_WhenRegisterTypeAndNotSubtype_ThenNotDerivedTypeException()
    {
        // when
        Executable executable = () -> testObject.insert(ClassRegisterIncorrectOtherClass.class,
                                                        ConstructionPolicy.getDefault());
        // then
        Assertions.assertThrows(NotDerivedTypeException.class, executable);
    }

    @Test
    public void insert_WhenRegisterTypeAndAbstract_ThenAbstractTypeException()
    {
        // when
        Executable executable = () -> testObject.insert(ClassRegisterAbstractIncorrect.class,
                                                        ConstructionPolicy.getDefault());
        // then
        Assertions.assertThrows(AbstractTypeException.class, executable);
    }

    @Test
    public void insert_WhenSelfRegisterAbstract_ThenAbstractTypeException()
    {
        // when
        Executable executable = () -> testObject.insert(ClassRegisterSelfAbstractIncorrect.class,
                                                        ConstructionPolicy.getDefault());
        // then
        Assertions.assertThrows(AbstractTypeException.class, executable);
    }

    // endregion
    //region get

    @Test
    public void get_WhenConcreteTypeIsAbsent_ThenCreatedNewMapping()
    {
        // given
        Class<ClassBasicInheritsFromAbstract> type = ClassBasicInheritsFromAbstract.class;
        // when
        SubtypeMapping<? extends ClassBasicInheritsFromAbstract> result = testObject.get(type);
        // then
        Assertions.assertEquals(type, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.getDefault(), result.policy);
    }

    @Test
    public void get_WhenAnnotatedTypeIsAbsent_ThenMappingFromAnnotation()
    {
        // when
        SubtypeMapping<? extends ClassRegisterConcrete> result =
                testObject.get(ClassRegisterConcrete.class);
        // then
        Assertions.assertEquals(ClassRegisterDerivedFromRegister.class, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.getDefault(), result.policy);
    }

    @Test
    public void get_WhenAbstractTypeIsAbsent_ThenMissingDependenciesException()
    {
        // when
        Executable executable = () -> testObject.get(InterfaceBasic.class);
        // then
        Assertions.assertThrows(MissingDependenciesException.class, executable);
    }

    // endregion
    //region contains

    @Test
    public void contains_WhenAbstractTypeIsAbsent_ThenFalse()
    {
        // when
        boolean result = testObject.contains(InterfaceBasic.class);
        // then
        Assertions.assertFalse(result);
    }

    @Test
    public void contains_WhenConcreteTypeIsAbsent_ThenFalse()
    {
        // when
        boolean result = testObject.contains(ClassBasicInheritsFromAbstract.class);
        // then
        Assertions.assertFalse(result);
    }

    @Test
    public void contains_WhenAnnotatedTypeIsAbsent_ThenTrue()
    {
        // when
        boolean result = testObject.contains(ClassRegisterConcrete.class);
        // then
        Assertions.assertTrue(result);
    }

    @Test
    public void contains_WhenAnnotatedTypeIsPresent_ThenTrue()
    {
        // given
        Class<ClassRegisterConcrete> type = ClassRegisterConcrete.class;
        testObject.insert(type, ConstructionPolicy.getDefault());
        // when
        boolean result = testObject.contains(type);
        // then
        Assertions.assertTrue(result);
    }

    @Test
    public void contains_WhenInsertedTypeIsPresent_ThenTrue()
    {
        // given
        Class<InterfaceBasic> type = InterfaceBasic.class;
        testObject.insert(type, ClassBasicAbstract.class, ConstructionPolicy.getDefault());
        // when
        boolean result = testObject.contains(type);
        // then
        Assertions.assertTrue(result);
    }

    @Test
    public void contains_WhenAnnotatedTypeIncorrect_ThenFalse()
    {
        // when
        boolean result = testObject.contains(ClassRegisterSelfAbstractIncorrect.class);
        // then
        Assertions.assertFalse(result);
    }

    // endregion
    // region find

    @Test
    public void find_WhenAbstractClass_ThenFoundMapping()
    {
        // given
        testObject.insert(InterfaceBasic.class, ClassBasicAbstract.class,
                          ConstructionPolicy.getDefault());
        testObject.insert(ClassBasicAbstract.class, ClassBasicInheritsFromAbstract.class,
                          ConstructionPolicy.getDefault());
        // when
        SubtypeMapping<? extends InterfaceBasic> result = testObject.find(InterfaceBasic.class);
        // then
        Assertions.assertEquals(ClassBasicInheritsFromAbstract.class, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.getDefault(), result.policy);
    }

    @Test
    public void find_WhenConcreteClassNotRegistered_ThenFoundNewMapping()
    {
        // given
        Class<ClassBasicInheritsFromAbstract> type = ClassBasicInheritsFromAbstract.class;
        // when
        SubtypeMapping<? extends ClassBasicInheritsFromAbstract> result = testObject.find(type);
        // then
        Assertions.assertEquals(type, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.getDefault(), result.policy);
    }

    @Test
    public void find_WhenAnnotatedClass_ThenMappingFromAnnotation()
    {
        // when
        SubtypeMapping<? extends ClassRegisterAbstract> result =
                testObject.find(ClassRegisterAbstract.class);
        // then
        Assertions.assertEquals(ClassRegisterDerivedFromRegister.class, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.getDefault(), result.policy);
    }

    @Test
    public void find_WhenDifferentPolicy_ThenMixingPoliciesException()
    {
        // given
        testObject.insert(InterfaceBasic.class, ClassBasicAbstract.class,
                          ConstructionPolicy.CONSTRUCT);
        testObject.insert(ClassBasicAbstract.class, ClassBasicInheritsFromAbstract.class,
                          ConstructionPolicy.SINGLETON);
        // when
        Executable executable = () -> testObject.find(InterfaceBasic.class);
        // then
        Assertions.assertThrows(MixingPoliciesException.class, executable);
    }

    @Test
    public void find_WhenSelfRegisterClass_ThenMapping()
    {
        // when
        SubtypeMapping<? extends ClassRegisterSelf> result =
                testObject.find(ClassRegisterSelf.class);
        // then
        Assertions.assertEquals(ClassRegisterSelf.class, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.getDefault(), result.policy);
    }

    // endregion
    // region insertSingleton/getSingleton

    @Test
    public void insertSingleton_getSingleton_WhenRegisteredTypeAsSingleton_ThenSingleton()
    {
        // given
        ClassBasicInheritsFromAbstract singleton = new ClassBasicInheritsFromAbstract();

        testObject.insert(ClassBasicAbstract.class, ClassBasicInheritsFromAbstract.class,
                          ConstructionPolicy.SINGLETON);
        // when
        testObject.insertSingleton(ClassBasicAbstract.class, singleton);

        Instance<ClassBasicAbstract> result = testObject.getSingleton(ClassBasicAbstract.class);
        // then
        Assertions.assertTrue(result.exists());
        Assertions.assertSame(singleton, result.extract());
    }

    @Test
    public void insertSingleton_getSingleton_WhenRegisteredTypeAsNotSingleton_ThenNoInstance()
    {
        // given
        ClassBasicInheritsFromAbstract singleton = new ClassBasicInheritsFromAbstract();

        testObject.insert(ClassBasicAbstract.class, ClassBasicInheritsFromAbstract.class,
                          ConstructionPolicy.CONSTRUCT);
        // when
        testObject.insertSingleton(ClassBasicAbstract.class, singleton);

        Instance<ClassBasicAbstract> result = testObject.getSingleton(ClassBasicAbstract.class);
        // then
        Assertions.assertFalse(result.exists());
    }

    @Test
    public void insertSingleton_getSingleton_WhenNotRegisteredType_ThenNoInstance()
    {
        // given
        ClassBasicInheritsFromAbstract singleton = new ClassBasicInheritsFromAbstract();
        // when
        testObject.insertSingleton(ClassBasicAbstract.class, singleton);

        Instance<ClassBasicAbstract> result = testObject.getSingleton(ClassBasicAbstract.class);
        // then
        Assertions.assertFalse(result.exists());
    }

    // endregion
}
