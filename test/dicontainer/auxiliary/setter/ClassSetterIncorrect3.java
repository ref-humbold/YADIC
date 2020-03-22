package dicontainer.auxiliary.setter;

import dicontainer.annotation.Dependency;

public class ClassSetterIncorrect3
{
    private int number;

    public ClassSetterIncorrect3()
    {
    }

    @Dependency
    public void method(int number)
    {
        this.number = number;
    }
}
