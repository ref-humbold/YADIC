package ref_humbold.di_container;

import ref_humbold.di_container.auxiliary.basics.*;
import ref_humbold.di_container.auxiliary.constructors.ClassConstructorsDefault;
import ref_humbold.di_container.auxiliary.diamonds.*;

class CustomProvider
    extends DIContainerBaseProvider
{
    public CustomProvider(DIContainer container)
    {
        super(container);
    }

    @Override
    protected void configureContainer(DIContainer container)
    {
        container.registerType(InterfaceBasicsComplexDependency.class,
                               ClassBasicsComplexDependency.class)
                 .registerType(InterfaceBasics.class, ClassConstructorsDefault.class)
                 .registerType(InterfaceDiamonds1.class, ClassDiamonds1.class)
                 .registerType(InterfaceDiamondsBottom.class, ClassDiamondsBottom.class)
                 .registerType(InterfaceDiamondsTop.class, ClassDiamondsTop.class)
                 .registerType(InterfaceBasicsStringGetter.class, ClassBasicsStringGetter.class)
                 .registerInstance(String.class, "string");
    }
}
