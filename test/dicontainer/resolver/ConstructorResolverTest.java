package dicontainer.resolver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dicontainer.auxiliary.basic.ClassBasicAbstract;
import dicontainer.auxiliary.basic.ClassBasicInheritsFromAbstract;
import dicontainer.auxiliary.basic.ClassBasicStringGetter;
import dicontainer.auxiliary.basic.InterfaceBasic;
import dicontainer.auxiliary.basic.InterfaceBasicStringGetter;
import dicontainer.auxiliary.register.ClassRegisterSelf;
import dicontainer.dictionary.RegistrationDictionary;

class ConstructorResolverTest
{
    private RegistrationDictionary registrationDictionary;
    private ConstructorResolver testObject;

    @BeforeEach
    public void setUp()
    {
        registrationDictionary = new RegistrationDictionary();
        testObject = new ConstructorResolver(registrationDictionary);
    }

    @AfterEach
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void resolve_WhenInterface_ThenConcreteClass()
    {
        // given
        registrationDictionary.insertType(InterfaceBasic.class, ClassBasicAbstract.class);
        registrationDictionary.insertType(ClassBasicAbstract.class,
                                          ClassBasicInheritsFromAbstract.class);
        // when
        InterfaceBasic result = testObject.resolve(InterfaceBasic.class);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(ClassBasicInheritsFromAbstract.class, result.getClass());
    }

    @Test
    public void resolve_WhenAbstractClass_ThenConcreteClass()
    {
        // given
        registrationDictionary.insertType(ClassBasicAbstract.class,
                                          ClassBasicInheritsFromAbstract.class);
        // when
        ClassBasicAbstract result = testObject.resolve(ClassBasicAbstract.class);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(ClassBasicInheritsFromAbstract.class, result.getClass());
    }

    @Test
    public void resolve_WhenNoDependency_ThenUsesDefaultConstructor()
    {
        // given
        registrationDictionary.insertType(InterfaceBasicStringGetter.class,
                                          ClassBasicStringGetter.class);
        // when
        InterfaceBasicStringGetter result = testObject.resolve(InterfaceBasicStringGetter.class);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(ClassBasicStringGetter.class, result.getClass());
        Assertions.assertEquals("", result.getString());
    }

    @Test
    public void resolve_WhenDependencyExists_ThenUsesParameterizedConstructor()
    {
        // given
        String value = "String";

        registrationDictionary.insertInstance(String.class, value);
        registrationDictionary.insertType(InterfaceBasicStringGetter.class,
                                          ClassBasicStringGetter.class);
        // when
        InterfaceBasicStringGetter result = testObject.resolve(InterfaceBasicStringGetter.class);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(ClassBasicStringGetter.class, result.getClass());
        Assertions.assertEquals(value, result.getString());
    }

    @Test
    public void resolve_WhenSelfRegister_ThenThisClassInstance()
    {
        // given
        Class<ClassRegisterSelf> type = ClassRegisterSelf.class;
        registrationDictionary.insertType(type);
        // when
        ClassRegisterSelf result = testObject.resolve(type);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(type, result.getClass());
    }
}
