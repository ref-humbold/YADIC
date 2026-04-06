package com.github.refhumbold.yadic.models.setter;

import com.github.refhumbold.yadic.annotation.YadicDependency;
import com.github.refhumbold.yadic.models.inheritance.InterfaceInheritance;

public class ClassSetterInterface
{
    private InterfaceInheritance interface_;

    public InterfaceInheritance getInterface()
    {
        return interface_;
    }

    @YadicDependency
    public void setInterface(InterfaceInheritance interface_)
    {
        this.interface_ = interface_;
    }
}
