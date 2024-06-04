package dicontainer.models.setter;

import dicontainer.annotation.Dependency;

public class ClassSetterIncorrectName
{
    private int number;

    public ClassSetterIncorrectName()
    {
    }

    @Dependency
    public void method(int number)
    {
        this.number = number;
    }
}
