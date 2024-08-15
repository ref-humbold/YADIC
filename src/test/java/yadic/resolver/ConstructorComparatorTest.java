package yadic.resolver;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Comparator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import yadic.annotation.Dependency;
import yadic.models.comparator.ClassComparatorConstructor;

public class ConstructorComparatorTest
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
        Assertions.assertThat(constructors[0].isAnnotationPresent(Dependency.class)).isTrue();
        Assertions.assertThat(
                          Arrays.stream(constructors).map(Constructor::getParameterCount).toList())
                  .containsExactly(3, 5, 4, 2, 1, 0);
    }

    @Test
    public void compare_WhenDependencyAnnotated_ThenLess()
    {
        // given
        Constructor<?>[] constructors = ClassComparatorConstructor.class.getConstructors();
        Arrays.sort(constructors, Comparator.comparingInt(Constructor::getParameterCount));

        // when
        int result = testObject.compare(constructors[3], constructors[4]);

        // then
        Assertions.assertThat(result).isEqualTo(-1);
    }

    @Test
    public void compare_WhenNotDependencyAnnotated_ThenByParametersCountDescending()
    {
        // given
        Constructor<?>[] constructors = ClassComparatorConstructor.class.getConstructors();
        Arrays.sort(constructors, Comparator.comparingInt(Constructor::getParameterCount));

        // when
        int result = testObject.compare(constructors[2], constructors[5]);

        // then
        Assertions.assertThat(result).isEqualTo(1);
    }
}
