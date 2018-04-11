package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import csp.Variable;

public class UndoTracker {

    private HashMap<Variable, ArrayList<Integer>> undos;
    
    public UndoTracker() {
        this.undos = new HashMap<>();
    }
    
    public Set<Variable> getUndoVars() {
        return undos.keySet();
    }
    
    public ArrayList<Integer> getUndoVals(Variable key) {
        return undos.get(key);
    }
    
    public void trackValue(Variable var, int val) {
        if (undos.containsKey(var)) {
            undos.get(var).add(val);
        } else {
            undos.put(var, new ArrayList<>(Arrays.asList(val)));
        }
    }
    
    public void undo() {
        for (Entry<Variable, ArrayList<Integer>> entry : undos.entrySet()) {
            Variable v = entry.getKey();
            for (int val : entry.getValue()) {
                v.restoreValue(val);
            }
        }
        undos.clear();
    }
    
}
