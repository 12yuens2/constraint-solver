import java.util.ArrayList;

public class BinaryConstraintSolver {

    
    private BinaryCSP csp;
    
	public BinaryConstraintSolver() {
		/* Heuristic/algorithm to choose */
	}
	
	
	public void solveCSP(BinaryCSP csp) {
		this.csp = csp;
	}
	
	private void forwardChecking(ArrayList<Variable> variables) {
	    if (completeAssignment()) {
	        return;
	    }
	    
	    Variable var = selectVar(variables); // select variable to assign
	    int val = selectVal(var.getDomain()); // select value from variable domain
	    branchLeft(variables, var, val);
	    branchRight(variables, var, val);
	}
	
	
	private void branchLeft(ArrayList<Variable> variables, Variable var, int val) {
	    //assign(var, val); //assign value to variable
	    var.assignValue(val);
	    
	    if (reviseFutureArcs(variables, var)) {
	        ArrayList<Variable> nextVarList = new ArrayList<>(variables);
	        nextVarList.remove(var);
	        
	        forwardChecking(nextVarList);
	    }
	    undoPruning(); //reverse changes from reviseFutureArcs()
	    unassign(var, val);
	}
	
	private void branchRight(ArrayList<Variable> variables, Variable var, int val) {
	    var.deleteValue(val);
	    
	    if (var.getDomain().length > 0) {
	        if (reviseFutureArcs(variables, var)) {
	            forwardChecking(variables);
	        }
	        undoPruning();
	    }
	    var.restoreValue(val);
	}
	
	private boolean reviseFutureArcs(ArrayList<Variable> variables, Variable var) {
	    boolean consistent = true;
	    for (Variable futureVar : variables) {
	        if (futureVar != var) {
	            consistent = revise(futureVar, var);
	            
	            if (!consistent) return false;
	        }
	    }
	    return true;
	}
	
	private boolean revise(Variable futureVar, Variable var) {
	    //csp.getcon
	    if (var.isAssigned()) {
	        reviseConstraint(futureVar, var, var.assignedVal());
	    } else {
	        for (int val : var.getDomain()) {
	            reviseConstraint(futureVar, var, val);
	        }
	    }
	    // revise arc by removing values from variable domains
	    // prune domain of futureVar
	    // revision false if one variable has domain wipeout
	}
	
	private void reviseConstraint(Variable futureVar, Variable var, int val) {
	    constraints.get(val) = allowedFutureVals;
        for (int futureVal : allowedFutureVals) {
            if (!futureVar.getDomain().contains(futureVal)) {
                // value not supported by futureVar
                var.dropVal(val);
            }
        }    
	}
	
	private boolean completeAssignment() {
	    return false;
	}
}
