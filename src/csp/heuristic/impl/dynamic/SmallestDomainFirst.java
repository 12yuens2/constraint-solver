package csp.heuristic.impl.dynamic;

import java.util.ArrayList;
import java.util.TreeSet;

import csp.Variable;
import csp.heuristic.Heuristic;

/**
 * The smallest domain first heuristic chooses the variable whose domain contains the least number of values left.
 *
 */
public class SmallestDomainFirst extends Heuristic {

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
    
    public String toString() {
        return "Smallest Domain First";
    }

}
