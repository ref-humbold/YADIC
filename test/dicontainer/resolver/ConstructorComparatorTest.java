package dicontainer.resolver;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dicontainer.annotation.Dependency;
import dicontainer.auxiliary.comparator.ClassComparatorConstructor;

class ConstructorComparatorTest
{
    private ConstructorComparator testObject;

    @BeforeEach
    public void setUp()
    {
        testObject = new ConstructorComparator();
    }

    @AfterEach
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void compare_WhenSorting_ThenFirstDependencyAndNextByParametersDescending()
    {
        // given
        Constructor<?>[] constructors = ClassComparatorConstructor.class.getConstructors();
        // when
        Arrays.sort(constructors, testObject);
        // then
        Assertions.assertTrue(constructors[0].isAnnotationPresent(Dependency.class));
        Assertions.assertEquals(3, constructors[0].getParameterCount());
        Assertions.assertEquals(5, constructors[1].getParameterCount());
        Assertions.assertEquals(4, constructors[2].getParameterCount());
        Assertions.assertEquals(2, constructors[3].getParameterCount());
        Assertions.assertEquals(1, constructors[4].getParameterCount());
        Assertions.assertEquals(0, constructors[5].getParameterCount());
    }

    @Test
    public void compare_WhenDependencyAnnotated_ThenLess()
    {
        // given
        Constructor<?>[] constructors = ClassComparatorConstructor.class.getConstructors();
        Arrays.sort(constructors,
                    (c1, c2) -> Integer.compare(c1.getParameterCount(), c2.getParameterCount()));
        // when
        int result = testObject.compare(constructors[3], constructors[4]);
        // then
        Assertions.assertEquals(-1, result);
    }

    @Test
    public void compare_WhenNotDependencyAnnotated_ThenByParametersCountDescending()
    {
        // given
        Constructor<?>[] constructors = ClassComparatorConstructor.class.getConstructors();
        Arrays.sort(constructors,
                    (c1, c2) -> Integer.compare(c1.getParameterCount(), c2.getParameterCount()));
        // when
        int result = testObject.compare(constructors[2], constructors[5]);
        // then
        Assertions.assertEquals(1, result);
    }
}
