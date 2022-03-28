import java.util.*;


public class Q1 {

	public static int find_exit(String[][][] jail) {
		// receive jail as parameter
		// create adjacency matrix
		// retrieve level, row, col
		int levelLen = jail.length;
		int rowLen = jail[0].length;
		int colLen = jail[0][0].length;
		// number of vertices on each side of the adjacency matrix
		int numVertices = levelLen * rowLen * colLen;
		// create 2d array for adjacency matrix
		int[][] adj = new int[numVertices][numVertices];
		int[] startPosition = new int[3];
		int[] exitPosition = new int[3];
		createAdjMatrix(jail, adj, startPosition, exitPosition);
		// now we have start, end, adjacency matrix
		// get 1D representation of index
		int startIndex = AdjMRow(startPosition[0], startPosition[1], startPosition[2], rowLen, colLen);
		int exitIndex = AdjMRow(exitPosition[0], exitPosition[1], exitPosition[2], rowLen, colLen);
		// use dijkstra
		int[] distances = dijkstra(startIndex, adj, numVertices);
		print1DArray(distances);
		if (distances[exitIndex] == Integer.MAX_VALUE) {
			return -1;
		}
		return distances[exitIndex];
	}
	

	public static void main(String[] args) {
		String[][][] jail1 = new String[][][]{
				{		{"S", ".", ".", ".", "." },
						{".", "#", "#", "#", "." },
						{".", "#", "#", ".", "." },
						{"#", "#", "#", ".", "#" }},

				{		{"#", "#", "#", "#", "#" },
						{"#", "#", "#", "#", "#" },
						{"#", "#", ".", "#", "#" },
						{"#", "#", ".", ".", "."}},

				{		{"#", "#", "#", "#", "#"},
						{"#", "#", "#", "#", "#"},
						{"#", ".", "#", "#", "#"},
						{"#", "#", "#", "#", "E"}}
		};
		String[][][] jail2 = new String[][][]{
				{		{"S", ".", ".", "."},
						{".", ".", "#", "."},
						{".", "#", "#", "E"},},

		};
		String[][][] jail3 = new String[][][]{{
			{"S", ".", "."},
				{".", ".", "."},
				{".", "#", "E"}
		}};
		int quickestMoves = find_exit(jail1);
		System.out.println(quickestMoves);

	}

	public static void createAdjMatrix(String[][][] jail, int[][] adj, int[] startPosition, int[] exitPosition) {
		String DOT = ".";
		String POUND = "#";
		String START = "S";
		String EXIT = "E";

		int levelLength = jail.length;
		int rowLength = jail[0].length;
		int colLength = jail[0][0].length;


		for(int level = 0; level < levelLength; level++) {
			for(int row = 0; row < rowLength; row++) {
				for(int col = 0; col < colLength; col++) {
//					printMatrix(adj);
//					System.out.println(level + "," + row + "," + col);
					if (!jail[level][row][col].equals(POUND)) {
						// check if it is start
						if(jail[level][row][col].equals(START)) {
							startPosition[0] = level;
							startPosition[1] = row;
							startPosition[2] = col;
						}

						else if(jail[level][row][col].equals(EXIT)) {
							exitPosition[0] = level;
							exitPosition[1] = row;
							exitPosition[2] = col;
						}

						int adjMatrixRow = AdjMRow(level, row, col, rowLength, colLength);
						int adjMatrixCol = adjMatrixRow;
						// cell to itself is 1
						adj[adjMatrixRow][adjMatrixCol] = 1;

						if (col <= colLength - 2) {
							String east = jail[level][row][col + 1];
							// pathEast = 1 if has path, 0 if does not have path
							int pathEast = hasPath(east);
							adjMatrixCol = adjMatrixRow + 1;
							adj[adjMatrixRow][adjMatrixCol] = pathEast;
							adj[adjMatrixCol][adjMatrixRow] = pathEast;
						}

						if (col >= 1) {
							String west = jail[level][row][col - 1];
							int pathWest = hasPath(west);
							adjMatrixCol = adjMatrixRow - 1;
							adj[adjMatrixRow][adjMatrixCol] = pathWest;
							adj[adjMatrixCol][adjMatrixRow] = pathWest;
						}

						if (row >= 1) {
							String north = jail[level][row - 1][col];
							int pathNorth = hasPath(north);
							adjMatrixCol = adjMatrixRow - colLength;
							adj[adjMatrixRow][adjMatrixCol] = pathNorth;
							adj[adjMatrixCol][adjMatrixRow] = pathNorth;

						}

						if (row <= rowLength - 2) {
							String south = jail[level][row + 1][col];
							int pathSouth = hasPath(south);
							adjMatrixCol = adjMatrixRow + colLength;
							adj[adjMatrixRow][adjMatrixCol] = pathSouth;
							adj[adjMatrixCol][adjMatrixRow] = pathSouth;

						}

						if (level >= 1) {
							String up = jail[level - 1][row][col];
							int pathUp = hasPath(up);
							adjMatrixCol = adjMatrixRow - (rowLength * colLength);
							adj[adjMatrixRow][adjMatrixCol] = pathUp;
							adj[adjMatrixCol][adjMatrixRow] = pathUp;

						}
						if (level <= levelLength - 2) {
							String down = jail[level + 1][row][col];
							int pathDown = hasPath(down);
							adjMatrixCol = adjMatrixRow + (rowLength * colLength);
							adj[adjMatrixRow][adjMatrixCol] = pathDown;
							adj[adjMatrixCol][adjMatrixRow] = pathDown;
						}

					}
				}
			}
		}
		printMatrix(adj);

	}

	public static int AdjMRow(int level, int row, int col, int rowLen, int colLen) {
		return (level * rowLen * colLen) + (row * colLen) + col;
	}

	// given neighbour at south, north, east, west, up, down -> return where is a path
	public static int hasPath(String neighbour) {
		// return 1 if has a path, return 0 is there is not a path
		String DOT = ".";
		String POUND = "#";
		String START = "S";
		String EXIT = "E";
		if(!neighbour.equals(POUND)){
			return 1;
		}
		return 0;
	}

	public static int[] dijkstra(int startIndex, int[][] adjMatrix, int numVertices) {
		// store distances
		int[] distance = new int[numVertices];
		boolean[] finalized = new boolean[numVertices];
		for (int i = 0; i < numVertices; i++) {
			distance[i] = Integer.MAX_VALUE; // initialize distances to infinity
			finalized[i] = false;
		}
		// source to itself is 0
		distance[startIndex] = 0;

		for (int v = 0; v < numVertices -1; v++) {
			// get vertex with smallest distance, mimics priority queue
			int minVertex = findClosestVertex(distance, finalized);
			// update adjacent vertices
			finalized[minVertex] = true;
			for (int j = 0; j < numVertices; j++) {
				if(!finalized[j] && adjMatrix[minVertex][j] == 1 && distance[minVertex] != Integer.MAX_VALUE && distance[minVertex] + 1 < distance[j]){
					distance[j] = distance[minVertex] + 1;
				}
			}

		}
		return distance;
	}

	public static int findClosestVertex(int[] distance, boolean[] finalized){
		int minDistance = Integer.MAX_VALUE;
		int closestIndex = -1;
		// if we have not finalized the vertex and it has the minimum distance, return the index
		for (int i = 0; i < distance.length; i++){
			if(!finalized[i] && distance[i] <= minDistance){
				minDistance = distance[i];
				closestIndex = i;
			}
		}
		return closestIndex;

	}


	public static void printMatrix(int[][] adj){
		// Loop through all rows
		for (int i = 0; i < adj.length; i++) {

			// Loop through all elements of current row
			for (int j = 0; j < adj[i].length; j++) {
				System.out.print(adj[i][j] + " ");
			}
			System.out.println();
		}
	}

	public static void print1DArray(int[] arr) {
		// Loop through all rows
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

}
