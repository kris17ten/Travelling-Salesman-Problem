//NOTE: CHANGE MADE ON LINE 156 AFTER INITIAL SUBMISSION

package CW3939;
import java.util.*;
import java.io.*;

public class DFS {
	static class Edge { 
	    int from, to;
	    double cost; 
	    public Edge(int from, int to, double cost) { 
	        this.from = from; 
	        this.to = to; 
	        this.cost = cost; 
	    }
	        
	    @Override
	    public String toString() {
	        return this.from + " " + this.to + " " + this.cost;
	    }
	}//close for Edge
	
	
	static class KRStack<E>   { 
		//data - just used the ArrayList. It will hold each element 
		private ArrayList<E> data; 
		//constructor 
		public KRStack() { 
			data = new ArrayList<E>(); 
		}  
		//push adds the obj onto the stack 
		public E push(E obj) { 
			data.add(obj); 
			return obj; 
		} 
		//pop pops the last item off the stack 
		public E pop() { 
			if (data.size() == 0) { 
				throw new EmptyStackException(); 
			} else { 
				return data.remove(data.size()-1);   //the ArrayList method will remove and return it, 
			} 
		} 
		//checks if the stack is empty
		public Boolean isEmpty() { 
			if(data.size() == 0) { 
				return true; 
			} else { 
				return false; 
			} 
		}  
	}//close for KRStack  

	
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
	                "\nTime taken(seconds): " + ((System.currentTimeMillis()/1000)-currsecs) + "\nTests Done!\n\n");
	}
	
	//run training with time
	public static void runTraining() throws Exception {
	    long currnano = System.nanoTime();
	    long currsecs = System.currentTimeMillis()/1000;
	    runTSP("test1tsp.txt");
	    runTSP("test2atsp.txt");
	    runTSP("test3atsp.txt");
	    System.out.println("Time taken(nanoseconds): " + (System.nanoTime()-currnano) + 
	                "\nTime taken(seconds): " + ((System.currentTimeMillis()/1000)-currsecs) + "\nTraining Done!\n\n");
	}
	    
	//run the dfs algorithm on a file
	public static void runTSP(String filename) throws Exception {
	    int[][] citiesArr = readFile(filename);
	        
	    System.out.println("Tour cost: " + dfs(getDistArray(citiesArr),1, citiesArr.length));
	    System.out.println("\n-------------\n");
	}
	
	//implements a simple dfs on a graph represented as an array of Edges
	public static double dfs(Edge[] graph, int start, int n) { 
	    //initialize all variables
		double cost = 0; 
	    boolean[] visited = new boolean[n]; 
	    KRStack <Integer> stack = new KRStack<>(); 
	    
	    //push start onto stack to explore    
	    stack.push(start); 
	    System.out.print("Route: ");
	    //while there are nodes to visit, visit them
	    while(!stack.isEmpty()) { 
	        int node = stack.pop(); 
	        System.out.print(node + " ");
	        //if not visited...
	        if (!visited[node-1]) { 
	            visited[node-1] = true; 
	            Edge[] edges = getConnectingEdges(node, graph);
	            
	            if (edges != null) { 
	                for(Edge edge : edges) { 
	                    if (!visited[edge.to-1]) { 
	                        stack.push(edge.to);
	                        cost += edge.cost;
	                    } 
	                } 
	            } 
	        }
	        //return to origin
	        if(visited[0] == true && stack.isEmpty()) {
	            System.out.print("1 ");
	            cost += getConnectingEdges(1, graph)[0].cost;
	        }
	    }
	    System.out.println();
	    return Math.round(cost); 
	}
	    
	public static int[][] readFile(String filename) throws Exception {
	    // Create a File instance     
	    File file = new File(filename);
	    // Read data from a file
	    int numberOfLines = 0;
	    //count the number of sharks in file
	    try ( // Create a Scanner for the file
	            Scanner count = new Scanner(file)) {
	        //count the number of lines in file
	        while(count.hasNextLine()) {
	            String str = count.nextLine();
	            if(str.equals("")) {
	            	continue; 
	            } else {
	                numberOfLines++;
	            }
	        }//close for while
	     }//close for try
	        
	     int[][] citiesArr = new int[numberOfLines][3];
	        
	     try ( // Create a Scanner for the file
	            Scanner input = new Scanner(file)) {
	    	 // Read data from a file
	         for (int i=0; i<numberOfLines; i++) {
	            String str = input.nextLine();
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
	
	//method to get connecting edges from a node in an Edge array
	public static Edge[] getConnectingEdges(int n, Edge[] arr) {
	    Edge[] allEdges = new Edge[arr.length];
	    int k = 0;
	        
	    for(int i=0; i<arr.length; i++) {
	        if(arr[i].from == n) {
	            allEdges[k] = arr[i];
	            k++;
	        }
	    }
	    allEdges = Arrays.stream(allEdges).filter(s -> (s != null)).toArray(Edge[]::new);
	    return allEdges;
	}
	
	//given an integer array of x-y co-ordinates, compose an array of Edges
	public static Edge[] getDistArray(int[][] arr) {
	    Edge[] returnArray = new Edge[fib(arr.length)];
	    int k = 0;
	        
	    for(int d=0; d<arr.length; d++) {
	        for(int e=d+1; e<arr.length; e++) {
	            int[] pt1 = arr[d];
	            int[] pt2 = arr[e];
	            
	            returnArray[k] = new Edge(pt1[0], pt2[0], calcEdge(pt1[1], pt1[2], pt2[1], pt2[2]));
	            k++;
	        }
	    }
	    return returnArray;
	}
	
	//method to calculate the distance between two points
	public static double calcEdge(int x1, int y1, int x2, int y2) {
		// sqrt((x2 - x1)^2 + (y2 - y1)^2)
	    double dist = (double)(Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2))); 
	    return dist;
	}
	
	//calculate number of Edges
	public static int fib(int n) {
	    int factN = 1;
	    for(int f=1; f<n; f++) {
	        factN += f;
	    }
	    return factN-1;
	}
}
