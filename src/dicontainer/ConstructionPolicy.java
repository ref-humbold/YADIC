package dicontainer;

public enum ConstructionPolicy
{
    CONSTRUCT, SINGLETON;

    public static final ConstructionPolicy defaultPolicy = CONSTRUCT;
}
