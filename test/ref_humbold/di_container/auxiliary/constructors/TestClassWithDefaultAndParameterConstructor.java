package ref_humbold.di_container.auxiliary.constructors;

import ref_humbold.di_container.auxiliary.basics.TestInterfaceBasic;

public class TestClassWithDefaultAndParameterConstructor
    implements TestInterfaceBasic
{
    private String text;

    public TestClassWithDefaultAndParameterConstructor()
    {
        this.text = "";
    }

    public TestClassWithDefaultAndParameterConstructor(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return text;
    }
}
