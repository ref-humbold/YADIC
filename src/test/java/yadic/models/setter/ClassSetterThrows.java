package yadic.models.setter;

import yadic.annotation.Dependency;

public class ClassSetterThrows
{
    @Dependency
    public void setString(String string)
            throws Exception
    {
        throw new Exception();
    }
}
