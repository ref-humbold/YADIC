package ref_humbold.di_container;

class TestProvider
    extends DIContainerBaseProvider
{
    public TestProvider(DIContainer container)
    {
        super(container);
    }

    @Override
    protected void configureContainer(DIContainer container)
    {
        container.registerType(TestInterfaceComplexDependency.class,
                               TestClassComplexDependency.class);
        container.registerType(TestInterfaceBasic.class, TestClassWithDefaultConstructorOnly.class);
        container.registerType(TestInterfaceDiamond1.class, TestClassDiamond1.class);
        container.registerType(TestInterfaceDiamondBase.class, TestClassDiamondBase.class);
        container.registerType(TestInterfaceWithString.class, TestClassWithString.class);

        container.registerInstance(String.class, "string");
    }
}
