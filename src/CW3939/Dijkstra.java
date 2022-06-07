//NOTE: CHANGE MADE ON LINE 193 AFTER INITIAL SUBMISSION

package CW3939;
import java.io.File;
import java.util.*;


public class Dijkstra {
	private static final int NO_PARENT = -1;

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
    
    //method to run Dijkstra on the file
    public static void runTSP(String filename) throws Exception {
        double[][] citiesArr = readFile(filename);
        runDijkstra(0, citiesArr.length, getDistMatrix(citiesArr));
        
        System.out.println("\n-------------\n");
    }
    
  
    // implements Dijkstra's SSSP algorithm for a adjacency matrix representation 
    private static double[] dijkstra(double[][] matrix, int startNode) { 
        int nNodes = matrix[0].length; 
  
        // shortestDistances[i] will hold the shortest distance from start to i 
        double[] shortestDistances = new double[nNodes]; 
  
        // visited[i] contains true if i is included / in shortest path tree 
        // or shortest distance from start to i is finalized 
        boolean[] visited = new boolean[nNodes]; 
        // Initialize all distances as INFINITE and visited[] as false 
        for (int nodeIndex = 0; nodeIndex < nNodes; nodeIndex++) {
            shortestDistances[nodeIndex] = Integer.MAX_VALUE; 
            visited[nodeIndex] = false; 
        } 
          
        // Distance of start to itself is always 0 
        shortestDistances[startNode] = 0; 
  
        // Parent array to store shortest path tree 
        int[] parents = new int[nNodes]; 
  
        // The starting node does not have a parent 
        parents[startNode] = NO_PARENT; 
  
        // Find shortest path for all  nodes 
        for (int i = 1; i < nNodes; i++) { 
            // Pick the minimum distance node from the set of nodes not yet traversed 
            int nearestNode = -1; 
            double shortestDistance = Double.MAX_VALUE; 
            for (int nodeIndex = 0; nodeIndex < nNodes; nodeIndex++) { 
                if (!visited[nodeIndex] && shortestDistances[nodeIndex] < shortestDistance) { 
                    nearestNode = nodeIndex; 
                    shortestDistance = shortestDistances[nodeIndex]; 
                } 
            } 
  
            // Mark the picked node as visited 
            visited[nearestNode] = true; 
  
            // Update distance value of the adjacent nodes
            for (int nodeIndex = 0; nodeIndex < nNodes; nodeIndex++) { 
                double edgeDistance = matrix[nearestNode][nodeIndex]; 
                if (edgeDistance > 0 && ((shortestDistance + edgeDistance) < shortestDistances[nodeIndex])) {
                    parents[nodeIndex] = nearestNode; 
                    shortestDistances[nodeIndex] = shortestDistance + edgeDistance; 
                } 
            } 
        } 
        return shortestDistances;
    } 
    
    //run Dijkstra through a loop of the cities
    public static void runDijkstra(int start, int nNodes, double[][] matrix) {
        //visited array
    	boolean[] visited = new boolean[nNodes];
        //keep track of the path
    	int[] path = new int[nNodes + 1]; int pathPos = 1;
        //path starts at the starting node, and so is visited
    	path[0] = start; visited[0] = true;
    	//keep track of nodes in iteration
        int nextNode = 0; int preNode = start; 
        double minDist = 0; //cost of tour
        
        //while there are nodes to visit, visit them
        while(containsFalse(visited)) {
        	double[] distArray = dijkstra(matrix, nextNode);
        
        	nextNode = pickSmallestNode(preNode, visited, distArray);
        	minDist += distArray[nextNode];
        	path[pathPos] = nextNode; pathPos++;
        	preNode = nextNode;
        }
        
        //return to origin
        double[] distArray = dijkstra(matrix, nextNode);
        visited[0] = false;
        nextNode = pickSmallestNode(preNode, visited, distArray);
        path[pathPos] = 0; minDist += distArray[nextNode];
        
        //print out tour and cost
        System.out.println("Tour: " + Arrays.toString(path));
        System.out.println("Distance: " + minDist);
    }
    
    //method to tell if an array contains false
    public static boolean containsFalse(boolean[] arr) {
        for(int f=0; f < arr.length; f++) {
            if(arr[f] == false) {
                return true;
            }
        }
        return false;
    }
    
    //pick the next smallest distance node
    public static int pickSmallestNode(int node, boolean[] visited, double[] shortestDistance) {
        int minPos = 0; 
        double minDist = Double.MAX_VALUE;
        for(int i = 0; i< shortestDistance.length; i++) {
            if(i == node) {
                continue;
            } else {
                if(shortestDistance[i] < minDist) {
                    if(visited[i]) {
                        continue;
                    } else {
                        minPos = i;
                        minDist = shortestDistance[i];
                    }
                }
            }
        }
        visited[minPos] = true;
        return minPos;
    }
    
    //method to read file
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
    
    //calculate the distance between two points
    public static double calcEdge(double x1, double y1, double x2, double y2) {
    	// sqrt((x2 - x1)^2 + (y2 - y1)^2)
        double dist = (double)(Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2))); 
        return Math.round(dist);
    }
}
