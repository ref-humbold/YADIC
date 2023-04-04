package dicontainer.auxiliary.constructor;

import dicontainer.auxiliary.basic.InterfaceBasic;

public class ClassConstructorDefaultAndParameterized
        implements InterfaceBasic
{
    private String text;

    public ClassConstructorDefaultAndParameterized()
    {
        text = "";
    }

    public ClassConstructorDefaultAndParameterized(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return text;
    }
}
