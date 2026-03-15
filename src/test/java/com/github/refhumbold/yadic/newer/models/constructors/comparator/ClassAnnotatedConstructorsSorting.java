package com.github.refhumbold.yadic.newer.models.constructors.comparator;

import java.util.Arrays;
import java.util.List;
import com.github.refhumbold.yadic.annotation.YadicDependency;
import com.github.refhumbold.yadic.newer.models.dependencies.linear.ClassLinearSecond;
import com.github.refhumbold.yadic.newer.models.inheritance.ClassConcrete;
import com.github.refhumbold.yadic.newer.models.inheritance.InterfaceInheritance;

public class ClassAnnotatedConstructorsSorting
{
    private final int number;
    private final String string;
    private final List<Double> doubles;
    private final InterfaceInheritance interfaceInheritance;
    private final ClassLinearSecond classLinearSecond;

    public ClassAnnotatedConstructorsSorting(
            int number,
            String string,
            List<Double> doubles,
            InterfaceInheritance interfaceInheritance,
            ClassLinearSecond classLinearSecond)
    {
        this.number = number;
        this.string = string;
        this.doubles = doubles;
        this.interfaceInheritance = interfaceInheritance;
        this.classLinearSecond = classLinearSecond;
    }

    public ClassAnnotatedConstructorsSorting(
            int number,
            String string,
            List<Double> doubles,
            InterfaceInheritance interfaceInheritance)
    {
        this.number = number;
        this.string = string;
        this.doubles = doubles;
        this.interfaceInheritance = interfaceInheritance;
        classLinearSecond = null;
    }

    @YadicDependency
    public ClassAnnotatedConstructorsSorting(int number, String string, List<Double> doubles)
    {
        this.number = number;
        this.string = string;
        this.doubles = doubles;
        interfaceInheritance = new ClassConcrete();
        classLinearSecond = null;
    }

    public ClassAnnotatedConstructorsSorting(int number, String string)
    {
        this.number = number;
        this.string = string;
        doubles = Arrays.asList(3.14, 2.71);
        interfaceInheritance = new ClassConcrete();
        classLinearSecond = null;
    }

    public ClassAnnotatedConstructorsSorting(int number)
    {
        this.number = number;
        string = "X";
        doubles = Arrays.asList(3.14, 2.71);
        interfaceInheritance = new ClassConcrete();
        classLinearSecond = null;
    }

    public ClassAnnotatedConstructorsSorting()
    {
        number = 0;
        string = "X";
        doubles = Arrays.asList(3.14, 2.71);
        interfaceInheritance = new ClassConcrete();
        classLinearSecond = null;
    }
}
