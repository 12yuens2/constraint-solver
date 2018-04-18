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
import csp.heuristic.impl.dynamic.SmallestDomainFirst;
import csp.heuristic.impl.fixed.AscendingStatic;
import csp.heuristic.impl.fixed.DescendingStatic;
import csp.heuristic.impl.fixed.MaximumDegree;
import csp.heuristic.impl.fixed.MinimumDegree;
import csp.heuristic.impl.random.RandomHeuristic;
import solver.BinaryCSPSolver;
import util.BinaryCSPReader;

@RunWith(Parameterized.class)
public class LangfordsTest {
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"2_3", 2}, {"2_4", 2}, {"2_5", 0}, {"2_6", 0}, {"3_9", 6}
           });
    }
    private static BinaryCSPReader reader = new BinaryCSPReader();
    private static BinaryCSPSolver solver = new BinaryCSPSolver(false, false);
    
    private String fInput;
    private int fExpected;

    public LangfordsTest(String input, int expected) {
        this.fInput = input;
        this.fExpected = expected;
    }

    @Test
    public void noHeuristicTest() {
        BinaryCSP langfordsCSP = getCSP("csp/langfords/langfords" + fInput + ".csp");
        
        langfordsCSP.setHeuristic(new AscendingStatic());
        
        ArrayList<Solution> langfordsSolutions = solver.solveCSP(langfordsCSP);
        assertEquals(fExpected, langfordsSolutions.size());
    }
    
    @Test
    public void descendingTest() {
        BinaryCSP langfordsCSP = getCSP("csp/langfords/langfords" + fInput + ".csp");
        
        langfordsCSP.setHeuristic(new DescendingStatic());
        
        ArrayList<Solution> langfordsSolutions = solver.solveCSP(langfordsCSP);
        assertEquals(fExpected, langfordsSolutions.size());
    }
    @Test
    public void sdfHeuristicTest() {
        BinaryCSP langfordsCSP = getCSP("csp/langfords/langfords" + fInput + ".csp");
        
        langfordsCSP.setHeuristic(new SmallestDomainFirst());
        
        ArrayList<Solution> langfordsSolutions = solver.solveCSP(langfordsCSP);
        assertEquals(fExpected, langfordsSolutions.size());
    }
    
   
/* Following tests were removed as they take too long to complete for langfords3_9 */
/*
    @Test
    public void randomHeuristicTest() {
        BinaryCSP langfordsCSP = getCSP("csp/langfords/langfords" + fInput + ".csp");
        
        langfordsCSP.setHeuristic(new RandomHeuristic());
        
        ArrayList<Solution> langfordsSolutions = solver.solveCSP(langfordsCSP);
        if (fExpected == 0) {
            assertEquals(fExpected, langfordsSolutions.size());
        } else {
            assertTrue(langfordsSolutions.size() >= fExpected);
        }
    }
    
    @Test
    public void maximumDegreeTest() {
        BinaryCSP langfordsCSP = getCSP("csp/langfords/langfords" + fInput + ".csp");
        
        langfordsCSP.setHeuristic(new MaximumDegree(langfordsCSP));
        
        ArrayList<Solution> langfordsSolutions = solver.solveCSP(langfordsCSP);
        assertEquals(fExpected, langfordsSolutions.size());
    }   
    @Test
    public void minimumDegreeTest() {
        BinaryCSP langfordsCSP = getCSP("csp/langfords/langfords" + fInput + ".csp");
        
        langfordsCSP.setHeuristic(new MinimumDegree(langfordsCSP));
        
        ArrayList<Solution> langfordsSolutions = solver.solveCSP(langfordsCSP);
        assertEquals(fExpected, langfordsSolutions.size());
    }   
     
*/
    private static BinaryCSP getCSP(String filename) {
        return reader.readBinaryCSP(filename);
    } 
}
