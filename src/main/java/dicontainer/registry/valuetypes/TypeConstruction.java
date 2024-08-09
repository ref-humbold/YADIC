package dicontainer.registry.valuetypes;

import dicontainer.ConstructionPolicy;

public record TypeConstruction<T>(Class<T> type, ConstructionPolicy policy)
{
}
