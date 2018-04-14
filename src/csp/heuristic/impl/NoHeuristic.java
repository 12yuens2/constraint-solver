package csp.heuristic.impl;

import java.util.ArrayList;
import java.util.TreeSet;

import csp.Variable;
import csp.heuristic.Heuristic;

/**
 * Default where no heuristic is applied.
 * First variable/value in the list is chosen to be assigned. 
 */
public class NoHeuristic implements Heuristic {

    public Variable getNextVariable(ArrayList<Variable> variables) {
        if (!variables.isEmpty()) {
            return variables.get(0);
        }
        
        //TODO exception
        return null;
    }

    public int getNextValue(TreeSet<Integer> domain) {
        return domain.first();
    }
    
    public String toString() {
        return "No heuristic";
    }

}
