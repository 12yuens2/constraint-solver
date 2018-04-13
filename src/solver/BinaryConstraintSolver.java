package solver;
import java.util.ArrayList;
import java.util.HashMap;

import csp.BinaryCSP;
import csp.BinaryTuple;
import csp.Solution;
import csp.Variable;
import util.Logger;

public class BinaryConstraintSolver {
    
    private BinaryCSP csp;
    private ArrayList<Integer> droppedVals;
    private ArrayList<Solution> solutions;
    private HashMap<Variable, UndoTracker> undoMap;
  
    private long timeTaken;
    private int revisions;
    private int nodes;
    
	public BinaryConstraintSolver() {
	    //Logger.displayMessage = true;
	    
		/* Heuristic/algorithm to choose */
	    this.droppedVals = new ArrayList<>();
	    this.solutions = new ArrayList<>();
	    this.undoMap = new HashMap<>();
	}
	
	
	public ArrayList<Solution> solveCSP(BinaryCSP csp) {
		resetAll(csp);
		long timeBefore = System.nanoTime();
		forwardChecking(csp.getVariables());
		long timeAfter = System.nanoTime();
		
		timeTaken = timeAfter - timeBefore;
		
//		for (Solution s : solutions) {
//		    System.out.println(s);
//		}
		Logger.displayMessage = true;
		Logger.newline();
		Logger.log(Logger.MessageType.DEBUG, "Time taken=" + timeTaken);
		Logger.log(Logger.MessageType.DEBUG, "Search nodes=" + nodes);
		Logger.log(Logger.MessageType.DEBUG, "Revisions=" + revisions);

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
	    Logger.newline();
	    Logger.log(Logger.MessageType.DEBUG, "Begin forward checking");
	    
	    if (completeAssignment()) {
	        Logger.log(Logger.MessageType.DEBUG, "Found solution.");
	        solutions.add(new Solution(csp));
	    }
	    
		if (variables.isEmpty()) {
	        return;
	    }
		
		/* New search node */
		nodes++;
	    
	    Variable var = csp.selectVar(variables);//variables.get(0); //selectVar(variables); // select variable to assign
	    int val = csp.selectVal(var); //var.getDomain().first(); //selectVal(var.getDomain()); // select value from variable domain
	    branchLeft(variables, var, val);
	    branchRight(variables, var, val);
	}
	

	
	private void branchLeft(ArrayList<Variable> variables, Variable var, int val) {
	    nodes++;
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
	    nodes++;
	    var.deleteValue(val);
	    
	    if (var.getDomain().size() > 0) {
	        //if (reviseFutureArcs(variables, var)) {
	            forwardChecking(variables);
	        //}
	        //undoPruning(var);
	    }
	    var.restoreValue(val);
	}
	
	private boolean reviseFutureArcs(ArrayList<Variable> variables, Variable var) {
	    Logger.log(Logger.MessageType.DEBUG, "Future arcs for: " + var);
	    Logger.log(Logger.MessageType.DEBUG, "Variables: " + variables);
	    
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
	    if (var.isAssigned()) {
	        return !reviseConstraint(futureVar, var, var.getAssignedValue());
	    } 
	    else {
	        ArrayList<Integer> toDrop = new ArrayList<>();
	        for (int val : var.getDomain()) {
	            if (reviseConstraint(futureVar, var, val)) {
	                toDrop.add(val);
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
	    revisions++;
	    ArrayList<Integer> allowedFutureVals = getAllowedValues(var, futureVar, val);
	    
	    /* No constraints between this arc */
	    if (allowedFutureVals == null) {
	        Logger.log(Logger.MessageType.WARN, "No constraints found for variables Var" + var.getId() + " and Var" + futureVar.getId());
	        return false;
	    }
	    
	    /* Domain wipeout */
	    if (allowedFutureVals.isEmpty()) {
	        Logger.log(Logger.MessageType.DEBUG, "Domain wipeout from constraints");
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
	    }
	    Logger.log(Logger.MessageType.DEBUG, "Revise: " + futureVar);
	    
	    
	    /* Domain wipeout */
	    if (futureVar.getDomain().isEmpty()) {
	        Logger.log(Logger.MessageType.DEBUG, "Domain wipeout from revision");
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
