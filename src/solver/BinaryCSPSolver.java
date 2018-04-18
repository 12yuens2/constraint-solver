package solver;
import java.util.ArrayList;
import java.util.HashMap;

import csp.BinaryCSP;
import csp.BinaryTuple;
import csp.Solution;
import csp.Variable;
import csp.heuristic.Heuristic;
import csp.heuristic.impl.dynamic.LargestDomainFirst;
import csp.heuristic.impl.dynamic.SmallestDomainFirst;
import csp.heuristic.impl.fixed.AscendingStatic;
import csp.heuristic.impl.fixed.DescendingStatic;
import csp.heuristic.impl.fixed.MaximumDegree;
import csp.heuristic.impl.fixed.MinimumDegree;
import csp.heuristic.impl.fixed.OddEvenHeuristic;
import csp.heuristic.impl.random.RandomHeuristic;
import csp.heuristic.impl.random.RandomStatic;
import csp.heuristic.impl.random.RandomValueHeuristic;
import csp.heuristic.impl.random.RandomVariableHeuristic;
import util.BinaryCSPReader;
import util.Logger;

public class BinaryCSPSolver {
     /**
       * Main (for testing)
       */
    public static void main(String[] args) {
        if (args.length != 4) {
          System.out.println("Usage: java BinaryCSPSolver <file.csp> <heuristic> <print-solutions> <debug-mode>") ;
          return ;
        }
        BinaryCSPReader reader = new BinaryCSPReader() ;
    	BinaryCSP csp = reader.readBinaryCSP(args[0]);
    	
    	Heuristic heuristic = getHeuristic(args[1], csp);
    	csp.setHeuristic(heuristic);
    	
    	System.out.println(csp);
    	System.out.println(csp.getHeuristic());
    	
    	BinaryCSPSolver solver = new BinaryCSPSolver(Boolean.valueOf(args[3]), Boolean.valueOf(args[2]));
    	solver.solveCSP(csp);	
    }
    
    public static Heuristic getHeuristic(String heuristicName, BinaryCSP csp) {
        switch (heuristicName) {
        case "sdf":
            return new SmallestDomainFirst();
        case "ldf":
            return new LargestDomainFirst();
        case "ascending":
            return new AscendingStatic();
        case "descending":
            return new DescendingStatic();
        case "max-degree":
            return new MaximumDegree(csp);
        case "min-degree":
            return new MinimumDegree(csp);
        case "odd-even":
            return new OddEvenHeuristic();
        case "random":
            return new RandomHeuristic();
        case "random-static":
            return new RandomStatic(csp);
        case "random-value":
            return new RandomValueHeuristic();
        case "random-variable":
            return new RandomVariableHeuristic();
        default:
            System.out.println("Heursitic " + heuristicName + " unknown. Defaulting to static ascending.");
            return new AscendingStatic();
        }
    }
    
    private BinaryCSP csp;
    private ArrayList<Integer> droppedVals;
    private ArrayList<Solution> solutions;
    private HashMap<Variable, UndoTracker> undoMap;
  
    private boolean allSolutions;
    private boolean printSolutions;
    private long timeTaken;
    private int revisions;
    private int nodes;
    
	public BinaryCSPSolver(boolean debug, boolean printSolutions) {
	    Logger.displayMessage = debug;
	    
	    this.printSolutions = printSolutions;
	    
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

		
		if (printSolutions) {
		    for (Solution s : solutions) {
		        System.out.println("Solution:");
		        System.out.println(s);
		    }
		}
//		Logger.displayMessage = true;
		Logger.newline();
		Logger.log(Logger.MessageType.INFO, "Problem=" + csp.getName());
		Logger.log(Logger.MessageType.INFO, "Heuristic=" + csp.getHeuristic());
		Logger.log(Logger.MessageType.INFO, "Time taken=" + timeTaken);
		Logger.log(Logger.MessageType.INFO, "Search nodes=" + nodes);
		Logger.log(Logger.MessageType.INFO, "Revisions=" + revisions);
		Logger.log(Logger.MessageType.INFO, "Solutions=" + solutions.size());
//		Logger.displayMessage = false;
		return solutions;
	}
	
	private void resetAll(BinaryCSP csp) {
	    this.csp = csp;
	    
	    timeTaken = 0;
	    nodes = 0;
	    revisions = 0;
	    
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
	        return;
	    }
	    
	    else if (variables.isEmpty()) {
		    Logger.log(Logger.MessageType.DEBUG, "No more variables, return up.");
	        return;
	    }
	    
	    else {
            /* New search node */
            
            nodes++;
            Variable var = csp.selectVar(variables);
            int val = csp.selectVal(var); 
            branchLeft(variables, var, val);
            branchRight(variables, var, val);
	    } 
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
	    
	    if (!var.getDomain().isEmpty()) {
	        if (reviseFutureArcs(variables, var)) {
	            forwardChecking(variables);
	        }
	        undoPruning(var);
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
	        return reviseConstraint(futureVar, var, var.getAssignedValue());
	    } 
	    else {
	        ArrayList<Integer> toDrop = new ArrayList<>();
	        for (int val : var.getDomain()) {
	            if (!reviseConstraint(futureVar, var, val)) {
	                toDrop.add(val);
	            }
	        }
	        undoPruning(var);
	       	return !var.getDomain().isEmpty();
	    }
	}
	
	private boolean reviseConstraint(Variable futureVar, Variable var, int val) {
	    revisions++;
	    ArrayList<Integer> allowedFutureVals = getAllowedValues(var, futureVar, val);
	    
	    /* No constraints between this arc */
	    if (allowedFutureVals == null) {
	        Logger.log(Logger.MessageType.WARN, "No constraints found for variables Var" + var.getId() + " and Var" + futureVar.getId());
	        return true;
	    }
	    
	    /* Domain wipeout */
	    if (allowedFutureVals.isEmpty()) {
	        Logger.log(Logger.MessageType.DEBUG, "Domain wipeout for futureVar" + futureVar.getId() + " from constraints");
	        return false;
	    }

	    ArrayList<Integer> valsToDrop = new ArrayList<>();
	    
	    for (int futureVal : futureVar.getDomain()) {
	        if (!allowedFutureVals.contains(futureVal)) {
	            valsToDrop.add(futureVal);
	        }
	    }
	   
	    /* Remove values from future domain, if it is empty, then the arc is not consistent */
	    futureVar.getDomain().removeAll(valsToDrop);

	    UndoTracker tracker = undoMap.get(var);
	    for (int droppedVal : valsToDrop) {
	        tracker.trackValue(futureVar, droppedVal);
	    }
	    Logger.log(Logger.MessageType.DEBUG, "Revise: " + futureVar);
	    
	    
	    /* Domain wipeout */
	    if (futureVar.getDomain().isEmpty()) {
	        Logger.log(Logger.MessageType.DEBUG, "Domain wipeout from rlangfordslangfordsevision");
	        return false;
	    }
        
        return true;
	}
	
	private ArrayList<Integer> getAllowedValues(Variable v1, Variable v2, int value) {
	    ArrayList<BinaryTuple> constraints1 = csp.getArcConstraints(v1, v2);
	    ArrayList<Integer> allowedValues = new ArrayList<>();
	   
	    if (constraints1 == null) {
	        return null;
	    }
	    if (constraints1 != null && constraints1.isEmpty()) {
	        /* No constraints in the arc */
	        return new ArrayList<>();
	    }
	    
	    /* Add to list of allowed values if first value matches given value */
	    if (constraints1 != null) {
            for (BinaryTuple bt : constraints1) {
                if (bt.getVal1() == value) {
                    allowedValues.add(bt.getVal2());
                }
            }
	    }
	    
	    return allowedValues;
	}
	
	public void undoPruning(Variable var) {
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
	
	
	
/*
 * Getters and setters
 */
	
	
	public void setAllSolutions(boolean getAllSolutions) {
	    this.allSolutions = getAllSolutions;
	}
	
    public long getTimeTaken() {
        return timeTaken;
    }

    public int getRevisions() {
        return revisions;
    }

    public int getNodes() {
        return nodes;
    }
    
    public int getNumSolutions() {
        return solutions.size();
    }
    
    public ArrayList<Solution> getSolutions() {
        return solutions;
    }
}
