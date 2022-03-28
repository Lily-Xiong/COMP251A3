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
//		int[][] adj = new int[numVertices][numVertices];

		int[] startPosition = new int[3];
		int[] exitPosition = new int[3];
		findStartExitIndices(jail, startPosition, exitPosition);

//		createAdjMatrix(jail, adj, startPosition, exitPosition);
		// now we have start, end, adjacency matrix
		// get 1D representation of index
//		int startIndex = AdjMRow(startPosition[0], startPosition[1], startPosition[2], rowLen, colLen);
//		int exitIndex = AdjMRow(exitPosition[0], exitPosition[1], exitPosition[2], rowLen, colLen);

		// use dijkstra
		int[][][] distances = dijkstra(jail, startPosition, numVertices);
//		print3DArray(distances);
		int exitDistance = distances[exitPosition[0]][exitPosition[1]][exitPosition[2]];
		if (exitDistance == Integer.MAX_VALUE) {
			return -1;
		}
		return exitDistance;
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

	public static int[] getEastIndices(String[][][] jail, int level, int row, int col) {
		int[] arr = {level, row, col+1};
		return arr;
	}
	public static int[] getWestIndices(String[][][] jail, int level, int row, int col) {
		int[] arr = {level, row, col-1};
		return arr;
	}
	public static int[] getNorthIndices(String[][][] jail, int level, int row, int col) {
		int[] arr = {level, row-1, col};
		return arr;
	}
	public static int[] getSouthIndices(String[][][] jail, int level, int row, int col) {
		int[] arr = {level, row+1, col};
		return arr;
	}
	public static int[] getUpIndices(String[][][] jail, int level, int row, int col) {
		int[] arr = {level-1, row, col};
		return arr;
	}
	public static int[] getDownIndices(String[][][] jail, int level, int row, int col) {
		int[] arr = {level+1, row, col};
		return arr;
	}

	public static int canGoEast(String[][][] jail, int[] currPos) {
		int levelLength = jail.length;
		int rowLength = jail[0].length;
		int colLength = jail[0][0].length;

		int level = currPos[0];
		int row = currPos[1];
		int col = currPos[2];

		if (col <= colLength - 2) {
			String east = jail[level][row][col + 1];
			// pathEast = 1 if has path, 0 if does not have path
			return hasPath(east);
		}
		return 0;
	}

	public static int canGoWest(String[][][] jail, int[] currPos) {
		int levelLength = jail.length;
		int rowLength = jail[0].length;
		int colLength = jail[0][0].length;

		int level = currPos[0];
		int row = currPos[1];
		int col = currPos[2];

		if (col >= 1) {
			String west = jail[level][row][col - 1];
			return hasPath(west);
		}
		return 0;
	}

	public static int canGoNorth(String[][][] jail, int[] currPos) {
		int levelLength = jail.length;
		int rowLength = jail[0].length;
		int colLength = jail[0][0].length;

		int level = currPos[0];
		int row = currPos[1];
		int col = currPos[2];

		if (row >= 1) {
			String north = jail[level][row - 1][col];
			return hasPath(north);
		}
		return 0;
	}

	public static int canGoSouth(String[][][] jail, int[] currPos) {
		int levelLength = jail.length;
		int rowLength = jail[0].length;
		int colLength = jail[0][0].length;

		int level = currPos[0];
		int row = currPos[1];
		int col = currPos[2];

		if (row <= rowLength - 2) {
			String south = jail[level][row + 1][col];
			return hasPath(south);
		}
		return 0;
	}

	public static int canGoUp(String[][][] jail, int[] currPos) {
		int levelLength = jail.length;
		int rowLength = jail[0].length;
		int colLength = jail[0][0].length;

		int level = currPos[0];
		int row = currPos[1];
		int col = currPos[2];

		if (level >= 1) {
			String up = jail[level - 1][row][col];
			return hasPath(up);
		}
		return 0;
	}

	public static int canGoDown(String[][][] jail, int[] currPos) {
		int levelLength = jail.length;
		int rowLength = jail[0].length;
		int colLength = jail[0][0].length;

		int level = currPos[0];
		int row = currPos[1];
		int col = currPos[2];

		if (level <= levelLength - 2) {
			String down = jail[level + 1][row][col];
			return hasPath(down);
		}
		return 0;
	}



	public static void createAdjMatrix(String[][][] jail, int[][] adjMatrix, int[] startPosition, int[] exitPosition) {
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
//					printMatrix(adjMatrix);
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

						int adjMatrixRow = get1DIndex(level, row, col, rowLength, colLength);
						int adjMatrixCol = adjMatrixRow;
						// cell to itself is 1
						adjMatrix[adjMatrixRow][adjMatrixCol] = 1;

						if (col <= colLength - 2) {
							String east = jail[level][row][col + 1];
							// pathEast = 1 if has path, 0 if does not have path
							int pathEast = hasPath(east);
							adjMatrixCol = adjMatrixRow + 1;
							adjMatrix[adjMatrixRow][adjMatrixCol] = pathEast;
							adjMatrix[adjMatrixCol][adjMatrixRow] = pathEast;
						}

						if (col >= 1) {
							String west = jail[level][row][col - 1];
							int pathWest = hasPath(west);
							adjMatrixCol = adjMatrixRow - 1;
							adjMatrix[adjMatrixRow][adjMatrixCol] = pathWest;
							adjMatrix[adjMatrixCol][adjMatrixRow] = pathWest;
						}

						if (row >= 1) {
							String north = jail[level][row - 1][col];
							int pathNorth = hasPath(north);
							adjMatrixCol = adjMatrixRow - colLength;
							adjMatrix[adjMatrixRow][adjMatrixCol] = pathNorth;
							adjMatrix[adjMatrixCol][adjMatrixRow] = pathNorth;
						}

						if (row <= rowLength - 2) {
							String south = jail[level][row + 1][col];
							int pathSouth = hasPath(south);
							adjMatrixCol = adjMatrixRow + colLength;
							adjMatrix[adjMatrixRow][adjMatrixCol] = pathSouth;
							adjMatrix[adjMatrixCol][adjMatrixRow] = pathSouth;
						}

						if (level >= 1) {
							String up = jail[level - 1][row][col];
							int pathUp = hasPath(up);
							adjMatrixCol = adjMatrixRow - (rowLength * colLength);
							adjMatrix[adjMatrixRow][adjMatrixCol] = pathUp;
							adjMatrix[adjMatrixCol][adjMatrixRow] = pathUp;
						}

						if (level <= levelLength - 2) {
							String down = jail[level + 1][row][col];
							int pathDown = hasPath(down);
							adjMatrixCol = adjMatrixRow + (rowLength * colLength);
							adjMatrix[adjMatrixRow][adjMatrixCol] = pathDown;
							adjMatrix[adjMatrixCol][adjMatrixRow] = pathDown;
						}

					}
				}
			}
		}
		printMatrix(adjMatrix);

	}


	public static int[][][] dijkstra(String[][][] jail, int[] startPosition, int numVertices) {

		PriorityQueue<Node> pq = new PriorityQueue<>();
		int levelLength = jail.length;
		int rowLength = jail[0].length;
		int colLength = jail[0][0].length;

		// store distances
		int[][][] distances = new int[levelLength][rowLength][colLength];
		boolean[][][] finalized = new boolean[levelLength][rowLength][colLength];

//		ArrayList<ArrayList<ArrayList<Node>>> jailNodes = new ArrayList<>();
		ArrayList<Node> nodesIn1D = new ArrayList<>();

		for(int level = 0; level < levelLength; level++) {
			for (int row = 0; row < rowLength; row++) {
				for (int col = 0; col < colLength; col++) {
					distances[level][row][col] = Integer.MAX_VALUE;
					finalized[level][row][col] = false;
					if (level == startPosition[0] && row == startPosition[1] && col == startPosition[2]) {
						nodesIn1D.add(new Node(level, row, col, 0));
					} else {
						nodesIn1D.add(new Node(level, row, col, Integer.MAX_VALUE));
					}
				}
			}
		}
		// source to itself is 0
		distances[startPosition[0]][startPosition[1]][startPosition[2]] = 0;

		Node sourceNode = nodesIn1D.get(get1DIndex(startPosition[0], startPosition[1], startPosition[2], rowLength, colLength));
		pq.add(sourceNode);

//		for (int v = 0; v < numVertices -1; v++) {
		while (!pq.isEmpty()) {
			// get vertex with smallest distances, mimics priority queue
			Node minNode = pq.remove();
//			int[] minVertex = findClosestVertex(distances, finalized);
			int[] minVertex = {minNode.level, minNode.row, minNode.col};

			// update adjacent vertices
			finalized[minVertex[0]][minVertex[1]][minVertex[2]] = true;

//			for (int j = 0; j < numVertices; j++) {
//				if(!finalized[j] && adjMatrix[minVertex][j] == 1 && distances[minVertex] != Integer.MAX_VALUE && distances[minVertex] + 1 < distances[j]){
//					distances[j] = distances[minVertex] + 1;
//				}
//			}
			if (canGoEast(jail, minVertex) == 1) {
				int[] neighbourIndices = getEastIndices(jail, minVertex[0], minVertex[1], minVertex[2]);
				Node neighbourNode = nodesIn1D.get(get1DIndex(neighbourIndices[0], neighbourIndices[1], neighbourIndices[2], rowLength, colLength));

				int minVertexDistance = distances[minVertex[0]][minVertex[1]][minVertex[2]];
				int neighbourVertexDistance = distances[neighbourIndices[0]][neighbourIndices[1]][neighbourIndices[2]];

				if (!finalized[neighbourIndices[0]][neighbourIndices[1]][neighbourIndices[2]]
						&& minVertexDistance != Integer.MAX_VALUE
						&& minVertexDistance + 1 < neighbourVertexDistance) {

					distances[neighbourIndices[0]][neighbourIndices[1]][neighbourIndices[2]] = minVertexDistance + 1;
					neighbourNode.cost = minVertexDistance + 1;
					pq.add(neighbourNode);
				}
			}
			if (canGoWest(jail, minVertex) == 1) {
				int[] neighbourIndices = getWestIndices(jail, minVertex[0], minVertex[1], minVertex[2]);
				Node neighbourNode = nodesIn1D.get(get1DIndex(neighbourIndices[0], neighbourIndices[1], neighbourIndices[2], rowLength, colLength));

				int minVertexDistance = distances[minVertex[0]][minVertex[1]][minVertex[2]];
				int neighbourVertexDistance = distances[neighbourIndices[0]][neighbourIndices[1]][neighbourIndices[2]];

				if (!finalized[neighbourIndices[0]][neighbourIndices[1]][neighbourIndices[2]]
						&& minVertexDistance != Integer.MAX_VALUE
						&& minVertexDistance + 1 < neighbourVertexDistance) {

					distances[neighbourIndices[0]][neighbourIndices[1]][neighbourIndices[2]] = minVertexDistance + 1;
					neighbourNode.cost = minVertexDistance + 1;
					pq.add(neighbourNode);
				}
			}
			if (canGoNorth(jail, minVertex) == 1) {
				int[] neighbourIndices = getNorthIndices(jail, minVertex[0], minVertex[1], minVertex[2]);
				Node neighbourNode = nodesIn1D.get(get1DIndex(neighbourIndices[0], neighbourIndices[1], neighbourIndices[2], rowLength, colLength));

				int minVertexDistance = distances[minVertex[0]][minVertex[1]][minVertex[2]];
				int neighbourVertexDistance = distances[neighbourIndices[0]][neighbourIndices[1]][neighbourIndices[2]];

				if (!finalized[neighbourIndices[0]][neighbourIndices[1]][neighbourIndices[2]]
						&& minVertexDistance != Integer.MAX_VALUE
						&& minVertexDistance + 1 < neighbourVertexDistance) {

					distances[neighbourIndices[0]][neighbourIndices[1]][neighbourIndices[2]] = minVertexDistance + 1;
					neighbourNode.cost = minVertexDistance + 1;
					pq.add(neighbourNode);
				}
			}
			if (canGoSouth(jail, minVertex) == 1) {
				int[] neighbourIndices = getSouthIndices(jail, minVertex[0], minVertex[1], minVertex[2]);
				Node neighbourNode = nodesIn1D.get(get1DIndex(neighbourIndices[0], neighbourIndices[1], neighbourIndices[2], rowLength, colLength));

				int minVertexDistance = distances[minVertex[0]][minVertex[1]][minVertex[2]];
				int neighbourVertexDistance = distances[neighbourIndices[0]][neighbourIndices[1]][neighbourIndices[2]];

				if (!finalized[neighbourIndices[0]][neighbourIndices[1]][neighbourIndices[2]]
						&& minVertexDistance != Integer.MAX_VALUE
						&& minVertexDistance + 1 < neighbourVertexDistance) {

					distances[neighbourIndices[0]][neighbourIndices[1]][neighbourIndices[2]] = minVertexDistance + 1;
					neighbourNode.cost = minVertexDistance + 1;
					pq.add(neighbourNode);
				}
			}
			if (canGoUp(jail, minVertex) == 1) {
				int[] neighbourIndices = getUpIndices(jail, minVertex[0], minVertex[1], minVertex[2]);
				Node neighbourNode = nodesIn1D.get(get1DIndex(neighbourIndices[0], neighbourIndices[1], neighbourIndices[2], rowLength, colLength));

				int minVertexDistance = distances[minVertex[0]][minVertex[1]][minVertex[2]];
				int neighbourVertexDistance = distances[neighbourIndices[0]][neighbourIndices[1]][neighbourIndices[2]];

				if (!finalized[neighbourIndices[0]][neighbourIndices[1]][neighbourIndices[2]]
						&& minVertexDistance != Integer.MAX_VALUE
						&& minVertexDistance + 1 < neighbourVertexDistance) {

					distances[neighbourIndices[0]][neighbourIndices[1]][neighbourIndices[2]] = minVertexDistance + 1;
					neighbourNode.cost = minVertexDistance + 1;
					pq.add(neighbourNode);
				}
			}
			if (canGoDown(jail, minVertex) == 1) {
				int[] neighbourIndices = getDownIndices(jail, minVertex[0], minVertex[1], minVertex[2]);
				Node neighbourNode = nodesIn1D.get(get1DIndex(neighbourIndices[0], neighbourIndices[1], neighbourIndices[2], rowLength, colLength));

				int minVertexDistance = distances[minVertex[0]][minVertex[1]][minVertex[2]];
				int neighbourVertexDistance = distances[neighbourIndices[0]][neighbourIndices[1]][neighbourIndices[2]];

				if (!finalized[neighbourIndices[0]][neighbourIndices[1]][neighbourIndices[2]]
						&& minVertexDistance != Integer.MAX_VALUE
						&& minVertexDistance + 1 < neighbourVertexDistance) {

					distances[neighbourIndices[0]][neighbourIndices[1]][neighbourIndices[2]] = minVertexDistance + 1;
					neighbourNode.cost = minVertexDistance + 1;
					pq.add(neighbourNode);
				}
			}
		}
		return distances;
	}

	public static int[] findClosestVertex(int[][][] distances, boolean[][][] finalized){
		int levelLength = distances.length;
		int rowLength = distances[0].length;
		int colLength = distances[0][0].length;

		int minDistance = Integer.MAX_VALUE;
		int[] closestPosition = {-1, -1, -1};
		for(int level = 0; level < levelLength; level++) {
			for (int row = 0; row < rowLength; row++) {
				for (int col = 0; col < colLength; col++) {
					int dist = distances[level][row][col];
					if (!finalized[level][row][col] && dist <= minDistance) {
						minDistance = dist;
						closestPosition[0] = level;
						closestPosition[1] = row;
						closestPosition[2] = col;
					}
				}
			}
		}
		return closestPosition;
	}

	public static void findStartExitIndices(String[][][] jail, int[] startPosition, int[] exitPosition) {
		String START = "S";
		String EXIT = "E";

		int levelLength = jail.length;
		int rowLength = jail[0].length;
		int colLength = jail[0][0].length;

		for(int level = 0; level < levelLength; level++) {
			for (int row = 0; row < rowLength; row++) {
				for (int col = 0; col < colLength; col++) {
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
				}
			}
		}
	}

	public static int get1DIndex(int level, int row, int col, int rowLen, int colLen) {
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

	public static void print3DArray(int[][][] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				for (int k = 0; k < arr[0][0].length; k++) {
					System.out.print(arr[i][j][k] + " ");
				}
				System.out.println();
			}
			System.out.println("level " + i);
		}
	}

	public static class Node implements Comparable<Node> {
		public int level;
		public int row;
		public int col;

		public int cost;

		public Node() {
		}

		public Node(int level, int row, int col, int cost) {
			this.level = level;
			this.row = row;
			this.col = col;
			this.cost = cost;
		}

		@Override
		public int compareTo(Node o) {
			return Integer.compare(this.cost, o.cost);
		}
	}
}
