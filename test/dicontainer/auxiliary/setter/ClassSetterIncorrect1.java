package dicontainer.auxiliary.setter;

import dicontainer.annotation.Dependency;

public class ClassSetterIncorrect1
{
    public ClassSetterIncorrect1()
    {
    }

    @Dependency
    public int method(int number)
    {
        return number + 1;
    }
}
