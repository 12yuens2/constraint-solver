package csp.heuristic.impl.variable;

import java.util.ArrayList;
import java.util.Random;

import csp.Variable;
import csp.heuristic.Heuristic;

public class RandomVariableHeuristic extends Heuristic {

    private Random random;
    
    public RandomVariableHeuristic() {
        this.random = new Random();
    }
    
    @Override
    public Variable getNextVariable(ArrayList<Variable> variables) {
        int index = random.nextInt(variables.size());
        return variables.get(index);    
    }


    @Override
    public String toString() {
        return "Random variable";
    }
}
