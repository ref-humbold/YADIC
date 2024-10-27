package yadic.models.comparator;

import java.util.Arrays;
import java.util.List;

import yadic.annotation.YadicDependency;
import yadic.models.basic.ClassBasicInheritsFromAbstract;
import yadic.models.basic.ClassBasicStringGetter;
import yadic.models.basic.InterfaceBasic;

public class ClassComparatorConstructor
{
    int number;
    String string;
    List<Double> doubles;
    InterfaceBasic interfaceBasic;
    ClassBasicStringGetter classBasicStringGetter;

    public ClassComparatorConstructor(
            int number,
            String string,
            List<Double> doubles,
            InterfaceBasic interfaceBasic,
            ClassBasicStringGetter classBasicStringGetter)
    {
        this.number = number;
        this.string = string;
        this.doubles = doubles;
        this.interfaceBasic = interfaceBasic;
        this.classBasicStringGetter = classBasicStringGetter;
    }

    public ClassComparatorConstructor(
            int number, String string, List<Double> doubles, InterfaceBasic interfaceBasic)
    {
        this.number = number;
        this.string = string;
        this.doubles = doubles;
        this.interfaceBasic = interfaceBasic;
        classBasicStringGetter = null;
    }

    @YadicDependency
    public ClassComparatorConstructor(int number, String string, List<Double> doubles)
    {
        this.number = number;
        this.string = string;
        this.doubles = doubles;
        interfaceBasic = new ClassBasicInheritsFromAbstract();
        classBasicStringGetter = null;
    }

    public ClassComparatorConstructor(int number, String string)
    {
        this.number = number;
        this.string = string;
        doubles = Arrays.asList(3.14, 2.71);
        interfaceBasic = new ClassBasicInheritsFromAbstract();
        classBasicStringGetter = null;
    }

    public ClassComparatorConstructor(int number)
    {
        this.number = number;
        string = "X";
        doubles = Arrays.asList(3.14, 2.71);
        interfaceBasic = new ClassBasicInheritsFromAbstract();
        classBasicStringGetter = null;
    }

    public ClassComparatorConstructor()
    {
        number = 0;
        string = "X";
        doubles = Arrays.asList(3.14, 2.71);
        interfaceBasic = new ClassBasicInheritsFromAbstract();
        classBasicStringGetter = null;
    }
}
