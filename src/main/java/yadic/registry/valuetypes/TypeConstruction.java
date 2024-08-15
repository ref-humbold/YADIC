package yadic.registry.valuetypes;

import yadic.ConstructionPolicy;

public record TypeConstruction<T>(Class<T> type, ConstructionPolicy policy)
{
}
