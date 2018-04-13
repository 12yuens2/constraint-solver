package csp;

import java.util.HashMap;
import java.util.Map.Entry;

public class Solution {

    HashMap<Variable, Integer> varMap;
    
    public Solution() {
        this.varMap = new HashMap<>();
    }
    
    public Solution(BinaryCSP solvedCSP) {
        this();
        for (Variable v : solvedCSP.getVariables()) {
            varMap.put(v, v.getAssignedValue());
        }
    }
    
    public HashMap<Variable, Integer> getSolution() {
        return this.varMap;
    }
    
    public Variable getVariable(int varId) {
        for (Entry<Variable, Integer> entry : varMap.entrySet()) {
            Variable v = entry.getKey();
            if (v.getId() == varId) return v;
        }
        //TODO use exception?
        return null;
    }
    
    public int getVarValue(Variable v) {
        return varMap.get(v);
    }
    
    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        
        for (Entry<Variable, Integer> entry : varMap.entrySet()) {
            Variable v = entry.getKey();
            result.append("Var " + v.getId() + ": " + entry.getValue() + "\n");
        }
        
        return result.toString();
    }
    
}
