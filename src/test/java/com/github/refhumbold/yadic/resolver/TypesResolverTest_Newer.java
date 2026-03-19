package com.github.refhumbold.yadic.resolver;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import com.github.refhumbold.yadic.ConstructionPolicy;
import com.github.refhumbold.yadic.newer.models.annotations.register.*;
import com.github.refhumbold.yadic.newer.models.annotations.registerself.ClassAbstractRegisterSelf;
import com.github.refhumbold.yadic.newer.models.annotations.registerself.ClassRegisterSelf;
import com.github.refhumbold.yadic.newer.models.annotations.registerself.InterfaceRegisterSelf;
import com.github.refhumbold.yadic.newer.models.constructors.*;
import com.github.refhumbold.yadic.newer.models.dependencies.circular.*;
import com.github.refhumbold.yadic.newer.models.dependencies.diamond.*;
import com.github.refhumbold.yadic.newer.models.dependencies.linear.*;
import com.github.refhumbold.yadic.newer.models.inheritance.ClassAbstract;
import com.github.refhumbold.yadic.newer.models.inheritance.ClassConcrete;
import com.github.refhumbold.yadic.newer.models.inheritance.InterfaceInheritance;
import com.github.refhumbold.yadic.registry.DependencyRegistry;
import com.github.refhumbold.yadic.registry.exception.AbstractTypeException;
import com.github.refhumbold.yadic.resolver.exception.CircularDependenciesException;
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

    // region resolve [constructor]

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

    // endregion
    // region resolve [annotations]

    @Test
    public void resolve_WhenAnnotatedInterface_ThenInstanceIsResolved()
    {
        // when
        InterfaceRegister result1 = testObject.resolve(InterfaceRegister.class);
        InterfaceRegister result2 = testObject.resolve(InterfaceRegister.class);

        // then
        Assertions.assertThat(result1)
                  .isNotNull()
                  .isExactlyInstanceOf(ClassDerivedFromInterfaceRegister.class);
        Assertions.assertThat(result2)
                  .isNotNull()
                  .isExactlyInstanceOf(ClassDerivedFromInterfaceRegister.class)
                  .isNotSameAs(result1);
    }

    @Test
    public void resolve_WhenAnnotatedAbstractClass_ThenInstanceIsResolved()
    {
        // when
        ClassAbstractRegister result1 = testObject.resolve(ClassAbstractRegister.class);
        ClassAbstractRegister result2 = testObject.resolve(ClassAbstractRegister.class);

        // then
        Assertions.assertThat(result1)
                  .isNotNull()
                  .isExactlyInstanceOf(ClassDerivedFromAbstractRegister.class);
        Assertions.assertThat(result2)
                  .isNotNull()
                  .isExactlyInstanceOf(ClassDerivedFromAbstractRegister.class)
                  .isNotSameAs(result1);
    }

    @Test
    public void resolve_WhenAnnotatedConcreteClass_ThenInstanceIsResolved()
    {
        // when
        ClassRegister result1 = testObject.resolve(ClassRegister.class);
        ClassRegister result2 = testObject.resolve(ClassRegister.class);

        // then
        Assertions.assertThat(result1)
                  .isNotNull()
                  .isExactlyInstanceOf(ClassDerivedFromRegister.class);
        Assertions.assertThat(result2)
                  .isNotNull()
                  .isExactlyInstanceOf(ClassDerivedFromRegister.class)
                  .isNotSameAs(result1);
    }

    @Test
    public void resolve_WhenSelfAnnotatedInterface_ThenAbstractTypeException()
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(InterfaceRegisterSelf.class))
                  .isInstanceOf(AbstractTypeException.class);
    }

    @Test
    public void resolve_WhenSelfAnnotatedAbstractClass_ThenAbstractTypeException()
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(ClassAbstractRegisterSelf.class))
                  .isInstanceOf(AbstractTypeException.class);
    }

    @Test
    public void resolve_WhenSelfAnnotatedConcreteClass_ThenInstanceIsResolved()
    {
        // when
        ClassRegisterSelf result1 = testObject.resolve(ClassRegisterSelf.class);
        ClassRegisterSelf result2 = testObject.resolve(ClassRegisterSelf.class);

        // then
        Assertions.assertThat(result1).isNotNull().isExactlyInstanceOf(ClassRegisterSelf.class);
        Assertions.assertThat(result2)
                  .isNotNull()
                  .isExactlyInstanceOf(ClassRegisterSelf.class)
                  .isNotSameAs(result1);
    }

    // endregion
    // region resolve [setter]

    // endregion
    // region resolve [dependencies schemas]

    @Test
    public void resolve_WhenLinear_ThenInstanceIsResolved()
    {
        // given
        dictionary.addType(InterfaceLinear.class, ClassLinear.class,
                ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceLinearFirst.class, ClassLinearFirst.class,
                ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceLinearSecond.class, ClassLinearSecond.class,
                ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceLinearThird.class, ClassLinearThird.class,
                ConstructionPolicy.CONSTRUCTION);

        // when
        InterfaceLinear result = testObject.resolve(InterfaceLinear.class);

        // then
        Assertions.assertThat(result).isNotNull().isExactlyInstanceOf(ClassLinear.class);
        Assertions.assertThat(result.getFirst())
                  .isNotNull()
                  .isExactlyInstanceOf(ClassLinearFirst.class);
        Assertions.assertThat(result.getFirst().getSecond())
                  .isNotNull()
                  .isExactlyInstanceOf(ClassLinearSecond.class);
        Assertions.assertThat(result.getFirst().getSecond().getThird())
                  .isNotNull()
                  .isExactlyInstanceOf(ClassLinearThird.class);
    }

    @Test
    public void resolve_WhenCircular_ThenCircularDependenciesException()
    {
        // given
        dictionary.addType(InterfaceCircular.class, ClassCircular.class,
                ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceCircularLeft.class, ClassCircularLeft.class,
                ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceCircularRight.class, ClassCircularRight.class,
                ConstructionPolicy.CONSTRUCTION);

        // then
        Assertions.assertThatThrownBy(() -> testObject.resolve(InterfaceCircular.class))
                  .isInstanceOf(CircularDependenciesException.class);
    }

    @Test
    public void resolve_WhenDiamondWithoutSingleton_ThenInstanceIsResolved()
    {
        // given
        dictionary.addType(InterfaceDiamond.class, ClassDiamond.class,
                ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceDiamondRight.class, ClassDiamondRight.class,
                ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(InterfaceDiamondTop.class, ClassDiamondTop.class,
                ConstructionPolicy.CONSTRUCTION);

        // when
        InterfaceDiamond result = testObject.resolve(InterfaceDiamond.class);

        // then
        Assertions.assertThat(result).isNotNull().isExactlyInstanceOf(ClassDiamond.class);
        Assertions.assertThat(result.getLeft())
                  .isNotNull()
                  .isExactlyInstanceOf(ClassDiamondLeft.class);
        Assertions.assertThat(result.getRight())
                  .isNotNull()
                  .isExactlyInstanceOf(ClassDiamondRight.class);
        Assertions.assertThat(result.getLeft().getTop())
                  .isNotNull()
                  .isExactlyInstanceOf(ClassDiamondTop.class);
        Assertions.assertThat(result.getRight().getTop())
                  .isNotNull()
                  .isExactlyInstanceOf(ClassDiamondTop.class)
                  .isNotSameAs(result.getLeft().getTop());
    }

    @Test
    public void resolve_WhenDiamondWithSingleton_ThenInstanceIsResolved()
    {
        // given
        dictionary.addType(InterfaceDiamond.class, ClassDiamond.class,
                ConstructionPolicy.SINGLETON);
        dictionary.addType(InterfaceDiamondLeft.class, ClassDiamondLeft.class,
                ConstructionPolicy.SINGLETON);
        dictionary.addType(InterfaceDiamondRight.class, ClassDiamondRight.class,
                ConstructionPolicy.SINGLETON);
        dictionary.addType(InterfaceDiamondTop.class, ClassDiamondTop.class,
                ConstructionPolicy.SINGLETON);

        // when
        InterfaceDiamond result = testObject.resolve(InterfaceDiamond.class);

        // then
        Assertions.assertThat(result).isNotNull().isExactlyInstanceOf(ClassDiamond.class);
        Assertions.assertThat(result.getLeft())
                  .isNotNull()
                  .isExactlyInstanceOf(ClassDiamondLeft.class);
        Assertions.assertThat(result.getRight())
                  .isNotNull()
                  .isExactlyInstanceOf(ClassDiamondRight.class);
        Assertions.assertThat(result.getLeft().getTop())
                  .isNotNull()
                  .isExactlyInstanceOf(ClassDiamondTop.class);
        Assertions.assertThat(result.getRight().getTop())
                  .isNotNull()
                  .isExactlyInstanceOf(ClassDiamondTop.class)
                  .isSameAs(result.getLeft().getTop());
    }

    // endregion
}
