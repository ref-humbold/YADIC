package com.github.refhumbold.yadic.resolver;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.github.refhumbold.yadic.annotation.YadicDependency;
import com.github.refhumbold.yadic.models.constructors.comparator.ClassAnnotatedConstructorsSorting;
import com.github.refhumbold.yadic.models.constructors.comparator.ClassConstructorsSorting;

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

    // region without annotation

    @Test
    public void compare_WhenSortingWithoutAnnotation_ThenOrderedByParametersCountDescending()
    {
        // given
        Constructor<?>[] constructors = ClassConstructorsSorting.class.getConstructors();

        // when
        Arrays.sort(constructors, testObject);

        // then
        Assertions.assertThat(
                          Arrays.stream(constructors).map(Constructor::getParameterCount).toList())
                  .isSortedAccordingTo(Comparator.reverseOrder())
                  .containsExactly(5, 4, 3, 2, 1, 0);
    }

    @Test
    public void compare_WhenNotDependencyAnnotated_ThenReverseParametersCountOrder()
    {
        // given
        List<Constructor<?>> constructors =
                Arrays.stream(ClassConstructorsSorting.class.getConstructors())
                      .sorted(Comparator.comparingInt(Constructor::getParameterCount))
                      .toList();

        for(int i = 0; i <= 5; ++i)
        {
            for(int j = i + 1; j <= 5; ++j)
            {
                // when
                int result = testObject.compare(constructors.get(i), constructors.get(j));

                // then
                Assertions.assertThat(result).isEqualTo(1);
            }
        }
    }

    // endregion
    // region with annotation

    @Test
    public void compare_WhenSortingWithAnnotation_ThenAnnotatedFirstThenByParametersCount()
    {
        // given
        Constructor<?>[] constructors = ClassAnnotatedConstructorsSorting.class.getConstructors();

        // when
        Arrays.sort(constructors, testObject);

        // then
        Assertions.assertThat(constructors[0].isAnnotationPresent(YadicDependency.class)).isTrue();
        Assertions.assertThat(
                          Arrays.stream(constructors).map(Constructor::getParameterCount).toList())
                  .containsExactly(3, 5, 4, 2, 1, 0);
    }

    @Test
    public void compare_WhenDependencyAnnotated_ThenLessThanAnyOtherConstructor()
    {
        // given
        Constructor<?> annotated =
                Arrays.stream(ClassAnnotatedConstructorsSorting.class.getConstructors())
                      .filter(constructor -> constructor.isAnnotationPresent(YadicDependency.class))
                      .findFirst()
                      .orElseThrow();
        List<Constructor<?>> otherConstructors =
                Arrays.stream(ClassAnnotatedConstructorsSorting.class.getConstructors())
                      .filter(constructor -> !constructor.isAnnotationPresent(
                              YadicDependency.class))
                      .sorted(Comparator.comparingInt(Constructor::getParameterCount))
                      .toList();

        for(Constructor<?> constructor : otherConstructors)
        {
            // when
            int result = testObject.compare(annotated, constructor);

            // then
            Assertions.assertThat(result).isEqualTo(-1);
        }
    }

    // endregion
}
