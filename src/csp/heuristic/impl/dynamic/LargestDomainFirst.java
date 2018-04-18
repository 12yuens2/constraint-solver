package csp.heuristic.impl.dynamic;

import java.util.ArrayList;

import csp.Variable;
import csp.heuristic.Heuristic;

/**
 * Opposite of smallest domain first where the variable whose domain contains the most values left is chosen.
 *
 */
public class LargestDomainFirst extends Heuristic {

    @Override
    public Variable getNextVariable(ArrayList<Variable> variables) {
        Variable largestDomain = variables.get(0);
        for (Variable v : variables) {
            if (v.getDomain().size() > largestDomain.getDomain().size()) {
                largestDomain = v;
            }
        }
        return largestDomain;       
    }
    
    @Override
    public String toString() {
        return "Largest Domain First";
    }

}
