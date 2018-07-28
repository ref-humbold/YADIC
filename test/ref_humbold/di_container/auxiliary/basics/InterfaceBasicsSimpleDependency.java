package ref_humbold.di_container.auxiliary.basics;

import ref_humbold.di_container.auxiliary.diamonds.InterfaceDiamonds1;

public interface InterfaceBasicsSimpleDependency
{
    InterfaceDiamonds1 getFirstObject();

    InterfaceBasicsStringGetter getSecondObject();
}
