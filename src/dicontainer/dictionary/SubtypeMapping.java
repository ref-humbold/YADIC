package dicontainer.dictionary;

import dicontainer.ConstructionPolicy;

public class SubtypeMapping<T>
{
    public final Class<T> subtype;
    public final ConstructionPolicy policy;
    public final boolean byInstance;

    public SubtypeMapping(Class<T> subtype, ConstructionPolicy policy, boolean byInstance)
    {
        this.subtype = subtype;
        this.policy = policy;
        this.byInstance = byInstance;
    }
}
