package com.github.refhumbold.yadic.registry.valuetypes;

import com.github.refhumbold.yadic.ConstructionPolicy;

public record TypeConstruction<T>(Class<T> type, ConstructionPolicy policy)
{
}
