package csp.heuristic.impl.random;

import java.util.ArrayList;
import java.util.Collections;

import csp.BinaryCSP;
import csp.Variable;
import csp.heuristic.Heuristic;

public class RandomStatic extends Heuristic {

    private ArrayList<Integer> staticOrder;
    
    public RandomStatic(BinaryCSP csp) {
        staticOrder = new ArrayList<>();
        for (int i = 0; i < csp.getVariables().size(); i++) {
            staticOrder.add(i);
        }
        Collections.shuffle(staticOrder);
        
        System.out.println(staticOrder);
    }
    
    @Override
    public Variable getNextVariable(ArrayList<Variable> variables) {
        for (int i : staticOrder) {
            for (Variable v : variables) {
                if (v.getId() == i) return v;
            }
        }
       
        System.out.println("oops");
        /* Default, shouldn't reach here */
        return variables.get(0);
    }
    
    @Override
    public String toString() {
        return "Random static";
    }

}
