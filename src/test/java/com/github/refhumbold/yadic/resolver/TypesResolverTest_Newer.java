package com.github.refhumbold.yadic.resolver;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import com.github.refhumbold.yadic.newer.models.constructors.*;
import com.github.refhumbold.yadic.newer.models.inheritance.ClassAbstract;
import com.github.refhumbold.yadic.newer.models.inheritance.ClassConcrete;
import com.github.refhumbold.yadic.newer.models.inheritance.InterfaceInheritance;
import com.github.refhumbold.yadic.registry.DependencyRegistry;
import com.github.refhumbold.yadic.resolver.exception.MissingDependenciesException;
import com.github.refhumbold.yadic.resolver.exception.NoInstanceCreatedException;
import com.github.refhumbold.yadic.resolver.exception.NoSuitableConstructorException;

public class TypesResolverTest_Newer
{
    private DependencyRegistry dictionary;
    private TypesResolver testObject;

    @BeforeEach
    public void setUp()
    {
        dictionary = new DependencyRegistry();
        testObject = new TypesResolver(dictionary);
    }

    @AfterEach
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void resolve_WhenClassHasDefaultConstructorOnly_ThenInstanceIsResolved()
    {
        // when
        ClassDefaultConstructorOnly result = testObject.resolve(ClassDefaultConstructorOnly.class);

        // then
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void resolve_WhenClassInheritsFromAbstractClass_ThenInstanceIsResolved()
    {
        // when
        ClassConcrete result = testObject.resolve(ClassConcrete.class);

        // then
        Assertions.assertThat(result).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(classes = { ClassParameterizedConstructorString.class,
            ClassParameterizedConstructorPrimitive.class,
            ClassParameterizedConstructorBoxed.class })
    public void resolve_WhenClassHasParameterConstructorWithoutAddedParameter_ThenMissingDependenciesException(
            Class<?> cls)
    {
        // then
        Assertions.assertThatThrownBy(() -> testObject.resolve(cls))
                  .isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void resolve_WhenClassHasPrimitiveParameterConstructorWithAddedParameter_ThenInstanceIsResolved()
    {
        // given
        int number = 10;

        dictionary.addInstance(int.class, number);

        // when
        ClassParameterizedConstructorPrimitive result =
                testObject.resolve(ClassParameterizedConstructorPrimitive.class);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getNumber()).isEqualTo(number);
    }

    @Test
    public void resolve_WhenClassHasReferenceParameterConstructorWithAddedParameter_ThenInstanceIsResolved()
    {
        // given
        Integer number = 10;

        dictionary.addInstance(Integer.class, number);

        // when
        ClassParameterizedConstructorBoxed result =
                testObject.resolve(ClassParameterizedConstructorBoxed.class);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getNumber()).isEqualTo(number);
    }

    @Test
    public void resolve_WhenClassHasPrimitiveParameterConstructorButAddedBoxedParameter_ThenMissingDependenciesException()
    {
        // given
        Integer number = 10;

        dictionary.addInstance(Integer.class, number);

        // then
        Assertions.assertThatThrownBy(
                          () -> testObject.resolve(ClassParameterizedConstructorPrimitive.class))
                  .isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void resolve_WhenClassHasBoxedParameterConstructorButAddedPrimitiveParameter_ThenMissingDependenciesException()
    {
        // given
        int number = 10;

        dictionary.addInstance(int.class, number);

        // then
        Assertions.assertThatThrownBy(
                          () -> testObject.resolve(ClassParameterizedConstructorBoxed.class))
                  .isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void resolve_WhenInterface_ThenMissingDependenciesException()
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(InterfaceInheritance.class))
                  .isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void resolve_WhenAbstractClass_ThenMissingDependenciesException()
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(ClassAbstract.class))
                  .isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void resolve_WhenClassConstructorThrowsException_ThenNoInstanceCreatedException()
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(ClassConstructorThrows.class))
                  .isInstanceOf(NoInstanceCreatedException.class);
    }

    @Test
    public void resolve_WhenPrimitiveType_ThenNoSuitableConstructorException()
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(double.class))
                  .isInstanceOf(NoSuitableConstructorException.class);
    }

    @Test
    public void resolve_WhenNoPublicConstructors_ThenNoSuitableConstructorException()
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(ClassPrivateConstructorOnly.class))
                  .isInstanceOf(NoSuitableConstructorException.class);
    }
}
