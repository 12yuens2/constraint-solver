package csp.heuristic.impl.random;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

import csp.Variable;
import csp.heuristic.Heuristic;

public class RandomHeuristic extends Heuristic {

    private Random random;
    
    public RandomHeuristic() {
        this.random = new Random();
    }
    
    @Override
    public Variable getNextVariable(ArrayList<Variable> variables) {
        int index = random.nextInt(variables.size());
        return variables.get(index);
    }

    @Override
    public int getNextValue(TreeSet<Integer> domain) {
        int r = random.nextInt(domain.size());
        
        int i = 0;
        for (int val : domain) {
            if (i == r) return val;
            i++;
        }
        
        /* Should never reach here */
        System.out.println("Domain first");
        return domain.first();
    }
    
    @Override
    public String toString() {
        return "Random";
    }

}
