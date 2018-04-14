package experiment;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import csp.BinaryCSP;
import csp.BinaryConstraint;
import csp.heuristic.Heuristic;
import csp.heuristic.impl.LargestValueFirst;
import csp.heuristic.impl.NoHeuristic;
import csp.heuristic.impl.RandomHeuristic;
import csp.heuristic.impl.SmallestDomainFirst;
import solver.BinaryCSPSolver;
import util.BinaryCSPReader;
import util.CSVWriter;

public class Experiment {
   

    /**
     * Main for running experiments
     */
    public static void main(String[] args) {
        
        ArrayList<Heuristic> heuristics = new ArrayList<>();
        heuristics.add(new NoHeuristic());
        heuristics.add(new SmallestDomainFirst());
        heuristics.add(new RandomHeuristic());
        heuristics.add(new LargestValueFirst());
        
        Experiment ex = new Experiment("queens", "queens.csv", true, heuristics);
        ex.run(15, 100);
        ex.writeResults();
        
    }
   
    private BinaryCSPReader reader;
    private BinaryCSPSolver solver;
    private CSVWriter writer;
   
    private String problemName;
    private ArrayList<Result> results;
    private ArrayList<Heuristic> heuristics;
    private ArrayList<String> instances;
    
    public Experiment(String problemName, String outputFile, boolean allSolutions, ArrayList<Heuristic> heuristics) {
        this.writer = new CSVWriter(outputFile);
        this.reader = new BinaryCSPReader();
        this.solver = new BinaryCSPSolver(allSolutions);
        
        this.results = new ArrayList<>();
        
        this.problemName = problemName;
        this.heuristics = heuristics;
        this.instances = Experiment.getCSPFiles(problemName);
    }
    
    public void run(int warmups, int runs) {
        for (String instanceName : instances) {
            System.out.println("Run instance " + instanceName);
            BinaryCSP csp = reader.readBinaryCSP(BinaryCSPReader.CSP_PATH + problemName + "/" + instanceName);
           
            for (Heuristic heuristic : heuristics) {
                System.out.println("Run heuristic: " + heuristic.toString());
                csp.setHeuristic(heuristic);
                
                runWarmups(csp, warmups);
                Result result = runExperiments(csp, runs);
                results.add(result);
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
    
    private void runWarmups(BinaryCSP csp, int runs) {
        for (int i = 0; i < runs; i++) {
            System.out.println("Run warmup iteration " + i);
            solver.solveCSP(csp);
        }
    }
    
    private Result runExperiments(BinaryCSP csp, int runs) {
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
