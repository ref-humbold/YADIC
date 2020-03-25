package dicontainer.dictionary;

import dicontainer.ConstructionPolicy;

public class SubtypeMapping<T>
{
    public final Class<T> subtype;
    public final ConstructionPolicy policy;

    public SubtypeMapping(Class<T> subtype, ConstructionPolicy policy)
    {
        this.subtype = subtype;
        this.policy = policy;
    }
}
