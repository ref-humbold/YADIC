package com.github.refhumbold.yadic.models.setter;

import com.github.refhumbold.yadic.annotation.YadicDependency;
import com.github.refhumbold.yadic.models.dependencies.linear.ClassLinear;
import com.github.refhumbold.yadic.models.inheritance.InterfaceInheritance;

public class ClassSetterOnly
{
    private InterfaceInheritance inheritance;
    private ClassLinear linear;

    public InterfaceInheritance getInheritance()
    {
        return inheritance;
    }

    @YadicDependency
    public void setInheritance(InterfaceInheritance inheritance)
    {
        this.inheritance = inheritance;
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
