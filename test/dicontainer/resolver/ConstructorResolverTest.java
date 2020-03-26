package dicontainer.resolver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dicontainer.auxiliary.register.ClassRegisterSelf;
import dicontainer.dictionary.InstancesDictionary;
import dicontainer.dictionary.TypesDictionary;

class ConstructorResolverTest
{
    private TypesDictionary typesDictionary;
    private InstancesDictionary instancesDictionary;
    private ConstructorResolver testObject;

    @BeforeEach
    public void setUp()
    {
        typesDictionary = new TypesDictionary();
        instancesDictionary = new InstancesDictionary();
        testObject = new ConstructorResolver(typesDictionary, instancesDictionary);
    }

    @AfterEach
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void resolve_WhenSelfRegister_ThenThisClassInstance()
    {
        // given
        Class<ClassRegisterSelf> type = ClassRegisterSelf.class;
        typesDictionary.insert(type);
        // when
        ClassRegisterSelf result = testObject.resolve(type);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(type, result.getClass());
    }
}
