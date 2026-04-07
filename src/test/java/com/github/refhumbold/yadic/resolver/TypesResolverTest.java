package com.github.refhumbold.yadic.resolver;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import com.github.refhumbold.yadic.ConstructionPolicy;
import com.github.refhumbold.yadic.models.annotations.register.*;
import com.github.refhumbold.yadic.models.annotations.registerself.ClassAbstractRegisterSelf;
import com.github.refhumbold.yadic.models.annotations.registerself.ClassRegisterSelf;
import com.github.refhumbold.yadic.models.annotations.registerself.InterfaceRegisterSelf;
import com.github.refhumbold.yadic.models.constructors.*;
import com.github.refhumbold.yadic.models.constructors.annotation.ClassAnnotatedDefaultConstructor;
import com.github.refhumbold.yadic.models.constructors.annotation.ClassAnnotatedMultipleConstructors;
import com.github.refhumbold.yadic.models.constructors.annotation.ClassAnnotatedParameterizedConstructor;
import com.github.refhumbold.yadic.models.inheritance.ClassAbstract;
import com.github.refhumbold.yadic.models.inheritance.ClassConcrete;
import com.github.refhumbold.yadic.models.inheritance.ClassConcreteDerived;
import com.github.refhumbold.yadic.models.inheritance.InterfaceInheritance;
import com.github.refhumbold.yadic.models.setter.*;
import com.github.refhumbold.yadic.registry.DependencyRegistry;
import com.github.refhumbold.yadic.registry.exception.AbstractTypeException;
import com.github.refhumbold.yadic.resolver.exception.*;

public class TypesResolverTest
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

    @ParameterizedTest
    @ValueSource(classes = {
            byte.class, short.class, int.class, long.class, float.class, double.class, char.class,
            boolean.class
    })
    public void resolve_WhenPrimitiveType_ThenNoSuitableConstructorException(Class<?> cls)
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(cls))
                  .isInstanceOf(NoSuitableConstructorException.class);
    }

    @Test
    public void resolve_WhenClassHasDefaultConstructorOnly_ThenInstance()
    {
        // when
        ClassDefaultConstructorOnly result = testObject.resolve(ClassDefaultConstructorOnly.class);

        // then
        Assertions.assertThat(result)
                  .isNotNull()
                  .isExactlyInstanceOf(ClassDefaultConstructorOnly.class);
    }

    @ParameterizedTest
    @ValueSource(classes = {
            ClassParameterizedConstructorString.class, ClassParameterizedConstructorPrimitive.class,
            ClassParameterizedConstructorBoxed.class
    })
    public void resolve_WhenClassHasParameterConstructorWithoutAddedParameter_ThenMissingDependenciesException(
            Class<?> cls)
    {
        // then
        Assertions.assertThatThrownBy(() -> testObject.resolve(cls))
                  .isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void resolve_WhenClassHasPrimitiveParameterConstructorWithAddedParameter_ThenInstance()
    {
        // given
        int number = 10;

        dictionary.addInstance(int.class, number);

        // when
        ClassParameterizedConstructorPrimitive result =
                testObject.resolve(ClassParameterizedConstructorPrimitive.class);

        // then
        Assertions.assertThat(result)
                  .isNotNull()
                  .isExactlyInstanceOf(ClassParameterizedConstructorPrimitive.class);
        Assertions.assertThat(result.getNumber()).isEqualTo(number);
    }

    @Test
    public void resolve_WhenClassHasReferenceParameterConstructorWithAddedParameter_ThenInstance()
    {
        // given
        Integer number = 10;

        dictionary.addInstance(Integer.class, number);

        // when
        ClassParameterizedConstructorBoxed result =
                testObject.resolve(ClassParameterizedConstructorBoxed.class);

        // then
        Assertions.assertThat(result)
                  .isNotNull()
                  .isExactlyInstanceOf(ClassParameterizedConstructorBoxed.class);
        Assertions.assertThat(result.getNumber()).isEqualTo(number);
    }

    @Test
    public void resolve_WhenClassHasPrimitiveParameterConstructorButAddedBoxedParameter_ThenMissingDependenciesException()
    {
        // given
        dictionary.addInstance(Integer.class, 10);

        // then
        Assertions.assertThatThrownBy(
                          () -> testObject.resolve(ClassParameterizedConstructorPrimitive.class))
                  .isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void resolve_WhenClassHasBoxedParameterConstructorButAddedPrimitiveParameter_ThenMissingDependenciesException()
    {
        // given
        dictionary.addInstance(int.class, 10);

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
    // region resolve [register annotations]

    @Test
    public void resolve_WhenAnnotatedInterface_ThenInstance()
    {
        // when
        InterfaceRegister result = testObject.resolve(InterfaceRegister.class);

        // then
        Assertions.assertThat(result)
                  .isNotNull()
                  .isExactlyInstanceOf(ClassDerivedFromInterfaceRegister.class);
    }

    @Test
    public void resolve_WhenAnnotatedAbstractClass_ThenInstance()
    {
        // when
        ClassAbstractRegister result = testObject.resolve(ClassAbstractRegister.class);

        // then
        Assertions.assertThat(result)
                  .isNotNull()
                  .isExactlyInstanceOf(ClassDerivedFromAbstractRegister.class);
    }

    @Test
    public void resolve_WhenAnnotatedConcreteClass_ThenInstance()
    {
        // when
        ClassRegister result = testObject.resolve(ClassRegister.class);

        // then
        Assertions.assertThat(result)
                  .isNotNull()
                  .isExactlyInstanceOf(ClassDerivedFromRegister.class);
    }

    @ParameterizedTest
    @ValueSource(classes = { InterfaceRegisterSelf.class, ClassAbstractRegisterSelf.class })
    public void resolve_WhenSelfAnnotatedAbstractType_ThenAbstractTypeException(Class<?> cls)
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(cls))
                  .isInstanceOf(AbstractTypeException.class);
    }

    @Test
    public void resolve_WhenSelfAnnotatedConcreteClass_ThenInstance()
    {
        // given
        Class<ClassRegisterSelf> type = ClassRegisterSelf.class;

        // when
        ClassRegisterSelf result = testObject.resolve(type);

        // then
        Assertions.assertThat(result).isNotNull().isExactlyInstanceOf(type);
    }

    // endregion
    // region resolve [inheritance]

    @ParameterizedTest
    @ValueSource(classes = { InterfaceInheritance.class, ClassAbstract.class })
    public void resolve_WhenAbstractTypeNotAdded_ThenMissingDependenciesException(Class<?> cls)
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(cls))
                  .isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void resolve_WhenInterfaceAdded_ThenResolvedToSubtype()
    {
        // given
        Class<InterfaceInheritance> type = InterfaceInheritance.class;
        Class<ClassConcreteDerived> subtype = ClassConcreteDerived.class;

        dictionary.addType(type, subtype, ConstructionPolicy.CONSTRUCTION);

        // when
        InterfaceInheritance result = testObject.resolve(type);

        // then
        Assertions.assertThat(result).isNotNull().isExactlyInstanceOf(subtype);
    }

    @Test
    public void resolve_WhenAbstractClassAdded_ThenResolvedToSubtype()
    {
        // given
        Class<ClassAbstract> type = ClassAbstract.class;
        Class<ClassConcreteDerived> subtype = ClassConcreteDerived.class;

        dictionary.addType(type, subtype, ConstructionPolicy.CONSTRUCTION);

        // when
        ClassAbstract result = testObject.resolve(type);

        // then
        Assertions.assertThat(result).isNotNull().isExactlyInstanceOf(subtype);
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
        Class<ClassConcrete> type = ClassConcrete.class;

        dictionary.addType(type, ConstructionPolicy.CONSTRUCTION);

        // when
        ClassConcrete result = testObject.resolve(type);

        // then
        Assertions.assertThat(result).isNotNull().isExactlyInstanceOf(type);
    }

    @Test
    public void resolve_WhenConcreteClassAddedWithSubtype_ThenResolvedToSubtype()
    {
        // given
        Class<ClassConcrete> type = ClassConcrete.class;
        Class<ClassConcreteDerived> subtype = ClassConcreteDerived.class;

        dictionary.addType(type, subtype, ConstructionPolicy.CONSTRUCTION);

        // when
        ClassConcrete result = testObject.resolve(type);

        // then
        Assertions.assertThat(result).isNotNull().isExactlyInstanceOf(subtype);
    }

    // endregion
    // region resolve [annotated constructor]

    @Test
    public void resolve_WhenAnnotatedDefaultConstructor_ThenInstance()
    {
        // when
        ClassAnnotatedDefaultConstructor result =
                testObject.resolve(ClassAnnotatedDefaultConstructor.class);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getString()).isEmpty();
    }

    @Test
    public void resolve_WhenAnnotatedParameterizedConstructorWithDependency_ThenInstance()
    {
        // given
        String string = "qwertyuiop";

        dictionary.addInstance(String.class, string);

        // when
        ClassAnnotatedParameterizedConstructor result =
                testObject.resolve(ClassAnnotatedParameterizedConstructor.class);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getString()).isEqualTo(string);
    }

    @Test
    public void resolve_WhenAnnotatedParameterizedConstructorWithoutDependency_ThenNoInstanceCreatedException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.resolve(ClassAnnotatedParameterizedConstructor.class))
                  .isInstanceOf(NoInstanceCreatedException.class);
    }

    @Test
    public void resolve_WhenMultipleAnnotatedConstructors_ThenMultipleAnnotatedConstructorsException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.resolve(ClassAnnotatedMultipleConstructors.class))
                  .isInstanceOf(MultipleAnnotatedConstructorsException.class);
    }

    // endregion
    // region resolve [annotated setter]

    @ParameterizedTest
    @ValueSource(classes = {
            ClassSetterInvalidReturnType.class, ClassSetterNoParameter.class,
            ClassSetterInvalidName.class, ClassSetterMultipleParameters.class
    })
    public void resolve_WhenSetterIsInvalid_ThenIncorrectDependencySetterException(Class<?> cls)
    {
        // given
        dictionary.addInstance(String.class, "qwertyuiop");
        dictionary.addInstance(int.class, 10);

        // then
        Assertions.assertThatThrownBy(() -> testObject.resolve(cls))
                  .isInstanceOf(IncorrectDependencySetterException.class);
    }

    @ParameterizedTest
    @ValueSource(classes = {
            ClassSetterString.class, ClassSetterPrimitive.class, ClassSetterBoxed.class
    })
    public void resolve_WhenSetterHasMissingDependency_ThenMissingDependenciesException(Class<?> cls)
    {
        Assertions.assertThatThrownBy(() -> testObject.resolve(cls))
                  .isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void resolve_WhenSetterHasPrimitiveParameterWithAddedParameter_ThenInstance()
    {
        // given
        int number = 10;

        dictionary.addInstance(int.class, number);

        // when
        ClassSetterPrimitive result = testObject.resolve(ClassSetterPrimitive.class);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getNumber()).isEqualTo(number);
    }

    @Test
    public void resolve_WhenSetterHasReferenceParameterWithAddedParameter_ThenInstance()
    {
        // given
        Integer number = 10;

        dictionary.addInstance(Integer.class, number);

        // when
        ClassSetterBoxed result = testObject.resolve(ClassSetterBoxed.class);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getNumber()).isEqualTo(number);
    }

    @Test
    public void resolve_WhenSetterHasPrimitiveParameterButAddedBoxedParameter_ThenMissingDependenciesException()
    {
        // given
        dictionary.addInstance(Integer.class, 10);

        // then
        Assertions.assertThatThrownBy(() -> testObject.resolve(ClassSetterPrimitive.class))
                  .isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void resolve_WhenSetterHasBoxedParameterButAddedPrimitiveParameter_ThenMissingDependenciesException()
    {
        // given
        dictionary.addInstance(int.class, 10);

        // then
        Assertions.assertThatThrownBy(() -> testObject.resolve(ClassSetterBoxed.class))
                  .isInstanceOf(MissingDependenciesException.class);
    }

    @Test
    public void resolve_WhenSetterThrowsException_ThenSetterInvocationException()
    {
        // given
        dictionary.addInstance(int.class, 10);

        // then
        Assertions.assertThatThrownBy(() -> testObject.resolve(ClassSetterThrows.class))
                  .isInstanceOf(SetterInvocationException.class);
    }

    // endregion
    // region resolveOrNull

    @Test
    public void resolveOrNull_WhenClassCanBeResolved_ThenInstance()
    {
        // when
        ClassDefaultConstructorOnly result =
                testObject.resolveOrNull(ClassDefaultConstructorOnly.class);

        // then
        Assertions.assertThat(result)
                  .isNotNull()
                  .isExactlyInstanceOf(ClassDefaultConstructorOnly.class);
    }

    @ParameterizedTest
    @ValueSource(classes = {
            InterfaceInheritance.class, ClassAbstract.class, ClassConstructorThrows.class,
            ClassPrivateConstructorOnly.class
    })
    public void resolveOrNull_WhenClassCannotBeResolved_ThenNull(Class<?> cls)
    {
        // when
        Object result = testObject.resolveOrNull(cls);

        // then
        Assertions.assertThat(result).isNull();
    }

    @ParameterizedTest
    @ValueSource(classes = {
            byte.class, short.class, int.class, long.class, float.class, double.class, char.class,
            boolean.class
    })
    public void resolveOrNull_WhenPrimitiveType_ThenNull(Class<?> cls)
    {
        // when
        Object result = testObject.resolveOrNull(cls);

        // then
        Assertions.assertThat(result).isNull();
    }

    //endregion
}
