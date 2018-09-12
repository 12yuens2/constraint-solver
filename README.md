# constraint-solver
This is a forward checking constraint solver with 2-way branching written in java. It can read in problems like the ones specified in `csp/` and output the solution.

## Tests
To run the tests, run `ant junit` in the project directory. To keep the tests small and running, tests for more difficult problems and heuristics that take a long time are not included. 

## Binary reader
The main function in the BinaryCSPSolver can be called with `ant` then `java -jar BinaryCSPSolver.jar` for further usage instructions.

List of heuristics for solver:
- "sdf" - Smallest Domain First
- "ldf" - Largest Domain First
- "ascending" - Static ascending
- "descending" - Static descending
- "max-degree" - Maximum degree
- "min-degree" - Minimum degree
- "odd-even" - Odd even variables
- "random" - Dynamic random variable and value ordering
- "random-static" - Random static variable ordering
- "random-value" - Random value ordering
- "random-variable" - Random variable ordering

## Experiments
The experiments main has also been created as a jar and can be run with `ant` then `java -jar Experiment.jar` for further usage instructions.
