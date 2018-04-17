package csp.heuristic.impl.fixed;

import java.util.ArrayList;
import java.util.HashMap;

import csp.BinaryCSP;
import csp.BinaryTuple;
import csp.Variable;
import csp.heuristic.Heuristic;

public class MaximumDegree extends Heuristic {

    protected HashMap<Variable, Integer> primalGraph;
    
    public MaximumDegree(BinaryCSP csp) {
        this.primalGraph = new HashMap<>();
        
        for (Variable v1 : csp.getVariables()) {
            int constraints = 0;
            for (Variable v2 : csp.getVariables()) {
                ArrayList<BinaryTuple> arcConstraints = csp.getArcConstraints(v1, v2);
                if (arcConstraints != null) {
                    constraints += (v1.getDomain().size() * v2.getDomain().size()) - arcConstraints.size();
                }
            }
            primalGraph.put(v1, constraints);
        }
    }
    
    @Override
    public Variable getNextVariable(ArrayList<Variable> variables) {
        Variable mostConstrained = variables.get(0);
        int constraints = primalGraph.get(mostConstrained);
        for (Variable v : variables) {
            if (primalGraph.get(v) > constraints) {
                mostConstrained = v;
                constraints = primalGraph.get(v);
            }
        }
        
        return mostConstrained;
    }
    
    @Override
    public String toString() {
        return "Maximum Degree";
    }

}
