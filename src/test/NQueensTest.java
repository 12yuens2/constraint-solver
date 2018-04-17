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
import csp.heuristic.impl.dynamic.LargestDomainFirst;
import csp.heuristic.impl.dynamic.SmallestDomainFirst;
import csp.heuristic.impl.fixed.AscendingStatic;
import csp.heuristic.impl.fixed.DescendingStatic;
import csp.heuristic.impl.fixed.MaximumDegree;
import csp.heuristic.impl.fixed.OddEvenHeuristic;
import csp.heuristic.impl.random.RandomHeuristic;
import csp.heuristic.impl.random.RandomValueHeuristic;
import csp.heuristic.impl.random.RandomVariableHeuristic;
import csp.heuristic.impl.value.LargestValueFirst;
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
    public void ascendingTest() {
        numSolutionsTest(new AscendingStatic());
    }
    
    @Test
    public void descendingTest() {
        numSolutionsTest(new DescendingStatic());
    }
    
    @Test
    public void oddEvenTest() {
        numSolutionsTest(new OddEvenHeuristic());
    }
    
    @Test
    public void sdfHeuristicTest() {
        numSolutionsTest(new SmallestDomainFirst());
    }
    
    @Test
    public void ldfHeuristicTest() {
        numSolutionsTest(new LargestDomainFirst());
    }
    
    
    @Test
    public void maximumDegreeTest() {
        BinaryCSP queensCSP = getCSP("csp/queens/" + fInput + "Queens.csp");
        
        queensCSP.setHeuristic(new MaximumDegree(queensCSP));
        
        ArrayList<Solution> queensSolutions = solver.solveCSP(queensCSP);
        assertEquals(fExpected, queensSolutions.size());
    }
    
    
    @Test
    public void randomHeuristicTest() {
        numSolutionsTest(new RandomHeuristic());
    }
    
    @Test
    public void randomVariableTest() {
        numSolutionsTest(new RandomVariableHeuristic());
    }
    
    @Test
    public void randomValueTest() {
        numSolutionsTest(new RandomValueHeuristic());
    }

    
    private static BinaryCSP getCSP(String filename) {
        return reader.readBinaryCSP(filename);
    }

}