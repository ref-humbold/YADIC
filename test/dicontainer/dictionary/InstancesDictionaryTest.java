package dicontainer.dictionary;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import dicontainer.auxiliary.basic.ClassBasicStringGetter;
import dicontainer.auxiliary.constructor.ClassConstructorDefault;
import dicontainer.commons.Instance;

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
        Instance<ClassConstructorDefault> result = testObject.get(ClassConstructorDefault.class);
        // then
        Assertions.assertTrue(result.exists());
        Assertions.assertSame(instance, result.extract());
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
        Instance<ClassConstructorDefault> result = testObject.get(ClassConstructorDefault.class);
        // then
        Assertions.assertTrue(result.exists());
        Assertions.assertSame(newInstance, result.extract());
        Assertions.assertNotSame(oldInstance, result.extract());
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
    public void get_WhenInstanceAbsent_ThenMappingNone()
    {
        // when
        Instance<ClassBasicStringGetter> result = testObject.get(ClassBasicStringGetter.class);
        // then
        Assertions.assertFalse(result.exists());
    }

    // endregion
}
