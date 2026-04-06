package com.github.refhumbold.yadic.models.constructors.comparator;

import java.util.Arrays;
import java.util.List;
import com.github.refhumbold.yadic.annotation.YadicDependency;
import com.github.refhumbold.yadic.models.dependencies.linear.ClassLinear;
import com.github.refhumbold.yadic.models.inheritance.ClassConcrete;
import com.github.refhumbold.yadic.models.inheritance.InterfaceInheritance;

public class ClassAnnotatedConstructorsSorting
{
    private final int number;
    private final String string;
    private final List<Double> doubles;
    private final InterfaceInheritance inheritance;
    private final ClassLinear linear;

    public ClassAnnotatedConstructorsSorting(
            int number,
            String string,
            List<Double> doubles,
            InterfaceInheritance inheritance,
            ClassLinear linear)
    {
        this.number = number;
        this.string = string;
        this.doubles = doubles;
        this.inheritance = inheritance;
        this.linear = linear;
    }

    public ClassAnnotatedConstructorsSorting(
            int number,
            String string,
            List<Double> doubles,
            InterfaceInheritance inheritance)
    {
        this.number = number;
        this.string = string;
        this.doubles = doubles;
        this.inheritance = inheritance;
        linear = null;
    }

    @YadicDependency
    public ClassAnnotatedConstructorsSorting(int number, String string, List<Double> doubles)
    {
        this.number = number;
        this.string = string;
        this.doubles = doubles;
        inheritance = new ClassConcrete();
        linear = null;
    }

    public ClassAnnotatedConstructorsSorting(int number, String string)
    {
        this.number = number;
        this.string = string;
        doubles = Arrays.asList(3.14, 2.71);
        inheritance = new ClassConcrete();
        linear = null;
    }

    public ClassAnnotatedConstructorsSorting(int number)
    {
        this.number = number;
        string = "X";
        doubles = Arrays.asList(3.14, 2.71);
        inheritance = new ClassConcrete();
        linear = null;
    }

    public ClassAnnotatedConstructorsSorting()
    {
        number = 0;
        string = "X";
        doubles = Arrays.asList(3.14, 2.71);
        inheritance = new ClassConcrete();
        linear = null;
    }

    public int getNumber()
    {
        return number;
    }

    public String getString()
    {
        return string;
    }

    public List<Double> getDoubles()
    {
        return doubles;
    }

    public InterfaceInheritance getInheritance()
    {
        return inheritance;
    }

    public ClassLinear getLinear()
    {
        return linear;
    }
}
