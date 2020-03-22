package dicontainer.auxiliary.setter;

import dicontainer.annotation.Dependency;

public class ClassSetterIncorrect2
{
    public ClassSetterIncorrect2()
    {
    }

    @Dependency
    public void setMethod()
    {
        System.out.println();
    }
}
