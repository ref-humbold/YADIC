package dicontainer.dictionary;

import dicontainer.ConstructionPolicy;

public class SubtypeMapping<T>
{
    public final Class<T> subtype;
    public final boolean isFromAnnotation;
    public final ConstructionPolicy policy;

    public SubtypeMapping(Class<T> subtype, boolean isFromAnnotation, ConstructionPolicy policy)
    {
        this.subtype = subtype;
        this.isFromAnnotation = isFromAnnotation;
        this.policy = policy;
    }
}
