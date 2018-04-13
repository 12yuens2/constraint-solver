package solver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import csp.BinaryCSP;
import csp.BinaryTuple;
import csp.Solution;
import csp.Variable;

public class BinaryConstraintSolver {

    
    private BinaryCSP csp;
    private ArrayList<Integer> droppedVals;
    private ArrayList<Solution> solutions;
    private HashMap<Variable, UndoTracker> undoMap;
    
    
	public BinaryConstraintSolver() {
		/* Heuristic/algorithm to choose */
	    this.droppedVals = new ArrayList<>();
	    this.solutions = new ArrayList<>();
	    this.undoMap = new HashMap<>();
	}
	
	
	public ArrayList<Solution> solveCSP(BinaryCSP csp) {
		resetAll(csp);
		forwardChecking(csp.getVariables());
		
		for (Solution s : solutions) {
		    System.out.println(s);
		}
		
		return solutions;
	}
	
	private void resetAll(BinaryCSP csp) {
	    this.csp = csp;
	    
	    droppedVals.clear();
	    solutions.clear();
	    undoMap.clear();
	    for (Variable v : csp.getVariables()) {
	        undoMap.put(v, new UndoTracker());
	    }
	}
	
	private void forwardChecking(ArrayList<Variable> variables) {
	    System.out.println();
	    if (completeAssignment()) {
	        solutions.add(new Solution(csp));
//	        for (int i = 0; i < csp.getNoVariables(); i++) {
//	            Variable v = csp.getVariables().get(i);
//	            System.out.println("Var " + i + ": " + v.getAssignedValue());
//	        }
//	        System.out.println(csp);
//	        System.exit(0);
	    }
	    
		if (variables.isEmpty()) {
	        return;
	    }
	    
	    Variable var = variables.get(0); //selectVar(variables); // select variable to assign
	    int val = var.getDomain().first(); //selectVal(var.getDomain()); // select value from variable domain
	    branchLeft(variables, var, val);
	    branchRight(variables, var, val);
	}
	
	
	private void branchLeft(ArrayList<Variable> variables, Variable var, int val) {
	    var.assignValue(val);
	    
	    if (reviseFutureArcs(variables, var)) {
	        ArrayList<Variable> nextVarList = new ArrayList<>(variables);
	        nextVarList.remove(var);
	        
	        forwardChecking(nextVarList);
	    }
	    undoPruning(var); //reverse changes from reviseFutureArcs()
	    var.unassignValue(val);
	}
	
	private void branchRight(ArrayList<Variable> variables, Variable var, int val) {
	    var.deleteValue(val);
	    
	    if (var.getDomain().size() > 0) {
//	        if (reviseFutureArcs(variables, var)) {
	            forwardChecking(variables);
//	        }
//	        undoPruning(var);
	    }
	    var.restoreValue(val);
	}
	
	private boolean reviseFutureArcs(ArrayList<Variable> variables, Variable var) {
	    System.out.println("Future arcs for: " + var);
	    System.out.println(variables);
	    
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
	        return !reviseConstraint(futureVar, var, var.getAssignedValue());
	    } 
	    else {
	        ArrayList<Integer> toDrop = new ArrayList<>();
	        for (int val : var.getDomain()) {
	            if (reviseConstraint(futureVar, var, val)) {
	                toDrop.add(val);
//	                var.deleteValue(val);
//	                droppedVals.add(val);
	            }
	        }
	        
	        for (int i : toDrop) {
	            var.deleteValue(i);
	            droppedVals.add(i);
	        }
	        
	        return true;
	    }
	    // revise arc by removing values from variable domains
	    // prune domain of futureVar
	    // revision false if one variable has domain wipeout
	}
	
	private boolean reviseConstraint(Variable futureVar, Variable var, int val) {
	    ArrayList<Integer> allowedFutureVals = getAllowedValues(var, futureVar, val);
	    
	    if (allowedFutureVals == null) {
	        /* No constraints between this arc */
	        return false;
	    }
	    
	    if (allowedFutureVals.isEmpty()) {
	        /* Domain wipeout */
	        return true;
	    }

	    ArrayList<Integer> valsToDrop = new ArrayList<>();
	    
	    for (int futureVal : futureVar.getDomain()) {
	        if (!allowedFutureVals.contains(futureVal)) {
	            valsToDrop.add(futureVal);
	        }
	    }
	    
	    futureVar.getDomain().removeAll(valsToDrop);
	   
	    UndoTracker tracker = undoMap.get(var);
	    for (int droppedVal : valsToDrop) {
	        tracker.trackValue(futureVar, droppedVal);
//	        if (droppedFutureVals.containsKey(futureVar)) {
//	            if (!droppedFutureVals.get(futureVar).contains(droppedVal)) {
//	                droppedFutureVals.get(futureVar).add(droppedVal);
//	            }
//	        }
//	        else {
//	            ArrayList<Integer> l = new ArrayList<Integer>();
//	            l.add(droppedVal);
//	            droppedFutureVals.put(futureVar, l);
//	        }
	    }
	    System.out.println("Revise: " + futureVar);
	    
	    if (futureVar.getDomain().isEmpty()) {
	        /* Domain wipeout */
	        System.out.println("wipeout");
	        return true;
	    }
	    
                
        return false;
	}
	
	private ArrayList<Integer> getAllowedValues(Variable v1, Variable v2, int value) {
	    ArrayList<BinaryTuple> constraints = csp.getArcConstraints(v1, v2);
	    ArrayList<Integer> allowedValues = new ArrayList<>();
	   
	    if (constraints.isEmpty()) {
	        return null; //TODO refactor without using null to represent no constraints
	    }
	    
	    /* Add to list of allowed values if first value matches given value */
	    for (BinaryTuple bt : constraints) {
	        if (bt.getVal1() == value) {
	            allowedValues.add(bt.getVal2());
	        }
	    }
	    
	    return allowedValues;
	}
	
	public void undoPruning(Variable var) {
	    for (int i : this.droppedVals) {
	        var.restoreValue(i);
	    }
	    droppedVals.clear();
	   
//	    System.out.println(droppedFutureVals);
	    undoMap.get(var).undo();
	}
	
	private boolean completeAssignment() {
	    for (Variable v : csp.getVariables()) {
	        if (!v.isAssigned()) {
	            return false;
	        }
	    }
	    
	    return true;
	}
}
