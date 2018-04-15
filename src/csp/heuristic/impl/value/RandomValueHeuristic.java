package csp.heuristic.impl.value;

import java.util.Random;
import java.util.TreeSet;

import csp.heuristic.Heuristic;

/**
 * Heuristic that chooses a random value from the domain but chooses the first variable.
 *
 */
public class RandomValueHeuristic extends Heuristic {

    private Random random;
    
    public RandomValueHeuristic() {
        this.random = new Random();
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
        return super.getNextValue(domain);
    }

    @Override
    public String toString() {
        return "Random value";
    }

}
