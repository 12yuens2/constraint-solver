package csp.heuristic.impl;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

import csp.Variable;
import csp.heuristic.Heuristic;

public class SmallestDomainFirst implements Heuristic {

    @Override
    public Variable getNextVariable(ArrayList<Variable> variables) {
        Variable smallestDomain = variables.get(0);
        for (Variable v : variables) {
            if (v.getDomain().size() < smallestDomain.getDomain().size()) {
                smallestDomain = v;
            }
        }
        return smallestDomain;
    }

    @Override
    public int getNextVariable(TreeSet<Integer> domain) {
        return domain.first();
    }

}
