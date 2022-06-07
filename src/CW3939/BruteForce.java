//NOTE: CHANGE MADE ON LINE 144 AFTER INITIAL SUBMISSION

package CW3939;
import java.util.*;
import java.io.*;

public class BruteForce {
	
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
    	double[][] matrix = readFile(filename); 
	      matrix = getDistMatrix(matrix);
	      int[] bestPath = tsp(matrix);
	      System.out.println("Path: " + Arrays.toString(bestPath));
	       
	      double pathDistance = computePathDistance(bestPath, matrix); 
	      System.out.println("Distance: " + pathDistance); 
	      System.out.println("\n-------------\n");
    }

	
	// Given an adjacency matrix this method goes through all possible routes 
    public static int[] tsp(double[][] matrix) { 
    	int n = matrix.length; //number of nodes
    	int [] permutation = new int[n]; //possible route
    	for(int i = 0; i < n; i++) {
    		permutation[i] = i; 
    	}
    	int[] bestPath = permutation.clone(); //default best path
    	double bestPathDistance = Double.POSITIVE_INFINITY; //default shortest distance
 
    	// Try all n paths 
    	do { 
    		//find path distance
    		double pathDistance = computePathDistance(permutation, matrix); 
       
    		if (pathDistance < bestPathDistance) { 
    			bestPathDistance = pathDistance; 
    			bestPath = permutation.clone(); 
    		} 
 
    	} while(nextPermutation(permutation)); 
    	return bestPath; 
    } 
 
    public static double computePathDistance(int[] path, double[][] matrix) { 
        double distance = 0; 
      // computes the distance of going to each city 
      for(int i = 1; i < matrix.length; i++) { 
        int from = path[i-1]; 
        int to   = path[i]; 
        distance += matrix[from][to]; 
      } 
      // computes the distance from return to the starting city 
      int last  = path[matrix.length-1]; 
      int first = path[0]; 
      return distance + matrix[last][first]; 
    } 
 
    // generates the next ordered permutation; skips repeated
    public static boolean nextPermutation(int [] arr) { 
      int first = getFirst(arr); 
      if (first == -1) {
    	  return false;
      } 
      int toSwap = arr.length - 1; 
      while (arr[first] >= arr[toSwap]) { 
    	  --toSwap; 
      }
      swap(arr, first++, toSwap); toSwap = arr.length - 1; 
      while (first < toSwap) {
    	  swap(arr, first++, toSwap--); 
      }
      return true; 
    } 
    //used in permutation to swap
    private static int getFirst(int [] arr) { 
      for (int i = arr.length - 2; i >= 0; --i) 
        if (arr[i] < arr[i + 1]) return i; 
      return -1; 
    } 
 
    private static void swap(int [] arr, int i, int j) { 
      int temp = arr[i]; 
      arr[i] = arr[j]; 
      arr[j] = temp; 
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
                numberOfLines++;}
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
        }//close for try
        
        return citiesArr;
    }
    
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
    
    //calculates the distance between two points
    public static double calcEdge(double x1, double y1, double x2, double y2) {
    	// sqrt((x2 - x1)^2 + (y2 - y1)^2)
        double dist = (double)(Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2))); 
        return Math.round(dist);
    }
}
