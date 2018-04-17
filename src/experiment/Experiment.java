package experiment;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import csp.BinaryCSP;
import csp.BinaryConstraint;
import csp.heuristic.Heuristic;
import csp.heuristic.impl.dynamic.LargestDomainFirst;
import csp.heuristic.impl.dynamic.SmallestDomainFirst;
import csp.heuristic.impl.fixed.AscendingStatic;
import csp.heuristic.impl.fixed.DescendingStatic;
import csp.heuristic.impl.fixed.MaximumDegree;
import csp.heuristic.impl.fixed.MinimumDegree;
import csp.heuristic.impl.fixed.OddEvenHeuristic;
import csp.heuristic.impl.random.RandomHeuristic;
import csp.heuristic.impl.random.RandomStatic;
import csp.heuristic.impl.random.RandomValueHeuristic;
import csp.heuristic.impl.random.RandomVariableHeuristic;
import csp.heuristic.impl.value.LargestValueFirst;
import solver.BinaryCSPSolver;
import util.BinaryCSPReader;
import util.CSVWriter;

public class Experiment {
   

    /**
     * Main for running experiments
     */
    public static void main(String[] args) {
        
        ArrayList<Heuristic> heuristics = new ArrayList<>();
        heuristics.add(new AscendingStatic());
        heuristics.add(new DescendingStatic());
        heuristics.add(new SmallestDomainFirst());
        heuristics.add(new LargestDomainFirst());
        heuristics.add(new OddEvenHeuristic());
        
        heuristics.add(new LargestValueFirst());
        
        ArrayList<Heuristic> randomHeuristics = new ArrayList<>();
        randomHeuristics.add(new RandomHeuristic());
        randomHeuristics.add(new RandomVariableHeuristic());
        randomHeuristics.add(new RandomValueHeuristic());
        
        Experiment ex = new Experiment("langfords", "langfords.csv", true, heuristics, randomHeuristics);
        ex.run(200);
        ex.writeResults();
    }
   
    private BinaryCSPReader reader;
    private BinaryCSPSolver solver;
    private CSVWriter writer;
   
    private String problemName;
    private ArrayList<Result> results;
    private ArrayList<Heuristic> heuristics;
    private ArrayList<Heuristic> randomHeuristics;
    private ArrayList<String> instances;
    
    public Experiment(String problemName, String outputFile, boolean allSolutions, 
            ArrayList<Heuristic> heuristics, ArrayList<Heuristic> randomHeuristics) {
        this.writer = new CSVWriter(outputFile);
        this.reader = new BinaryCSPReader();
        this.solver = new BinaryCSPSolver(allSolutions);
        
        this.results = new ArrayList<>();
        
        this.problemName = problemName;
        this.heuristics = heuristics;
        this.randomHeuristics = randomHeuristics;
        this.instances = Experiment.getCSPFiles(problemName);
    }
    
    public void run(int runs) {
        for (String instanceName : instances) {
            System.out.println("Run instance " + instanceName);
            BinaryCSP csp = reader.readBinaryCSP(BinaryCSPReader.CSP_PATH + problemName + "/" + instanceName);
           
            /* Various heuristics */
            for (Heuristic heuristic : heuristics) {
                results.add(runExperiments(csp, heuristic, runs));
            }
            results.add(runExperiments(csp, new MaximumDegree(csp), runs));
            results.add(runExperiments(csp, new MinimumDegree(csp), runs));
            
            
            /* Random heuristics */
            results.add(runExperimentRandomStatic(csp, runs));
            
            for (Heuristic randomHeuristic : randomHeuristics) {
                results.add(runExperiments(csp, randomHeuristic, runs));
            }
            
        }
    }
    
    public void writeResults() {
        writer.writeRow("Instance", "Heuristic", "SolverTime", "SolverNodes", "ArcRevisions", "NumSolutions");
        for (Result result : results) {
            writer.writeRow(result.getInstanceName(), 
                            result.getHeuristic(), 
                            result.getSolverTime(), 
                            result.getSolverNodes(), 
                            result.getArcRevisions(), 
                            result.getNumSolutions());
        }
        writer.close();
    }
    
    private Result runExperiments(BinaryCSP csp, Heuristic heuristic, int runs) {
        System.out.println("Run heuristic: " + heuristic.toString());
        csp.setHeuristic(heuristic);
        
        double totalTimeTaken = 0.0;
        int totalNodes = 0;
        int totalRevisions = 0;
        
        for (int i = 0; i < runs; i++) {
            System.out.println("Run experiment iteration " + i);
            solver.solveCSP(csp);
            
            totalTimeTaken += solver.getTimeTaken() / 1000000.0;
            totalNodes += solver.getNodes();
            totalRevisions += solver.getRevisions();
   
        }   
        
        double timeTaken = totalTimeTaken / runs;
        int solverNodes = totalNodes / runs;
        int arcRevisions = totalRevisions / runs;
        int numSolutions = solver.getNumSolutions();
        
        return new Result(csp.getName().replaceAll("[^0-9]", ""), csp.getHeuristic().toString(), timeTaken, solverNodes, arcRevisions, numSolutions);
    }
    
    private Result runExperimentRandomStatic(BinaryCSP csp, int runs) {
        Heuristic heuristic = new RandomStatic(csp);
        System.out.println("Run heuristic: " + heuristic.toString());
        csp.setHeuristic(heuristic);
        
        double totalTimeTaken = 0.0;
        int totalNodes = 0;
        int totalRevisions = 0;
        
        for (int i = 0; i < runs; i++) {
            System.out.println("Run experiment iteration " + i);
            solver.solveCSP(csp);
            
            totalTimeTaken += solver.getTimeTaken() / 1000000.0;
            totalNodes += solver.getNodes();
            totalRevisions += solver.getRevisions();
   
        }   
        
        double timeTaken = totalTimeTaken / runs;
        int solverNodes = totalNodes / runs;
        int arcRevisions = totalRevisions / runs;
        int numSolutions = solver.getNumSolutions();
        
        return new Result(csp.getName().replaceAll("[^0-9]", ""), csp.getHeuristic().toString(), timeTaken, solverNodes, arcRevisions, numSolutions);
     
    }
    
    
    public static ArrayList<String> getCSPFiles(String problemName) {
        ArrayList<String> instanceNames = new ArrayList<>();
        
        File problemFolder = new File(BinaryCSPReader.CSP_PATH + problemName);
        for (File problemInstance : problemFolder.listFiles()) {
            instanceNames.add(problemInstance.getName());
        }
        
        return instanceNames;
    }
}
