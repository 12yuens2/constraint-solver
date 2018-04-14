package csp.heuristic.impl;

import java.util.ArrayList;
import java.util.TreeSet;

import csp.Variable;
import csp.heuristic.Heuristic;

public class SmallestDomainFirst implements Heuristic {

    public Variable getNextVariable(ArrayList<Variable> variables) {
        Variable smallestDomain = variables.get(0);
        for (Variable v : variables) {
            if (v.getDomain().size() < smallestDomain.getDomain().size()) {
                smallestDomain = v;
            }
        }
        return smallestDomain;
    }

    public int getNextValue(TreeSet<Integer> domain) {
        return domain.first();
    }
    
    public String toString() {
        return "Smallest Domain First";
    }

}
