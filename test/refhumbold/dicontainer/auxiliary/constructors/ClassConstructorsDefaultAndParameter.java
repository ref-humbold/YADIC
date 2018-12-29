package refhumbold.dicontainer.auxiliary.constructors;

import refhumbold.dicontainer.auxiliary.basics.InterfaceBasics;

public class ClassConstructorsDefaultAndParameter
    implements InterfaceBasics
{
    private String text;

    public ClassConstructorsDefaultAndParameter()
    {
        text = "";
    }

    public ClassConstructorsDefaultAndParameter(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return text;
    }
}
