package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import csp.BinaryCSP;
import csp.Solution;
import csp.Variable;
import csp.heuristic.impl.dynamic.SmallestDomainFirst;
import solver.BinaryCSPSolver;
import util.BinaryCSPReader;


public class FourQueensTest {

    private static BinaryCSPReader reader = new BinaryCSPReader();
    private static BinaryCSPSolver solver = new BinaryCSPSolver(false, false);
   
    private ArrayList<Solution> solutions;

    public FourQueensTest() {
        BinaryCSP fourQueensCSP = getCSP("csp/queens/4Queens.csp");
        fourQueensCSP.setHeuristic(new SmallestDomainFirst());
        this.solutions = solver.solveCSP(fourQueensCSP); 
    }
    
    @Test
    public void testSol1() {
        /* First solution */
        Solution s1 = solutions.get(0);
        ArrayList<Variable> rows = getQueensRows(s1, 4);
        assertEquals(1, s1.getVarValue(rows.get(0)));
        assertEquals(3, s1.getVarValue(rows.get(1)));
        assertEquals(0, s1.getVarValue(rows.get(2)));
        assertEquals(2, s1.getVarValue(rows.get(3)));
      
    }
    
    @Test
    public void testSol2() {
        /* Second solution */
        Solution s2 = solutions.get(1);
        ArrayList<Variable >rows = getQueensRows(s2, 4);
        assertEquals(2, s2.getVarValue(rows.get(0)));
        assertEquals(0, s2.getVarValue(rows.get(1)));
        assertEquals(3, s2.getVarValue(rows.get(2)));
        assertEquals(1, s2.getVarValue(rows.get(3)));
    }

    
    
        
    private static BinaryCSP getCSP(String filename) {
        return reader.readBinaryCSP(filename);
    }
    
    private static ArrayList<Variable> getQueensRows(Solution s, int problemSize) {
        ArrayList<Variable> rows = new ArrayList<>();
        
        for (int i = 0; i < problemSize; i++) {
            rows.add(s.getVariable(i));
        }
        
        return rows;
    }
}
