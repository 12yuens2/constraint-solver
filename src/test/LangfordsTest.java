package test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import csp.BinaryCSP;
import csp.Solution;
import csp.heuristic.impl.NoHeuristic;
import csp.heuristic.impl.RandomHeuristic;
import csp.heuristic.impl.variable.SmallestDomainFirst;
import solver.BinaryCSPSolver;
import util.BinaryCSPReader;

@RunWith(Parameterized.class)
public class LangfordsTest {
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"2_3", 16}, {"2_4", 32}, {"2_5", 0}, {"2_6", 0}
           });
    }
    private static BinaryCSPReader reader = new BinaryCSPReader();
    private static BinaryCSPSolver solver = new BinaryCSPSolver(true);
    
    private String fInput;
    private int fExpected;

    public LangfordsTest(String input, int expected) {
        this.fInput = input;
        this.fExpected = expected;
    }

    @Test
    public void noHeuristicTest() {
        BinaryCSP langfordsCSP = getCSP("csp/langfords/langfords" + fInput + ".csp");
        
        langfordsCSP.setHeuristic(new NoHeuristic());
        
        ArrayList<Solution> queensSolutions = solver.solveCSP(langfordsCSP);
        if (fExpected == 0) {
            assertEquals(fExpected, queensSolutions.size());
        } else {
            assertTrue(queensSolutions.size() >= fExpected);
        }
    }
    
    @Test
    public void sdfHeuristicTest() {
        BinaryCSP langfordsCSP = getCSP("csp/langfords/langfords" + fInput + ".csp");
        
        langfordsCSP.setHeuristic(new SmallestDomainFirst());
        
        ArrayList<Solution> queensSolutions = solver.solveCSP(langfordsCSP);
        if (fExpected == 0) {
            assertEquals(fExpected, queensSolutions.size());
        } else {
            assertTrue(queensSolutions.size() >= fExpected);
        }
    }
    
    
    @Test
    public void randomHeuristicTest() {
        BinaryCSP langfordsCSP = getCSP("csp/langfords/langfords" + fInput + ".csp");
        
        langfordsCSP.setHeuristic(new RandomHeuristic());
        
        ArrayList<Solution> queensSolutions = solver.solveCSP(langfordsCSP);
        if (fExpected == 0) {
            assertEquals(fExpected, queensSolutions.size());
        } else {
            assertTrue(queensSolutions.size() >= fExpected);
        }
    }
    
    
    private static BinaryCSP getCSP(String filename) {
        return reader.readBinaryCSP(filename);
    } 
}
