package dicontainer.models.basic;

public class ClassBasicStringGetter
        implements InterfaceBasicStringGetter
{
    private String text;

    public ClassBasicStringGetter()
    {
        text = "";
    }

    public ClassBasicStringGetter(String text)
    {
        this.text = text;
    }

    @Override
    public String getString()
    {
        return text;
    }
}
