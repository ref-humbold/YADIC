package yadic.models.setter;

import yadic.annotation.YadicDependency;

public class ClassSetterWithoutParameters
{
    public ClassSetterWithoutParameters()
    {
    }

    @YadicDependency
    public void setMethod()
    {
        System.out.println();
    }
}
