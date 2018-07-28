package ref_humbold.di_container.auxiliary.basics;

public class ClassBasicsStringGetter
    implements InterfaceBasicsStringGetter
{
    private String text;

    public ClassBasicsStringGetter()
    {
        this.text = "";
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
