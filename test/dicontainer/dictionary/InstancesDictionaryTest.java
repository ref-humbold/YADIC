package dicontainer.dictionary;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dicontainer.auxiliary.basic.ClassBasicStringGetter;
import dicontainer.commons.Instance;

public class InstancesDictionaryTest
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
    public void insert_getSingleton_WhenInstancePresent_ThenMapping()
    {
        // given
        String string = "String";
        ClassBasicStringGetter instance = new ClassBasicStringGetter(string);
        // when
        testObject.insert(ClassBasicStringGetter.class, instance);
        Instance<ClassBasicStringGetter> result = testObject.get(ClassBasicStringGetter.class);
        // then
        Assertions.assertTrue(result.exists());
        Assertions.assertSame(instance, result.extract());
        Assertions.assertEquals(string, result.extract().getString());
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
    // region contains

    @Test
    public void contains_WhenInstancePresent_ThenTrue()
    {
        // given
        String string = "String";
        ClassBasicStringGetter instance = new ClassBasicStringGetter(string);
        testObject.insert(ClassBasicStringGetter.class, instance);
        // when
        boolean result = testObject.contains(ClassBasicStringGetter.class);
        // then
        Assertions.assertTrue(result);
    }

    @Test
    public void contains_WhenInstanceAbsent_ThenFalse()
    {
        // when
        boolean result = testObject.contains(ClassBasicStringGetter.class);
        // then
        Assertions.assertFalse(result);
    }

    // endregion
}
