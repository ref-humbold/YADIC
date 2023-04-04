package dicontainer.dictionary.valuetypes;

import dicontainer.ConstructionPolicy;

public class Subtype<T>
{
    public final Class<T> subtype;
    public final ConstructionPolicy policy;

    public Subtype(Class<T> subtype, ConstructionPolicy policy)
    {
        this.subtype = subtype;
        this.policy = policy;
    }
}
