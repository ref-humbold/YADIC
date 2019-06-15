package dicontainer.auxiliary.basics;

public class ClassBasicsStringGetter
        implements InterfaceBasicsStringGetter
{
    private String text;

    public ClassBasicsStringGetter()
    {
        text = "";
    }

    public ClassBasicsStringGetter(String text)
    {
        this.text = text;
    }

    @Override
    public String getString()
    {
        return text;
    }
}
