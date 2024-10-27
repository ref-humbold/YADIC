package yadic.models.setter;

import yadic.annotation.YadicDependency;

public class ClassSetterThrows
{
    @YadicDependency
    public void setString(String string)
            throws Exception
    {
        throw new Exception();
    }
}
