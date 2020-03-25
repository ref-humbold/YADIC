package dicontainer.dictionary;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import dicontainer.auxiliary.basic.ClassBasicStringGetter;
import dicontainer.auxiliary.constructor.ClassConstructorDefault;
import dicontainer.auxiliary.register.ClassRegisterConcrete;

class InstancesDictionaryTest
{
    private InstancesDictionary testObject;

    @BeforeEach
    public void setUp()
    {
        testObject = new InstancesDictionary();
    }

    @AfterEach
    public void tearDown()
    {
        testObject = null;
    }

    // region insert/get

    @Test
    public void insert_get_WhenAbsent_ThenCreatesNewMapping()
    {
        // given
        ClassConstructorDefault instance = new ClassConstructorDefault();
        // when
        testObject.insert(ClassConstructorDefault.class, instance);
        InstanceMapping<ClassConstructorDefault> result =
                testObject.get(ClassConstructorDefault.class);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertSame(instance, result.instance);
    }

    @Test
    public void insert_get_WhenInstancePresent_ThenMappingUpdated()
    {
        // given
        ClassConstructorDefault oldInstance = new ClassConstructorDefault();
        ClassConstructorDefault newInstance = new ClassConstructorDefault();
        testObject.insert(ClassConstructorDefault.class, oldInstance);
        // when
        testObject.insert(ClassConstructorDefault.class, newInstance);
        InstanceMapping<ClassConstructorDefault> result =
                testObject.get(ClassConstructorDefault.class);
        // then
        Assertions.assertNotNull(result);
        Assertions.assertSame(newInstance, result.instance);
        Assertions.assertNotSame(oldInstance, result.instance);
    }

    // endregion
    // region insert

    @Test
    public void insert_WhenInstanceNull_ThenNullPointerException()
    {
        // when
        Executable executable = () -> testObject.insert(ClassBasicStringGetter.class, null);
        // then
        Assertions.assertThrows(NullPointerException.class, executable);
    }

    // endregion
    // region get

    @Test
    public void get_WhenInstanceAbsent_ThenNull()
    {
        // when
        InstanceMapping<ClassBasicStringGetter> result =
                testObject.get(ClassBasicStringGetter.class);
        // then
        Assertions.assertNull(result);
    }

    // endregion
    // region remove

    @Test
    public void remove_WhenInstancePresent_ThenRemoved()
    {
        // given
        testObject.insert(ClassBasicStringGetter.class, new ClassBasicStringGetter("X"));
        // when
        testObject.remove(ClassBasicStringGetter.class);
        // then
        Assertions.assertNull(testObject.get(ClassBasicStringGetter.class));
    }

    @Test
    public void remove_WhenInstanceAbsent_ThenNull()
    {
        // when
        testObject.remove(ClassRegisterConcrete.class);
        // then
        Assertions.assertNull(testObject.get(ClassRegisterConcrete.class));
    }

    // endregion
}
