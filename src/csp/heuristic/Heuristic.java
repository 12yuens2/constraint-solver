package csp.heuristic;

import java.util.ArrayList;
import java.util.TreeSet;

import csp.Variable;

public abstract class Heuristic {

    /**
     * Get the next variable to be chosen for assignment
     * @param variables - List of variables to choose from
     * @return chosen variable
     */
    public Variable getNextVariable(ArrayList<Variable> variables) {
        return variables.get(0);
    }

    
    /**
     * Select the next value to assign to the variable
     * @param domain - Domain the value can be chosen from
     * @return chosen value
     */
    public int getNextValue(TreeSet<Integer> domain) {
        return domain.first();
    }
    
    /**
     * Override toString to only return name of the heuristic
     * @return
     */
    public abstract String toString();
}
