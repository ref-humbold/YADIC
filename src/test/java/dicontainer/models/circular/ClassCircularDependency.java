package dicontainer.models.circular;

import dicontainer.models.basic.InterfaceBasicStringGetter;

public class ClassCircularDependency
        implements InterfaceCircularDependency
{
    InterfaceCircularLeft circular;
    InterfaceBasicStringGetter nonCircular;

    public ClassCircularDependency(
            InterfaceCircularLeft circular, InterfaceBasicStringGetter nonCircular)
    {
        this.circular = circular;
        this.nonCircular = nonCircular;
    }

    public ClassCircularDependency(InterfaceBasicStringGetter nonCircular)
    {
        this.nonCircular = nonCircular;
    }

    @Override
    public InterfaceBasicStringGetter getNonCircularObject()
    {
        return nonCircular;
    }

    @Override
    public InterfaceCircularLeft getCircularObject()
    {
        return circular;
    }
}
