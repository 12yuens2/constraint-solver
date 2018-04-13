package csp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

import util.Logger;
import util.SortedArrayList;

public class Variable {

	//private int domainLB;
	//private int domainUB;
    private int id;
	private TreeSet<Integer> domain; // make domain a sorted arraylist so LB and UB always at 0 and size() indices?

	private boolean isAssigned;
	private int assignedValue;
	
	
	public Variable(int id, int domainLB, int domainUB) {
		this.id = id;
		
		this.domain = new TreeSet<>();
		
		//TODO dont need i
		for (int i = 0, j = domainLB; j <= domainUB; i++, j++) {
		    this.domain.add(j);
		}
		
		this.isAssigned = false;
		this.assignedValue = 0;
	}
	
	@Override 
	public boolean equals(Object other) {
	    Variable otherVar = (Variable) other;
	    return this.id == otherVar.id;
	}
	
	@Override
	public String toString() {
	    return "Var " + id + ": " + domain;
	}
	
	public int getId() {
	    return this.id;
	}
	
	public void setId(int id) {
	    this.id = id;
	}

	public int getDomainLB() {
	    return this.domain.first();
	}

	public int getDomainUB() {
	    return this.domain.last();
	}

	public TreeSet<Integer> getDomain() {
		return this.domain;
	}
	
	public int getAssignedValue() {
	    return this.assignedValue;
	}

    public boolean dropVal(int val) {
        /* Need to convert to integer because remove(int) removes by index */
        return this.domain.remove(new Integer(val));
    }

    

/* 
 * Variable assignment 
 */
    public void assignValue(int val) {
        Logger.log(Logger.MessageType.DEBUG, "Assign Var" + id + " with " + val);
        this.assignedValue = val;
        this.isAssigned = true;
    }
    
    public void unassignValue(int val) {
        this.assignedValue = -1;
        this.isAssigned = false;
    }
    
    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }
    
    public boolean isAssigned() {
        return isAssigned;
    }
   
    /**
     * Remove value from domain.
     * @param val value to remove.
     * @return false if value is not in domain, true otherwise.
     */
    public boolean deleteValue(int val) {
        if (this.domain.contains(val)) {
            this.domain.remove(new Integer(val));
            return true;
        } 
        else {
            return false;
        }
    }
   
    /**
     * Add value back to domain.
     * @param val value to add.
     */
    public void restoreValue(int val) {
        this.domain.add(val);
    }
	
}
