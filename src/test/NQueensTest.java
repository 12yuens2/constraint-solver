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
import csp.heuristic.Heuristic;
import csp.heuristic.impl.LargestValueFirst;
import csp.heuristic.impl.NoHeuristic;
import csp.heuristic.impl.RandomHeuristic;
import csp.heuristic.impl.SmallestDomainFirst;
import solver.BinaryCSPSolver;
import util.BinaryCSPReader;

@RunWith(Parameterized.class)
public class NQueensTest {
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {1, 1}, {2, 0}, {3, 0}, {4, 2}, {5, 10}, {6, 4}, {7, 40}, {8, 92}, {9, 352}, {10, 724}
           });
    }
    private static BinaryCSPReader reader = new BinaryCSPReader();
    private static BinaryCSPSolver solver = new BinaryCSPSolver(true);
    
    private int fInput;
    private int fExpected;

    public NQueensTest(int input, int expected) {
        fInput= input;
        fExpected= expected;
    }
    
    private void numSolutionsTest(Heuristic heuristic) {
        BinaryCSP queensCSP = getCSP("csp/queens/" + fInput + "Queens.csp");
        
        queensCSP.setHeuristic(heuristic);
        
        ArrayList<Solution> queensSolutions = solver.solveCSP(queensCSP);
        assertEquals(fExpected, queensSolutions.size());
    }

    @Test
    public void noHeuristicTest() {
        numSolutionsTest(new NoHeuristic());
    }
    
    @Test
    public void sdfHeuristicTest() {
        numSolutionsTest(new SmallestDomainFirst());
    }
    
    @Test
    public void randomHeuristicTest() {
        numSolutionsTest(new RandomHeuristic());
    }
    
    @Test
    public void largestValueHeuristicTest() {
        numSolutionsTest(new LargestValueFirst());
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