package dicontainer.dictionary;

import dicontainer.ConstructionPolicy;

public class ClassMapping<T>
{
    public final Class<T> subtype;
    public ConstructionPolicy policy;

    public ClassMapping(Class<T> subtype)
    {
        this(subtype, ConstructionPolicy.getDefault());
    }

    public ClassMapping(Class<T> subtype, ConstructionPolicy policy)
    {
        this.subtype = subtype;
        this.policy = policy;
    }
}
