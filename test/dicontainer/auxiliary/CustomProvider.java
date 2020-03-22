package dicontainer.auxiliary;

import dicontainer.DIContainer;
import dicontainer.DIContainerProvider;
import dicontainer.auxiliary.basic.ClassBasicComplexDependency;
import dicontainer.auxiliary.basic.ClassBasicStringGetter;
import dicontainer.auxiliary.basic.InterfaceBasic;
import dicontainer.auxiliary.basic.InterfaceBasicComplexDependency;
import dicontainer.auxiliary.basic.InterfaceBasicStringGetter;
import dicontainer.auxiliary.constructor.ClassConstructorDefault;
import dicontainer.auxiliary.diamond.*;

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
        container.registerType(InterfaceBasicComplexDependency.class,
                               ClassBasicComplexDependency.class)
                 .registerType(InterfaceBasic.class, ClassConstructorDefault.class)
                 .registerType(InterfaceDiamondLeft.class, ClassDiamondLeft.class)
                 .registerType(InterfaceDiamondBottom.class, ClassDiamondBottom.class)
                 .registerType(InterfaceDiamondTop.class, ClassDiamondTop.class)
                 .registerType(InterfaceBasicStringGetter.class, ClassBasicStringGetter.class)
                 .registerInstance(String.class, "string");
    }
}
