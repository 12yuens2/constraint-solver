package experiment;

import csp.heuristic.Heuristic;

public class Result {

    private String instanceName;
    private String heuristic;
    private double secondsTaken;
    private int solverNodes;
    private int arcRevisions;
    private int numSolutions;
    
    public Result(String instanceName, String heuristic, 
                  double secondsTaken, int solverNodes, int arcRevisions, int numSolutions) {
        
        this.instanceName = instanceName;
        this.heuristic = heuristic;
        this.secondsTaken = secondsTaken;
        this.solverNodes = solverNodes;
        this.arcRevisions = arcRevisions;
        this.numSolutions = numSolutions;
    }
    
    
    public String getInstanceName() {
        return instanceName;
    }

    public String getHeuristic() {
        return heuristic;
    }
    
    public double getSolverTime() {
        return secondsTaken;
    }

    public int getSolverNodes() {
        return solverNodes;
    }

    public int getArcRevisions() {
        return arcRevisions;
    }
    
    public int getNumSolutions() {
        return numSolutions;
    }
    
}
