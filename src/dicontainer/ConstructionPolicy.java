package dicontainer;

public enum ConstructionPolicy
{
    CONSTRUCT, SINGLETON;

    public static ConstructionPolicy getDefault()
    {
        return CONSTRUCT;
    }
}
