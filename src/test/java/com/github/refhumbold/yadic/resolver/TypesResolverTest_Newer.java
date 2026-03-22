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
import com.github.refhumbold.yadic.newer.models.dependencies.annotation.ClassDependencyAnnotation;
import com.github.refhumbold.yadic.newer.models.dependencies.annotation.ClassDependencyOptionalCircular;
import com.github.refhumbold.yadic.newer.models.dependencies.circular.ClassCircular;
import com.github.refhumbold.yadic.newer.models.dependencies.circular.ClassCircularLeft;
import com.github.refhumbold.yadic.newer.models.dependencies.circular.ClassCircularRight;
import com.github.refhumbold.yadic.newer.models.dependencies.diamond.ClassDiamond;
import com.github.refhumbold.yadic.newer.models.dependencies.diamond.ClassDiamondLeft;
import com.github.refhumbold.yadic.newer.models.dependencies.diamond.ClassDiamondRight;
import com.github.refhumbold.yadic.newer.models.dependencies.diamond.ClassDiamondTop;
import com.github.refhumbold.yadic.newer.models.dependencies.linear.ClassLinear;
import com.github.refhumbold.yadic.newer.models.dependencies.linear.ClassLinearFirst;
import com.github.refhumbold.yadic.newer.models.dependencies.linear.ClassLinearSecond;
import com.github.refhumbold.yadic.newer.models.dependencies.linear.ClassLinearThird;
import com.github.refhumbold.yadic.newer.models.inheritance.ClassAbstract;
import com.github.refhumbold.yadic.newer.models.inheritance.ClassConcrete;
import com.github.refhumbold.yadic.newer.models.inheritance.ClassConcreteDerived;
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

    @Test
    public void resolve_WhenPrimitiveType_ThenNoSuitableConstructorException()
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(double.class))
                  .isInstanceOf(NoSuitableConstructorException.class);
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
    public void resolve_WhenClassConstructorThrowsException_ThenNoInstanceCreatedException()
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(ClassConstructorThrows.class))
                  .isInstanceOf(NoInstanceCreatedException.class);
    }

    @Test
    public void resolve_WhenNoPublicConstructors_ThenNoSuitableConstructorException()
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(ClassPrivateConstructorOnly.class))
                  .isInstanceOf(NoSuitableConstructorException.class);
    }

    // endregion
    // region resolve [inheritance]

    @Test
    public void resolve_WhenInterfaceNotAdded_ThenMissingDependenciesException()
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(InterfaceInheritance.class))
                  .isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void resolve_WhenInterfaceAdded_ThenResolvedToSubtype()
    {
        // given
        dictionary.addType(InterfaceInheritance.class, ClassConcrete.class,
                ConstructionPolicy.CONSTRUCTION);

        // when
        InterfaceInheritance result = testObject.resolve(InterfaceInheritance.class);

        // then
        Assertions.assertThat(result).isNotNull().isExactlyInstanceOf(ClassConcrete.class);
    }

    @Test
    public void resolve_WhenAbstractClassNotAdded_ThenMissingDependenciesException()
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(ClassAbstract.class))
                  .isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void resolve_WhenAbstractClassAdded_ThenResolvedToSubtype()
    {
        // given
        dictionary.addType(ClassAbstract.class, ClassConcreteDerived.class,
                ConstructionPolicy.CONSTRUCTION);

        // when
        ClassAbstract result = testObject.resolve(ClassAbstract.class);

        // then
        Assertions.assertThat(result).isNotNull().isExactlyInstanceOf(ClassConcreteDerived.class);
    }

    @Test
    public void resolve_WhenConcreteClassNotAdded_ThenInstanceOfThisClass()
    {
        // when
        ClassConcrete result = testObject.resolve(ClassConcrete.class);

        // then
        Assertions.assertThat(result).isNotNull().isExactlyInstanceOf(ClassConcrete.class);
    }

    @Test
    public void resolve_WhenConcreteClassAddedWithSelf_ThenResolvedToThisType()
    {
        // given
        dictionary.addType(ClassConcrete.class, ConstructionPolicy.CONSTRUCTION);

        // when
        ClassConcrete result = testObject.resolve(ClassConcrete.class);

        // then
        Assertions.assertThat(result).isNotNull().isExactlyInstanceOf(ClassConcrete.class);
    }

    @Test
    public void resolve_WhenConcreteClassAddedWithSubtype_ThenResolvedToSubtype()
    {
        // given
        dictionary.addType(ClassConcrete.class, ClassConcreteDerived.class,
                ConstructionPolicy.CONSTRUCTION);

        // when
        ClassConcrete result = testObject.resolve(ClassConcrete.class);

        // then
        Assertions.assertThat(result).isNotNull().isExactlyInstanceOf(ClassConcreteDerived.class);
    }

    // endregion
    // region resolve [register annotations]

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
        dictionary.addType(ClassLinear.class, ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(ClassLinearFirst.class, ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(ClassLinearSecond.class, ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(ClassLinearThird.class, ConstructionPolicy.CONSTRUCTION);

        // when
        ClassLinear result = testObject.resolve(ClassLinear.class);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getFirst()).isNotNull();
        Assertions.assertThat(result.getFirst().getSecond()).isNotNull();
        Assertions.assertThat(result.getFirst().getSecond().getThird()).isNotNull();
    }

    @Test
    public void resolve_WhenCircular_ThenCircularDependenciesException()
    {
        // given
        dictionary.addType(ClassCircular.class, ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(ClassCircularLeft.class, ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(ClassCircularRight.class, ConstructionPolicy.CONSTRUCTION);

        // then
        Assertions.assertThatThrownBy(() -> testObject.resolve(ClassCircular.class))
                  .isInstanceOf(CircularDependenciesException.class);
    }

    @Test
    public void resolve_WhenDiamondWithoutSingleton_ThenInstanceIsResolved()
    {
        // given
        dictionary.addType(ClassDiamond.class, ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(ClassDiamondLeft.class, ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(ClassDiamondRight.class, ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(ClassDiamondTop.class, ConstructionPolicy.CONSTRUCTION);

        // when
        ClassDiamond result = testObject.resolve(ClassDiamond.class);

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
        dictionary.addType(ClassDiamond.class, ConstructionPolicy.SINGLETON);
        dictionary.addType(ClassDiamondLeft.class, ConstructionPolicy.SINGLETON);
        dictionary.addType(ClassDiamondRight.class, ConstructionPolicy.SINGLETON);
        dictionary.addType(ClassDiamondTop.class, ConstructionPolicy.SINGLETON);

        // when
        ClassDiamond result = testObject.resolve(ClassDiamond.class);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getLeft()).isNotNull();
        Assertions.assertThat(result.getRight()).isNotNull();
        Assertions.assertThat(result.getLeft().getTop()).isNotNull();
        Assertions.assertThat(result.getRight().getTop())
                  .isNotNull()
                  .isSameAs(result.getLeft().getTop());
    }

    @Test
    public void resolve_WhenDependencyNotPresent_ThenMissingDependenciesException()
    {
        // given
        dictionary.addType(ClassLinear.class, ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(ClassLinearFirst.class, ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(ClassLinearSecond.class, ConstructionPolicy.CONSTRUCTION);

        // then
        Assertions.assertThatThrownBy(() -> testObject.resolve(ClassLinear.class))
                  .isInstanceOf(MissingDependenciesException.class);
    }

    // endregion
    // region resolve [dependency annotation]

    @Test
    public void resolve_WhenDependencyAnnotationHasAddedTypes_ThenInstanceIsResolved()
    {

        // given
        dictionary.addType(ClassDependencyAnnotation.class, ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(ClassLinear.class, ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(ClassLinearFirst.class, ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(ClassLinearSecond.class, ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(ClassLinearThird.class, ConstructionPolicy.CONSTRUCTION);

        // when
        ClassDependencyAnnotation result = testObject.resolve(ClassDependencyAnnotation.class);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getLinear()).isNotNull();
        Assertions.assertThat(result.getDiamond()).isNull();
    }

    @Test
    public void resolve_WhenDependencyAnnotationHasAddedInstance_ThenInstanceIsResolved()
    {

        // given
        dictionary.addType(ClassDependencyAnnotation.class, ConstructionPolicy.CONSTRUCTION);
        dictionary.addInstance(ClassLinear.class, new ClassLinear(null));

        // when
        ClassDependencyAnnotation result = testObject.resolve(ClassDependencyAnnotation.class);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getLinear()).isNotNull();
        Assertions.assertThat(result.getLinear().getFirst()).isNull();
        Assertions.assertThat(result.getDiamond()).isNull();
    }

    @Test
    public void resolve_WhenAnnotationHasMissingDependency_ThenMissingDependenciesException()
    {

        // given
        dictionary.addType(ClassDependencyAnnotation.class, ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(ClassLinear.class, ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(ClassLinearFirst.class, ConstructionPolicy.CONSTRUCTION);

        // then
        Assertions.assertThatThrownBy(() -> testObject.resolve(ClassDependencyAnnotation.class))
                  .isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void resolve_WhenAnnotationOmitsCircularDependency_ThenInstanceIsResolved()
    {
        // given
        dictionary.addType(ClassDependencyOptionalCircular.class, ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(ClassLinear.class, ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(ClassLinearFirst.class, ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(ClassLinearSecond.class, ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(ClassLinearThird.class, ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(ClassCircular.class, ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(ClassCircularLeft.class, ConstructionPolicy.CONSTRUCTION);
        dictionary.addType(ClassCircularRight.class, ConstructionPolicy.CONSTRUCTION);

        // when
        ClassDependencyOptionalCircular result =
                testObject.resolve(ClassDependencyOptionalCircular.class);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getLinear()).isNotNull();
        Assertions.assertThat(result.getCircular()).isNull();
    }

    // endregion
}
