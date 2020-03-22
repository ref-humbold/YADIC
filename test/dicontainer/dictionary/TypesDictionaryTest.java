package dicontainer.dictionary;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dicontainer.ConstructionPolicy;
import dicontainer.auxiliary.basic.ClassBasicAbstract;
import dicontainer.auxiliary.basic.ClassBasicInheritsFromAbstract;
import dicontainer.auxiliary.basic.InterfaceBasic;
import dicontainer.auxiliary.register.ClassRegisterConcrete;
import dicontainer.auxiliary.register.ClassRegisterDerivedFromRegister;
import dicontainer.auxiliary.register.ClassRegisterDerivedFromSelfRegister;
import dicontainer.auxiliary.register.ClassRegisterSelf;

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

    @Test
    public void insert_get_WhenSpecifyTypeWithSubtype_ThenSubtypeInserted()
    {
        // given
        Class<InterfaceBasic> type = InterfaceBasic.class;
        Class<ClassBasicAbstract> subtype = ClassBasicAbstract.class;
        // when
        testObject.insert(type, subtype);
        SubtypeMapping<? extends InterfaceBasic> result = testObject.get(type);
        // then
        Assertions.assertEquals(subtype, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.getDefault(), result.policy);
        Assertions.assertFalse(result.isFromAnnotation);
    }

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
        Assertions.assertFalse(result.isFromAnnotation);
    }

    @Test
    public void insert_get_WhenSpecifyType_ThenThisTypeInserted()
    {
        // given
        Class<ClassBasicInheritsFromAbstract> type = ClassBasicInheritsFromAbstract.class;
        // when
        testObject.insert(type);
        SubtypeMapping<? extends ClassBasicInheritsFromAbstract> result = testObject.get(type);
        // then
        Assertions.assertEquals(type, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.getDefault(), result.policy);
        Assertions.assertFalse(result.isFromAnnotation);
    }

    @Test
    public void insert_get_WhenTypeHasSelfRegisterAnnotation_ThenThisTypeInserted()
    {
        // given
        Class<ClassRegisterSelf> type = ClassRegisterSelf.class;
        // when
        testObject.insert(type);
        SubtypeMapping<? extends ClassRegisterSelf> result = testObject.get(type);
        // then
        Assertions.assertEquals(type, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.getDefault(), result.policy);
        Assertions.assertTrue(result.isFromAnnotation);
    }

    @Test
    public void insert_get_WhenTypeHasRegisterAnnotation_ThenSubtypeInserted()
    {
        // given
        Class<ClassRegisterConcrete> type = ClassRegisterConcrete.class;
        // when
        testObject.insert(type);
        SubtypeMapping<? extends ClassRegisterConcrete> result = testObject.get(type);
        // then
        Assertions.assertEquals(ClassRegisterDerivedFromRegister.class, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.getDefault(), result.policy);
        Assertions.assertTrue(result.isFromAnnotation);
    }

    @Test
    public void insert_WhenRegisterAnnotatedTypeAndSubtype_ThenAlreadyAnnotatedException()
    {
        Assertions.assertThrows(AlreadyAnnotatedException.class,
                                () -> testObject.insert(ClassRegisterConcrete.class,
                                                        ClassRegisterDerivedFromRegister.class));
    }

    @Test
    public void insert_WhenSelfRegisterAnnotatedTypeAndSubtype_ThenAlreadyAnnotatedException()
    {
        Assertions.assertThrows(AlreadyAnnotatedException.class,
                                () -> testObject.insert(ClassRegisterSelf.class,
                                                        ClassRegisterDerivedFromSelfRegister.class));
    }
}
