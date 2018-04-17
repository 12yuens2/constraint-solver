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
import csp.heuristic.Heuristic;
import csp.heuristic.impl.dynamic.SmallestDomainFirst;
import csp.heuristic.impl.fixed.AscendingStatic;
import csp.heuristic.impl.fixed.MaximumDegree;
import solver.BinaryCSPSolver;
import util.BinaryCSPReader;

@RunWith(Parameterized.class)
public class SudokuTest {
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            /*{"FinnishSudoku.csp", 1},*/ {"SimonisSudoku.csp", 1}
        });
    }
    
    private static BinaryCSPReader reader = new BinaryCSPReader();
    private static BinaryCSPSolver solver = new BinaryCSPSolver(true);
    
    private String fInput;
    private int fExpected;

    public SudokuTest(String input, int expected) {
        fInput= input;
        fExpected= expected;
    }


    @Test
    public void sdfHeursiticTest() {
        BinaryCSP sudokuCSP = reader.readBinaryCSP("csp/sudoku/" + fInput);
        sudokuCSP.setHeuristic(new SmallestDomainFirst());
        
        ArrayList<Solution> solutions = solver.solveCSP(sudokuCSP);
        assertEquals(fExpected, solutions.size());
    }
    
    
    @Test
    public void noHeursiticTest() {
        BinaryCSP sudokuCSP = reader.readBinaryCSP("csp/sudoku/" + fInput);
        sudokuCSP.setHeuristic(new AscendingStatic());
        
        ArrayList<Solution> solutions = solver.solveCSP(sudokuCSP);
        assertEquals(fExpected, solutions.size());
    }
    
    
//    @Test
//    public void MaximumDegreeTest() {
//        BinaryCSP sudokuCSP = reader.readBinaryCSP("csp/sudoku/" + fInput);
//        sudokuCSP.setHeuristic(new MaximumDegree(sudokuCSP));
//        
//        ArrayList<Solution> solutions = solver.solveCSP(sudokuCSP);
//        assertEquals(fExpected, solutions.size());
//    }
    
}
