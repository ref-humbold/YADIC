package dicontainer.auxiliary;

import dicontainer.DIContainer;
import dicontainer.DIContainerProvider;
import dicontainer.auxiliary.basics.ClassBasicsComplexDependency;
import dicontainer.auxiliary.basics.ClassBasicsStringGetter;
import dicontainer.auxiliary.basics.InterfaceBasics;
import dicontainer.auxiliary.basics.InterfaceBasicsComplexDependency;
import dicontainer.auxiliary.basics.InterfaceBasicsStringGetter;
import dicontainer.auxiliary.constructors.ClassConstructorsDefault;
import dicontainer.auxiliary.diamonds.*;

public class CustomProvider
        extends DIContainerProvider
{
    public CustomProvider(DIContainer container)
    {
        super(container);
    }

    @Override
    public void configure(DIContainer container)
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
