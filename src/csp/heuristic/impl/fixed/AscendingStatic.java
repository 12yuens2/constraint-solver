package csp.heuristic.impl.fixed;

import csp.heuristic.Heuristic;

/**
 * Default where no heuristic is applied.
 * First variable/value in the list is chosen to be assigned. 
 */
public class AscendingStatic extends Heuristic {

    public String toString() {
        return "No heuristic";
    }

}
