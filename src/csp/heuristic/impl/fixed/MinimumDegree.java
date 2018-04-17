package csp.heuristic.impl.fixed;

import java.util.ArrayList;

import csp.BinaryCSP;
import csp.Variable;

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
