package refhumbold.dicontainer;

import refhumbold.dicontainer.auxiliary.basics.*;
import refhumbold.dicontainer.auxiliary.constructors.ClassConstructorsDefault;
import refhumbold.dicontainer.auxiliary.diamonds.*;

class CustomProvider
    extends DIContainerProvider
{
    public CustomProvider(DIContainer container)
    {
        super(container);
    }

    @Override
    protected void configure(DIContainer container)
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
