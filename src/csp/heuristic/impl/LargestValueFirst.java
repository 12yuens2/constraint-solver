package csp.heuristic.impl;

import java.util.ArrayList;
import java.util.TreeSet;

import csp.Variable;
import csp.heuristic.Heuristic;

public class LargestValueFirst extends SmallestDomainFirst {

    @Override
    public Variable getNextVariable(ArrayList<Variable> variables) {
        return super.getNextVariable(variables);
    }

    @Override
    public int getNextValue(TreeSet<Integer> domain) {
        return domain.last();
    }
    
    @Override
    public String toString() {
        return "Largest value first";
    }

}
