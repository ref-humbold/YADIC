package com.github.refhumbold.yadic;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.github.refhumbold.yadic.newer.models.dependencies.linear.ClassLinear;
import com.github.refhumbold.yadic.newer.models.dependencies.linear.ClassLinearFirst;
import com.github.refhumbold.yadic.newer.models.dependencies.linear.ClassLinearSecond;
import com.github.refhumbold.yadic.newer.models.dependencies.linear.ClassLinearThird;
import com.github.refhumbold.yadic.newer.models.inheritance.ClassAbstract;
import com.github.refhumbold.yadic.newer.models.inheritance.ClassConcrete;
import com.github.refhumbold.yadic.newer.models.inheritance.InterfaceInheritance;
import com.github.refhumbold.yadic.registry.exception.AbstractTypeException;

public class YadicContainerTest_Newer
{
    private YadicContainer testObject;

    @BeforeEach
    public void setUp()
    {
        testObject = new YadicContainer();
    }

    @AfterEach
    public void tearDown()
    {
        testObject = null;
    }

    // region registerType & registerType/resolve [single class]

    @Test
    public void registerType_resolve_WhenTypeWithConstructionPolicy_ThenDifferentInstances()
    {
        // when
        testObject.registerType(ClassConcrete.class, ConstructionPolicy.CONSTRUCTION);

        ClassConcrete result1 = testObject.resolve(ClassConcrete.class);
        ClassConcrete result2 = testObject.resolve(ClassConcrete.class);

        // then
        Assertions.assertThat(result1).isNotNull();
        Assertions.assertThat(result2).isNotNull().isNotSameAs(result1);
    }

    @Test
    public void registerType_resolve_WhenTypeWithSingletonPolicy_ThenSameInstance()
    {
        // when
        testObject.registerType(ClassConcrete.class, ConstructionPolicy.SINGLETON);

        ClassConcrete result1 = testObject.resolve(ClassConcrete.class);
        ClassConcrete result2 = testObject.resolve(ClassConcrete.class);

        // then
        Assertions.assertThat(result1).isNotNull();
        Assertions.assertThat(result2).isNotNull().isSameAs(result1);
    }

    @Test
    public void registerType_resolve_WhenTypeRegisteredWithDifferentPolicy_ThenChangesInstances()
    {
        // when 1
        testObject.registerType(ClassConcrete.class, ConstructionPolicy.SINGLETON);

        ClassConcrete result11 = testObject.resolve(ClassConcrete.class);
        ClassConcrete result12 = testObject.resolve(ClassConcrete.class);

        // then 1
        Assertions.assertThat(result11).isNotNull();
        Assertions.assertThat(result12).isNotNull().isSameAs(result11);

        // when
        testObject.registerType(ClassConcrete.class, ConstructionPolicy.CONSTRUCTION);

        ClassConcrete result21 = testObject.resolve(ClassConcrete.class);
        ClassConcrete result22 = testObject.resolve(ClassConcrete.class);

        // then 2
        Assertions.assertThat(result21).isNotNull();
        Assertions.assertThat(result22).isNotNull().isNotSameAs(result21);
    }

    @Test
    public void registerType_WhenTypeIsInterface_ThenAbstractTypeException()
    {
        Assertions.assertThatThrownBy(() -> testObject.registerType(InterfaceInheritance.class,
                ConstructionPolicy.CONSTRUCTION)).isInstanceOf(AbstractTypeException.class);
    }

    @Test
    public void registerType_WhenTypeIsAbstractClass_ThenAbstractTypeException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.registerType(ClassAbstract.class, ConstructionPolicy.CONSTRUCTION))
                  .isInstanceOf(AbstractTypeException.class);
    }

    @Test
    public void registerType_resolve_WhenTypeDependencyWithVariousPolicies_ThenResolveWithPolicies()
    {
        // when
        testObject.registerType(ClassLinear.class, ConstructionPolicy.CONSTRUCTION);
        testObject.registerType(ClassLinearFirst.class, ConstructionPolicy.SINGLETON);
        testObject.registerType(ClassLinearSecond.class, ConstructionPolicy.CONSTRUCTION);
        testObject.registerType(ClassLinearThird.class, ConstructionPolicy.SINGLETON);

        ClassLinear result1 = testObject.resolve(ClassLinear.class);
        ClassLinear result2 = testObject.resolve(ClassLinear.class);

        // then
        Assertions.assertThat(result1).isNotNull();
        Assertions.assertThat(result1.getFirst()).isNotNull();
        Assertions.assertThat(result1.getFirst().getSecond()).isNotNull();
        Assertions.assertThat(result1.getFirst().getSecond().getThird()).isNotNull();
        Assertions.assertThat(result2).isNotNull().isNotSameAs(result1);
        Assertions.assertThat(result2.getFirst()).isNotNull().isSameAs(result1.getFirst());
        Assertions.assertThat(result2.getFirst().getSecond())
                  .isNotNull()
                  .isSameAs(result1.getFirst().getSecond());
        Assertions.assertThat(result2.getFirst().getSecond().getThird())
                  .isNotNull()
                  .isSameAs(result1.getFirst().getSecond().getThird());
    }

    // endregion
}
