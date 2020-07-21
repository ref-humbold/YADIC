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
import dicontainer.auxiliary.register.*;
import dicontainer.commons.Instance;
import dicontainer.exception.AbstractTypeException;
import dicontainer.exception.NotDerivedTypeException;
import dicontainer.exception.NullInstanceException;

public class DIDictionaryTest
{
    private DIDictionary testObject;

    @BeforeEach
    public void setUp()
    {
        testObject = new DIDictionary();
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

        SubtypeMapping<? extends InterfaceBasic> result = testObject.findType(type);
        // then
        Assertions.assertEquals(subtype, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.SINGLETON, result.policy);
    }

    @Test
    public void addType_findType_WhenSpecifyTypeAndPolicy_ThenThisTypeInserted()
    {
        // given
        Class<ClassBasicInheritsFromAbstract> type = ClassBasicInheritsFromAbstract.class;
        // when
        testObject.addType(type, ConstructionPolicy.SINGLETON);

        SubtypeMapping<? extends ClassBasicInheritsFromAbstract> result = testObject.findType(type);
        // then
        Assertions.assertEquals(type, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.SINGLETON, result.policy);
    }

    @Test
    public void addType_findType_WhenTypeHasSelfRegisterAnnotation_ThenThisTypeInserted()
    {
        // given
        Class<ClassRegisterSelf> type = ClassRegisterSelf.class;
        // when
        testObject.addType(type);

        SubtypeMapping<? extends ClassRegisterSelf> result = testObject.findType(type);
        // then
        Assertions.assertEquals(type, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.getDefault(), result.policy);
    }

    @Test
    public void addType_findType_WhenTypeHasRegisterAnnotation_ThenSubtypeInserted()
    {
        // given
        Class<ClassRegisterConcrete> type = ClassRegisterConcrete.class;
        // when
        testObject.addType(type);

        SubtypeMapping<? extends ClassRegisterConcrete> result = testObject.findType(type);
        // then
        Assertions.assertEquals(ClassRegisterDerivedFromRegister.class, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.getDefault(), result.policy);
    }

    // endregion
    // region addType

    @Test
    public void addType_WhenSingleInterface_ThenAbstractTypeException()
    {
        // when
        Executable executable = () -> testObject.addType(InterfaceBasic.class);
        // then
        Assertions.assertThrows(AbstractTypeException.class, executable);
    }

    @Test
    public void addType_WhenSingleAbstractClass_ThenAbstractTypeException()
    {
        // when
        Executable executable = () -> testObject.addType(ClassBasicAbstract.class);
        // then
        Assertions.assertThrows(AbstractTypeException.class, executable);
    }

    @Test
    public void addType_WhenRegisterAnnotatedTypeAndSubtype_ThenAnnotatedTypeRegistrationException()
    {
        // when
        Executable executable = () -> testObject.addType(ClassRegisterConcrete.class,
                                                         ClassRegisterDerivedFromRegister.class,
                                                         ConstructionPolicy.getDefault());
        // then
        Assertions.assertThrows(AnnotatedTypeRegistrationException.class, executable);
    }

    @Test
    public void addType_WhenSelfRegisterAnnotatedTypeAndSubtype_ThenAnnotatedTypeRegistrationException()
    {
        // when
        Executable executable = () -> testObject.addType(ClassRegisterSelf.class,
                                                         ClassRegisterDerivedFromSelfRegister.class,
                                                         ConstructionPolicy.getDefault());
        // then
        Assertions.assertThrows(AnnotatedTypeRegistrationException.class, executable);
    }

    @Test
    public void addType_WhenRegisterTypeAndNotSubtype_ThenNotDerivedTypeException()
    {
        // when
        Executable executable = () -> testObject.addType(ClassRegisterIncorrectOtherClass.class,
                                                         ConstructionPolicy.getDefault());
        // then
        Assertions.assertThrows(NotDerivedTypeException.class, executable);
    }

    @Test
    public void addType_WhenRegisterTypeAndAbstract_ThenAbstractTypeException()
    {
        // when
        Executable executable = () -> testObject.addType(ClassRegisterAbstractIncorrect.class,
                                                         ConstructionPolicy.getDefault());
        // then
        Assertions.assertThrows(AbstractTypeException.class, executable);
    }

    @Test
    public void addType_WhenSelfRegisterAbstract_ThenAbstractTypeException()
    {
        // when
        Executable executable = () -> testObject.addType(ClassRegisterSelfAbstractIncorrect.class,
                                                         ConstructionPolicy.getDefault());
        // then
        Assertions.assertThrows(AbstractTypeException.class, executable);
    }

    @Test
    public void addType_WhenRegisteredInstance_ThenRegistrationException()
    {
        // given
        String string = "String";
        Class<ClassBasicStringGetter> type = ClassBasicStringGetter.class;
        ClassBasicStringGetter instance = new ClassBasicStringGetter(string);

        testObject.addInstance(type, instance);
        // when
        Executable executable = () -> testObject.addType(type);
        // then
        Assertions.assertThrows(RegistrationException.class, executable);
    }

    @Test
    public void addType_WhenPrimitiveType_ThenRegistrationException()
    {
        // when
        Executable executable = () -> testObject.addType(boolean.class);
        // then
        Assertions.assertThrows(RegistrationException.class, executable);
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
        Assertions.assertTrue(result.exists());
        Assertions.assertSame(instance, result.extract());
        Assertions.assertEquals(string, result.extract().getString());
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
        // when
        Executable executable = () -> testObject.addInstance(type, instance);
        // then
        Assertions.assertThrows(RegistrationException.class, executable);
    }

    @Test
    public void addInstance_WhenAnnotatedType_ThenRegistrationException()
    {
        // when
        Executable executable = () -> testObject.addInstance(ClassRegisterConcrete.class,
                                                             new ClassRegisterConcrete());
        // then
        Assertions.assertThrows(RegistrationException.class, executable);
    }

    @Test
    public void addInstance_WhenNullInstance_ThenNullInstanceException()
    {
        // when
        Executable executable = () -> testObject.addInstance(ClassBasicAbstract.class, null);
        // then
        Assertions.assertThrows(NullInstanceException.class, executable);
    }

    // endregion
    // region findType

    @Test
    public void findType_WhenPrimitive_ThenFoundMapping()
    {
        // when
        SubtypeMapping<?> result = testObject.findType(double.class);
        // then
        Assertions.assertEquals(double.class, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.getDefault(), result.policy);
    }

    @Test
    public void findType_WhenAbstractClass_ThenFoundMapping()
    {
        // given
        testObject.addType(InterfaceBasic.class, ClassBasicAbstract.class,
                           ConstructionPolicy.getDefault());
        testObject.addType(ClassBasicAbstract.class, ClassBasicInheritsFromAbstract.class,
                           ConstructionPolicy.getDefault());
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
        testObject.addType(InterfaceBasic.class, ClassBasicAbstract.class,
                           ConstructionPolicy.CONSTRUCT);
        testObject.addType(ClassBasicAbstract.class, ClassBasicInheritsFromAbstract.class,
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

    @Test
    public void findType_whenRegisteredInstance_ThenMappingToThisType()
    {
        // given
        Class<ClassBasicInheritsFromAbstract> type = ClassBasicInheritsFromAbstract.class;

        testObject.addInstance(type, new ClassBasicInheritsFromAbstract());
        // when
        SubtypeMapping<? extends ClassBasicInheritsFromAbstract> result = testObject.findType(type);
        // then
        Assertions.assertEquals(type, result.subtype);
        Assertions.assertEquals(ConstructionPolicy.SINGLETON, result.policy);
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
        Assertions.assertFalse(result.exists());
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
        Assertions.assertTrue(result);
    }

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

        testObject.addType(type);
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

        testObject.addType(type, ClassBasicAbstract.class);
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
        Assertions.assertTrue(result.exists());
        Assertions.assertSame(singleton, result.extract());
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
        Assertions.assertFalse(result.exists());
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
        Assertions.assertFalse(result.exists());
    }

    // endregion
}
