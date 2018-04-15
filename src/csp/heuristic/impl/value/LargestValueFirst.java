package csp.heuristic.impl.value;

import java.util.TreeSet;

import csp.heuristic.impl.variable.SmallestDomainFirst;

public class LargestValueFirst extends SmallestDomainFirst {


    @Override
    public int getNextValue(TreeSet<Integer> domain) {
        return domain.last();
    }
    
    @Override
    public String toString() {
        return "Largest value first";
    }

}
