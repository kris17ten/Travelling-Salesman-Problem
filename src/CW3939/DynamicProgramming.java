//NOTE: CHANGE MADE ON LINE 106 AFTER INITIAL SUBMISSION

package CW3939;
import java.util.*;
import java.io.*;

public class DynamicProgramming {
	private final int N; 
    private final int START_NODE; 
    private final int FINISHED_STATE; 
    
    private double[][] distance; 
    private double minPathCost = Double.POSITIVE_INFINITY; 

    private int[] path; 
    private boolean ranSolver = false; 
    
    //constructor
    public DynamicProgramming(double[][] distance) { 
        this(0, distance); 
    } 
    
    //constructor
    public DynamicProgramming(int startNode, double[][] distance) { 
        this.distance = distance; 
        N = distance.length; 
        START_NODE = startNode; 
        
        // Validate inputs. 
        if (N <= 2) throw new IllegalStateException("TSP on 0, 1 or 2 nodes doesn't make sense."); 
        if (N != distance[0].length) throw new IllegalArgumentException("Matrix must be square (N x N)"); 
        if (START_NODE < 0 || START_NODE >= N) throw new IllegalArgumentException("Starting node must be: 0 <= startNode < N"); 
        if (N > 32) throw new IllegalArgumentException("Matrix too large!"); 
        // The finished state is when the finished state mask has all bits are set to one (all the nodes have been visited). 
        FINISHED_STATE = (1 << N) - 1; 
    } 
    
    public static void main(String[] args) throws Exception {
        runTraining();
        runFinalTests();
    }
    
    //run tests with time
    public static void runFinalTests() throws Exception {
        long currnano = System.nanoTime();
        long currsecs = System.currentTimeMillis()/1000;
        runTSP("TSP1-18.txt");
        runTSP("TSP2-18.txt");
        runTSP("TSP3-18.txt");
        runTSP("TSP4-18.txt");
        System.out.println("Time taken(nanoseconds): " + (System.nanoTime()-currnano) + 
                "\nTime taken(seconds): " + ((System.currentTimeMillis()/1000)-currsecs) + "\nTests Done!\n\n\n");
    }
    
    //run training with time
    public static void runTraining() throws Exception {
        long currnano = System.nanoTime();
        long currsecs = System.currentTimeMillis()/1000;
        runTSP("test1tsp.txt");
        runTSP("test2atsp.txt");
        runTSP("test3atsp.txt");
        System.out.println("Time taken(nanoseconds): " + (System.nanoTime()-currnano) + 
                "\nTime taken(seconds): " + ((System.currentTimeMillis()/1000)-currsecs) + "\nTraining Done!\n\n\n");
    }
    
    //run the dynamic programming algorithm on a file
    public static void runTSP(String filename) throws Exception {
        double[][] citiesArr = readFile(filename);
        DynamicProgramming solver = new DynamicProgramming(0, getDistMatrix(citiesArr));
        
        // Prints: [0, 3, 2, 4, 1, 5, 0] 
        System.out.println("Path: " + Arrays.toString(solver.getPath())); 

        // Print: 42.0 
        System.out.println("Distance: " + solver.getPathCost()); 
        System.out.println("\n-------------\n");
    }
    
    public static double[][] readFile(String filename) throws Exception {
        // Create a File instance     
        File file = new File(filename);
        // Read data from a file
        int numberOfLines = 0;
        //count the number of lines
        try ( // Create a Scanner for the file
                Scanner count = new Scanner(file)) {
            //count the number of lines in file
            while(count.hasNextLine()) {
                String str = count.nextLine();
                if(str.equals("")) {
                continue; } else {
                numberOfLines++; }
                //count.nextLine();
            }//close for while
        }//close for try
        double[][] citiesArr = new double[numberOfLines][3];
        
        try ( // Create a Scanner for the file
            Scanner input = new Scanner(file)) {
            // Read data from a file
            for (int i=0; i<numberOfLines; i++) {
                String str = input.nextLine();
                
                //remove all potential spaces
                str = str.replaceAll("( )+", " ");
                str = str.replaceAll("(\t)+", " ");
                if(str.indexOf(" ")==0) {
                str = str.replaceFirst(" ", ""); }
                String[] arrayS = str.split(" ");
                citiesArr[i][0] = Integer.parseInt(arrayS[0]);
                citiesArr[i][1] = Integer.parseInt(arrayS[1]);
                citiesArr[i][2] = Integer.parseInt(arrayS[2]);
            }//close for for-loop
            input.close();
        }//close for try
        
        return citiesArr;
    }
    
  //given an array of cities with x-y co-ordinates, convert it to an adjacency matrix rep.
    public static double[][] getDistMatrix(double[][] arr) {
        double[][] distanceMatrix = new double[arr.length][arr.length];
        
        for(int d=0; d<arr.length; d++) {
            for(int e=d+1; e<arr.length; e++) {
                double[] pt1 = arr[d];
                double[] pt2 = arr[e];
                
                distanceMatrix[d][e] = distanceMatrix[e][d] = calcEdge(pt1[1], pt1[2], pt2[1], pt2[2]);
            }
        }
        
        
        return distanceMatrix;
    }
    
    // Returns the optimal path for the traveling salesman problem. 
    public int[] getPath() { 
        if (!ranSolver) solve(); 
        return path; 
    } 
    
    // Returns the minimal path cost. 
    public double getPathCost() { 
        if (!ranSolver) solve(); 
        return minPathCost; 
    } 
    
    public void solve() { 
        //Running solver to compute the path and distance  
        int state = 1 << START_NODE; 
        Double[][] cache = new Double[N][1 << N]; //length = n * 2^n
        Integer[][] previous = new Integer[N][1 << N]; //length = n * 2^n
        path = new int[N+1]; //length = n+1, to store origin at the end
        minPathCost = tsp(START_NODE, state, cache, previous); 
        int k = 0; //position in the path array
        
        // Regenerate path 
        int index = START_NODE; 
        while (true) { 
        	path[k] = index+1; k++; 
            Integer nextIndex = previous[index][state]; 
            if (nextIndex == null) break; 
            int nextState = state | (1 << nextIndex); 
            state = nextState; 
            index = nextIndex; 
        } 
        path[k] = START_NODE+1; 
        ranSolver = true; 
    } 
    
    private double tsp(int i, int state, Double[][] cache, Integer[][] previous) { 
        // Done this path. Return cost of going back to start node. 
        if (state == FINISHED_STATE) return distance[i][START_NODE]; 
        // Return cached answer if already computed. 
        if (cache[i][state] != null) return cache[i][state]; 
        double minCost = Double.POSITIVE_INFINITY; 
        int index = -1;
        for (int p = 0; p < N; p++) { 
            // Skip if the next node has already been visited. 
            if ((state & (1 << p)) != 0) continue; 
            int nextState = state | (1 << p); 
            double newCost = distance[i][p] + tsp(p, nextState, cache, previous); 
            if (newCost < minCost) { 
                minCost = newCost; 
                index = p; 
            } 
        }
        previous[i][state] = index; 
        return cache[i][state] = minCost; 
    } 
    
  //calculate the distance between two points
    public static double calcEdge(double x1, double y1, double x2, double y2) {
    	// sqrt((x2 - x1)^2 + (y2 - y1)^2)
        double dist = (double)(Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2))); 
        return Math.round(dist);
    }
}
