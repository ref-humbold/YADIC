package yadic.models.setter;

import yadic.annotation.Dependency;

public class ClassSetterWithoutParameters
{
    public ClassSetterWithoutParameters()
    {
    }

    @Dependency
    public void setMethod()
    {
        System.out.println();
    }
}
