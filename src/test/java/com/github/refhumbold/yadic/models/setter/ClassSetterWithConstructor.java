package com.github.refhumbold.yadic.models.setter;

import com.github.refhumbold.yadic.annotation.YadicDependency;
import com.github.refhumbold.yadic.models.dependencies.linear.ClassLinear;
import com.github.refhumbold.yadic.models.inheritance.InterfaceInheritance;

public class ClassSetterWithConstructor
{
    private final InterfaceInheritance inheritance;
    private ClassLinear linear;

    @YadicDependency
    public ClassSetterWithConstructor(InterfaceInheritance inheritance)
    {
        this.inheritance = inheritance;
    }

    public InterfaceInheritance getInheritance()
    {
        return inheritance;
    }

    public ClassLinear getLinear()
    {
        return linear;
    }

    @YadicDependency
    public void setLinear(ClassLinear linear)
    {
        this.linear = linear;
    }
}
