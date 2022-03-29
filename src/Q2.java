import java.util.*;


public class Q2 {
	
	public static int rings(Hashtable<Integer, ArrayList<Integer>> graph, int[]location) {
		int EARTH = 1;
		int ASGARD = 2;
		int transferFromEarth = Integer.MAX_VALUE;
		int transferFromAsgard = Integer.MAX_VALUE;
		// find nodes with no predecessors, put them into a queue
		// create boolean for visited.
		boolean[] visited = new boolean[graph.size()];
		//int[] dependencies = new int[graph.size()];
		int[] inDegree = new int[graph.size()];
		// initialize to 0
		for(int i =0; i < inDegree.length; i++){
			inDegree[i] = 0;
		}

		inDegreeCalculation(graph, inDegree);
		// now dependencies is filled with dependencies, we want to find a source node.
		// One source node for each planet.

		// Todo: queue that store sources?
		Queue<Integer> qEarth = new LinkedList<Integer>();
		Queue<Integer> qAsgard = new LinkedList<Integer>();

		// find source with degree 0 for earth, there might not be a source
		Integer sourceEarth = zeroDegreeNodeByPlanet(inDegree, location, EARTH);
		Integer sourceAsgard = zeroDegreeNodeByPlanet(inDegree, location, ASGARD);

		if(sourceEarth != -1){
			qEarth.add(sourceEarth);
			if(sourceAsgard != -1){
				qAsgard.add(sourceAsgard);
			}
			transferFromEarth = topologicalSortHelper(graph, inDegree, location, EARTH, ASGARD, qEarth, qAsgard);
			System.out.println("transferFromEarth " + transferFromEarth);
		}


		if(sourceAsgard != -1){
			qAsgard.add(sourceAsgard);
			if(sourceEarth != -1){
				qEarth.add(sourceEarth);
			}
			transferFromAsgard = topologicalSortHelper(graph, inDegree, location, ASGARD, EARTH, qAsgard, qEarth);
			System.out.println("transferFromAsgard " + transferFromAsgard);
		}


		if(transferFromEarth < transferFromAsgard){
			return transferFromEarth;
		}

		return transferFromAsgard;
	}

	// dependencies in the beginning
	public static void dependenciesBeginning(Hashtable<Integer, ArrayList<Integer>> graph, int[] dependencies){
		Set<Integer> keys = graph.keySet();
		for (Integer key: keys){
			// dependencies[key] = number of nodes dependent on that key
			dependencies[key] = graph.get(key).size();
		}

	}

	// given planet number, find min dependencyNode
	public static Integer minDependencyNodeByPlanet(int[] dependencies, int[] location, int planetNum){
		// initialize to -1
		int minDependencyNode = -1;
		Integer minDependency = Integer.MAX_VALUE;
		for(int i = 0; i < dependencies.length; i++){
			if(location[i] == planetNum && dependencies[i] < minDependency){
				minDependency = dependencies[i];
				minDependencyNode = i;

			}
		}

		return minDependencyNode;
	}

	// given planet number, find max dependencyNode
	public static Integer maxDependencyNodeByPlanet(int[] dependencies, int[] location, int planetNum){
		// initialize to -1
		int maxDependencyNode = -1;
		int maxDependency = -1;
		for(int i = 0; i < dependencies.length; i++){
			if(location[i] == planetNum && dependencies[i] > maxDependency){
				maxDependency = dependencies[i];
				maxDependencyNode = i;

			}
		}

		return maxDependencyNode;
	}
	// returns number of moves
	public static int topologicalSortHelper(Hashtable<Integer, ArrayList<Integer>> graph, int[] inDegree,
											 int[] location, int curPlanetNum, int otherPlanetNum, Queue<Integer> qCurPlanet, Queue<Integer> qOtherPlanet){
		int transfer = 0;
		while(!qCurPlanet.isEmpty() || !qOtherPlanet.isEmpty()){
			topologicalSortPlanet(graph, inDegree, location, curPlanetNum, qCurPlanet, qOtherPlanet);
			transfer ++;
			// now need to swap qCurPlanet and qOtherPlanet
			if(!qCurPlanet.isEmpty() || !qOtherPlanet.isEmpty()){
				topologicalSortPlanet(graph, inDegree, location, otherPlanetNum, qOtherPlanet, qCurPlanet);
				transfer++;
			}

		}
		return transfer -1;

	}


	public static void topologicalSortPlanet(Hashtable<Integer, ArrayList<Integer>> graph, int[] inDegree,
											 int[] location, int planetNum, Queue<Integer> qCurPlanet, Queue<Integer> qOtherPlanet){
		// Stores sorted nodes
		while (!qCurPlanet.isEmpty()) {
			// get minNode
			int minNode = qCurPlanet.remove();


			// iterate through minNode's neighbour nodes??... decrease in degree
			ArrayList<Integer> neighbourNodes = graph.get(minNode);
			for(Integer neighbour : neighbourNodes){

				inDegree[neighbour] = inDegree[neighbour] -1;
				System.out.println("neighbor" + neighbour + "inDegree" + inDegree[neighbour]);
				// same planet and in degree == 0;
				if(inDegree[neighbour] == 0){

					System.out.println(neighbour);
					System.out.println(location[neighbour]);

					if(location[neighbour] == planetNum && !qCurPlanet.contains(neighbour)){
						qCurPlanet.add(neighbour);
					}
					else if(!qOtherPlanet.contains(neighbour)){
						qOtherPlanet.add(neighbour);
					}

				}
			}

		}


	}
	public static void inDegreeCalculation(Hashtable<Integer, ArrayList<Integer>> graph, int[] inDegrees){
		for(int i = 0; i < graph.size(); i++){
			ArrayList<Integer> temp = graph.get(i);
			// each node
			for(int j = 0; j < temp.size(); j++){
				inDegrees[temp.get(j)] =  inDegrees[temp.get(j)] + 1;
			}
		}
	}
	public static Integer zeroDegreeNodeByPlanet(int[] inDegree, int[] location, int planetNum){
		// initialize to -1
		int minInDegreeNode = -1;
		int minInDegree = Integer.MAX_VALUE;
		for(int i = 0; i < inDegree.length; i++){
			if(location[i] == planetNum && inDegree[i] < minInDegree){
				minInDegree = inDegree[i];
				minInDegreeNode = i;

			}
		}
		if(minInDegree == 0){
			return minInDegreeNode;
		}
		else{
			return -1;
		}
	}

	public static void main(String[] args) {
//		Hashtable<Integer, ArrayList<Integer>> graph = new Hashtable<>();
//		ArrayList<Integer> key0 = new ArrayList<>();
//		ArrayList<Integer> key1 = new ArrayList<>();
//		ArrayList<Integer> key2 = new ArrayList<>();
//		ArrayList<Integer> key3 = new ArrayList<>();
//		ArrayList<Integer> key4 = new ArrayList<>();
//		key0.add(2);
//		key0.add(1);
//		key1.add(3);
//		key1.add(4);
//		key2.add(3);
//		key2.add(4);
//
//
//		graph.put(0, key0);
//		graph.put(1, key1);
//		graph.put(2, key2);
//		graph.put(3, key3);
//		graph.put(4, key4);
//
//		int[] location = {1,2,1,2,1};
//		int moves = rings(graph, location);
//		System.out.println(moves);

//		Hashtable<Integer, ArrayList<Integer>> graph2 = new Hashtable<>();
//		ArrayList<Integer> key0 = new ArrayList<>();
//		ArrayList<Integer> key1 = new ArrayList<>();
//		ArrayList<Integer> key2 = new ArrayList<>();
//		ArrayList<Integer> key3 = new ArrayList<>();
//		ArrayList<Integer> key4 = new ArrayList<>();
//
//		key0.add(1);
//		key0.add(3);
//		key1.add(3);
//		key2.add(4);
//		key2.add(0);
//		key2.add(3);
//		key4.add(0);
//		key4.add(1);
//		key4.add(3);
//		graph2.put(0, key0);
//		graph2.put(1, key1);
//		graph2.put(2, key2);
//		graph2.put(3, key3);
//		graph2.put(4, key4);
//
//		int[] location = {2,1,2,2,1};
//		int moves = rings(graph2, location);
//		System.out.println(moves);

		Hashtable<Integer, ArrayList<Integer>> graph3 = new Hashtable<>();
		ArrayList<Integer> key0 = new ArrayList<>();
		ArrayList<Integer> key1 = new ArrayList<>();
		ArrayList<Integer> key2 = new ArrayList<>();
		ArrayList<Integer> key3 = new ArrayList<>();
		ArrayList<Integer> key4 = new ArrayList<>();

		key0.add(1);
		key0.add(3);
		key0.add(4);
		key0.add(2);
		key2.add(3);
		key2.add(1);
		key2.add(4);
		key3.add(1);
		key4.add(1);
		key4.add(3);

		graph3.put(0, key0);
		graph3.put(1, key1);
		graph3.put(2, key2);
		graph3.put(3, key3);
		graph3.put(4, key4);

		int[] location = {2,2,2,1,2};
		int moves = rings(graph3, location);
		System.out.println(moves);
	}

}
