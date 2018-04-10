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
	    
	    if (var.getDomain().size() > 0) {
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
//	    if (var.isAssigned()) {
//	        reviseConstraint(futureVar, var, var.assignedVal());
//	    } else {
	        for (int val : var.getDomain()) {
	            if (reviseConstraint(futureVar, var, val)) return false;
	        }
	        
	        return true;
	    //}
	    // revise arc by removing values from variable domains
	    // prune domain of futureVar
	    // revision false if one variable has domain wipeout
	}
	
	private boolean reviseConstraint(Variable futureVar, Variable var, int val) {
	    ArrayList<Integer> allowedFutureVals = getAllowedValues(futureVar, var, val);
	    
	    if (allowedFutureVals.isEmpty()) {
	        /* Domain wipeout */
	        return true;
	    }
	    
        for (int futureVal : allowedFutureVals) {
            if (!futureVar.getDomain().contains(futureVal)) {
                // value not supported by futureVar
                // keep track of dropped vals to undo later TODO
                var.dropVal(val);
            }
        }
        
        return false;
	}
	
	private ArrayList<Integer> getAllowedValues(Variable v1, Variable v2, int value) {
	    ArrayList<BinaryTuple> constraints = csp.getArcConstraints(v1, v2);
	    ArrayList<Integer> allowedValues = new ArrayList<>();
	    
	    /* Add to list of allowed values if first value matches given value */
	    for (BinaryTuple bt : constraints) {
	        if (bt.getVal1() == value) {
	            allowedValues.add(bt.getVal2());
	        }
	    }
	    
	    return allowedValues;
	}
	
	private boolean completeAssignment() {
	    return false;
	}
}
