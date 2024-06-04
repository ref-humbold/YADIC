package dicontainer.models.setter;

import dicontainer.annotation.Dependency;

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
