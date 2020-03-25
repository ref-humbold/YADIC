package dicontainer.resolver;

import dicontainer.dictionary.InstancesDictionary;
import dicontainer.dictionary.TypesDictionary;

public class ConstructorResolver
{
    private final TypesDictionary typesDictionary;
    private final InstancesDictionary instancesDictionary;

    public ConstructorResolver(TypesDictionary typesDictionary,
                               InstancesDictionary instancesDictionary)
    {
        this.typesDictionary = typesDictionary;
        this.instancesDictionary = instancesDictionary;
    }
}
