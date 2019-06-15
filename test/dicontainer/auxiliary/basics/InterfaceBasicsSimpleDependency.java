package dicontainer.auxiliary.basics;

import dicontainer.auxiliary.diamonds.InterfaceDiamonds1;

public interface InterfaceBasicsSimpleDependency
{
    InterfaceDiamonds1 getFirstObject();

    InterfaceBasicsStringGetter getSecondObject();
}
