package csp.heuristic.impl.fixed;

import java.util.ArrayList;

import csp.Variable;
import csp.heuristic.Heuristic;

/**
 * Orders the variables by descending order.
 *
 */
public class DescendingStatic extends Heuristic {

    
    @Override
    public Variable getNextVariable(ArrayList<Variable> variables) {
        int lowestId = -1;
        Variable nextVar = variables.get(0);
        
        for (Variable v : variables) {
            if (v.getId() > lowestId) {
                nextVar = v;
            }
        }
        
        return nextVar;
    }
    
    @Override
    public String toString() {
        return "Static Descending";
    }

}
