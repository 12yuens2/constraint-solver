package test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import csp.BinaryCSP;
import csp.Solution;
import csp.Variable;
import solver.BinaryConstraintSolver;
import util.BinaryCSPReader;

@RunWith(Parameterized.class)
public class NQueensTest {
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {4, 2}, {6, 4}, {8, 92}, {10, 724}
           });
    }
    private static BinaryCSPReader reader = new BinaryCSPReader();
    private static BinaryConstraintSolver solver = new BinaryConstraintSolver();
    
    private int fInput;
    private int fExpected;

    public NQueensTest(int input, int expected) {
        fInput= input;
        fExpected= expected;
    }

    @Test
    public void test() {
        BinaryCSP queensCSP = getCSP("csp/" + fInput + "Queens.csp");
        ArrayList<Solution> queensSolutions = solver.solveCSP(queensCSP);
        
        assertEquals(fExpected, queensSolutions.size());
    }
        
//    @Test
//    public void test4Queens() {
//        BinaryCSP fourQueensCSP = getCSP("csp/4Queens.csp");
//        ArrayList<Solution> fourQueensSolutions = solver.solveCSP(fourQueensCSP);
//       
//        /* First solution */
//        Solution s1 = fourQueensSolutions.get(0);
//        ArrayList<Variable> rows = getQueensRows(s1, 4);
//        assertEquals(1, s1.getVarValue(rows.get(0)));
//        assertEquals(3, s1.getVarValue(rows.get(1)));
//        assertEquals(0, s1.getVarValue(rows.get(2)));
//        assertEquals(2, s1.getVarValue(rows.get(3)));
//        
//        /* Second solution */
//        Solution s2 = fourQueensSolutions.get(1);
//        rows = getQueensRows(s1, 4);
//        assertEquals(2, s2.getVarValue(rows.get(0)));
//        assertEquals(0, s2.getVarValue(rows.get(1)));
//        assertEquals(3, s2.getVarValue(rows.get(2)));
//        assertEquals(1, s2.getVarValue(rows.get(3)));
//    }
    
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