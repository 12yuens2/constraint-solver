package csp.heuristic;

import java.util.ArrayList;
import java.util.TreeSet;

import csp.Variable;

public interface Heuristic {

    /**
     * Get the next variable to be chosen for assignment
     * @param variables - List of variables to choose from
     * @return chosen variable
     */
    public abstract Variable getNextVariable(ArrayList<Variable> variables);

    
    /**
     * Select the next value to assign to the variable
     * @param domain - Domain the value can be chosen from
     * @return chosen value
     */
    public abstract int getNextVariable(TreeSet<Integer> domain);
    
}
