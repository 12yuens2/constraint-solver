package csp.heuristic.impl.fixed;

import java.util.ArrayList;

import csp.Variable;
import csp.heuristic.Heuristic;

/**
 * Returns odd numbered variables first before even numbered variables.
 *
 */
public class OddEvenHeuristic extends Heuristic {

    @Override
    public Variable getNextVariable(ArrayList<Variable> variables) {
        Variable lowestOdd = null;
        Variable lowestEven = null;
        
        for (Variable v : variables) {
            int id = v.getId();
            if (id % 2 == 1) {
                if (lowestOdd == null) lowestOdd = v;
                else if (id < lowestOdd.getId()) lowestOdd = v;
            }
            else {
                if (lowestEven == null) lowestEven = v;
                else if (id < lowestEven.getId()) lowestEven = v;
            }
        }
        
        return lowestOdd != null ? lowestOdd : lowestEven;
    }
    
    @Override
    public String toString() {
        return "Odd even";
    }

}
