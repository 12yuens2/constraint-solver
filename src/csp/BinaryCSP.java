package csp;
import java.util.* ;

public final class BinaryCSP {
    private ArrayList<Variable> variables;
    private ArrayList<BinaryConstraint> constraints;
  
    public BinaryCSP(int[][] db, ArrayList<BinaryConstraint> c) {
        this.variables = new ArrayList<>();
        for (int i = 0; i < db.length; i++) {
            Variable v = new Variable(i, db[i][0], db[i][1]);
            variables.add(v);
        }
        
        this.constraints = c;
    }
    
    public ArrayList<BinaryTuple> getArcConstraints(Variable v1, Variable v2) {
        for (BinaryConstraint bc : constraints) {

            if (bc.getFirstVar() == v1.getId() && bc.getSecondVar() == v2.getId()) {
                return bc.getTuples();
            }
        }
       
        /* No constaints found for arc(v1,v2) */
        return new ArrayList<>();
    }
  
    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("CSP:\n");
        
        for (Variable v : variables) {
            result.append("Var " + v.getId() + ": " + v.getDomain() + "\n");
        }
        
//        for (BinaryConstraint bc : constraints) {
//            result.append(bc+"\n");
//        }
        
        return result.toString();
    }
 
    public ArrayList<Variable> getVariables() {
        return this.variables;
    }
    
    public int getNoVariables() {
        return variables.size();
    }
  
    public int getLB(int varIndex) {
        return variables.get(varIndex).getDomainLB();
    }
  
    public int getUB(int varIndex) {
        return variables.get(varIndex).getDomainUB();
    }
  
    public ArrayList<BinaryConstraint> getConstraints() {
        return constraints;
    }
}
