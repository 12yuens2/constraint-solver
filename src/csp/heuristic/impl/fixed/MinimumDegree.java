package csp.heuristic.impl.fixed;

import java.util.ArrayList;

import csp.BinaryCSP;
import csp.Variable;

/**
 * The opposite of Maximum degree ordering where the variable with the least number of constraints is chosen.
 *
 */
public class MinimumDegree extends MaximumDegree {

    public MinimumDegree(BinaryCSP csp) {
        super(csp);
    }
    
    @Override
    public Variable getNextVariable(ArrayList<Variable> variables) {
        Variable leastConstrained = variables.get(0);
        int constraints = primalGraph.get(leastConstrained);
        for (Variable v : variables) {
            if (primalGraph.get(v) < constraints) {
                leastConstrained = v;
                constraints = primalGraph.get(v);
            }
        }
        
        return leastConstrained;
    }
    
    @Override
    public String toString() {
        return "Minimum degree";
    }

}
