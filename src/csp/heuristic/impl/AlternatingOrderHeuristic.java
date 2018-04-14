package csp.heuristic.impl;

import java.util.ArrayList;
import java.util.TreeSet;

import csp.Variable;
import csp.heuristic.Heuristic;

public class AlternatingOrderHeuristic implements Heuristic {

    @Override
    public Variable getNextVariable(ArrayList<Variable> variables) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getNextValue(TreeSet<Integer> domain) {
        return domain.first();
    } 
    
    @Override
    public String toString() {
        return "Alternating order";
    }

}
