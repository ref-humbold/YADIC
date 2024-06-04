package dicontainer.models.setter;

import dicontainer.annotation.Dependency;

public class ClassSetterThrows
{
    @Dependency
    public void setString(String string)
            throws Exception
    {
        throw new Exception();
    }
}
