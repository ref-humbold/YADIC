package refhumbold.dicontainer.auxiliary.circulars;

import refhumbold.dicontainer.auxiliary.basics.InterfaceBasicsStringGetter;

public class ClassCircularsDependency
    implements InterfaceCircularsDependency
{
    InterfaceCirculars1 circular;
    InterfaceBasicsStringGetter nonCircular;

    public ClassCircularsDependency(InterfaceCirculars1 circular,
                                    InterfaceBasicsStringGetter nonCircular)
    {
        this.circular = circular;
        this.nonCircular = nonCircular;
    }

    public ClassCircularsDependency(InterfaceBasicsStringGetter nonCircular)
    {
        this.nonCircular = nonCircular;
    }

    @Override
    public InterfaceBasicsStringGetter getNonCircularObject()
    {
        return nonCircular;
    }

    @Override
    public InterfaceCirculars1 getCircularObject()
    {
        return circular;
    }
}
