import java.util.*;


public class Q2 {
	
	public static int rings(Hashtable<Integer, ArrayList<Integer>> graph, int[]location) {
		int EARTH = 1;
		int ASGARD = 2;
		// find nodes with no predecessors, put them into a queue
		// create boolean for visited.
		boolean[] visited = new boolean[graph.size()];
		//int[] dependencies = new int[graph.size()];
		int[] inDegree = new int[graph.size()];

		inDegreeCalculation(graph, inDegree);
		// now dependencies is filled with dependencies, we want to find a source node.
		// One source node for each planet.

		// Todo: queue that store sources?
		Queue<Integer> qEarth = new LinkedList<Integer>();
		Queue<Integer> qAsgard = new LinkedList<Integer>();
		Queue<Integer> qAsgardDuringEarth = new LinkedList<Integer>();
		Queue<Integer> qEarthDuringAsgard = new LinkedList<Integer>();
		// find source with degree 0 for earth
		Integer sourceEarth = minInDegreeNodeByPlanet(inDegree, location, EARTH);
		qEarth.add(sourceEarth);
		Integer sourceAsgard = minInDegreeNodeByPlanet(inDegree, location, ASGARD);
		qAsgard.add(sourceAsgard);

		// now perform topological sort on sourceEarth and source Asgard
		int transferStartFromEarth = topologicalSortHelper(graph, dependencies, location, EARTH, qEarth, qAsgardDuringEarth);
		int transferStartFromAsgard = topologicalSortHelper(graph, dependencies, location, ASGARD, qAsgard, qEarthDuringAsgard);

		if(transferStartFromAsgard <= transferStartFromEarth){
			return transferStartFromAsgard;
		}

		return transferStartFromEarth;
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
	public static int topologicalSortHelper(Hashtable<Integer, ArrayList<Integer>> graph, int[] dependencies,
											 int[] location, int planetNum, Queue<Integer> qCurPlanet, Queue<Integer> qOtherPlanet){
		int transfer = 0;
		while(!qCurPlanet.isEmpty() || !qOtherPlanet.isEmpty()){
			topologicalSortPlanet(graph, dependencies, location, planetNum, qCurPlanet, qOtherPlanet);
			transfer ++;
			// now need to swap qCurPlanet and qOtherPlanet
			if(!qCurPlanet.isEmpty() || !qOtherPlanet.isEmpty()){
				topologicalSortPlanet(graph, dependencies, location, planetNum, qOtherPlanet, qCurPlanet);
				transfer++;
			}

		}
		return transfer;

	}


	public static void topologicalSortPlanet(Hashtable<Integer, ArrayList<Integer>> graph, int[] dependencies,
											 int[] location, int planetNum, Queue<Integer> qCurPlanet, Queue<Integer> qOtherPlanet){
		// Stores sorted nodes
		ArrayList<Integer> sortedNodes = new ArrayList<>();
		while (!qCurPlanet.isEmpty()) {
			// get minNode
			int minNode = qCurPlanet.remove();
			sortedNodes.add(minNode);

			// iterate through minNode's neighbour nodes??... decrease in degree
			ArrayList<Integer> neighbourNodes = graph.get(minNode);
			for(Integer neighbour : neighbourNodes){
				// decrease dependencies
				// TODO: dependencies = in degree?
				// TODO: dependencies = out degree!!
				dependencies[neighbour] = dependencies[neighbour] -1;
				System.out.println("neighbor" + neighbour + "dependencies" + dependencies[neighbour]);
				// same planet and in degree == 0;
				if(dependencies[neighbour] == 0){

					System.out.println(neighbour);
					System.out.println(location[neighbour]);

					if(location[neighbour] == planetNum){
						qCurPlanet.add(neighbour);
					}
					else{
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
	public static Integer minInDegreeNodeByPlanet(int[] inDegree, int[] location, int planetNum){
		// initialize to -1
		int minInDegreeNode = -1;
		int minInDegree = -1;
		for(int i = 0; i < inDegree.length; i++){
			if(location[i] == planetNum && inDegree[i] < minInDegree){
				minInDegree = inDegree[i];
				minInDegreeNode = i;

			}
		}

		return minInDegreeNode;
	}

	public static void main(String[] args) {
		Hashtable<Integer, ArrayList<Integer>> graph = new Hashtable<>();
		ArrayList<Integer> key0 = new ArrayList<>();
		ArrayList<Integer> key1 = new ArrayList<>();
		ArrayList<Integer> key2 = new ArrayList<>();
		ArrayList<Integer> key3 = new ArrayList<>();
		ArrayList<Integer> key4 = new ArrayList<>();
		key0.add(2);
		key0.add(1);
		key1.add(3);
		key1.add(4);
		key2.add(3);
		key2.add(4);


		graph.put(0, key0);
		graph.put(1, key1);
		graph.put(2, key2);
		graph.put(3, key3);
		graph.put(4, key4);

		int[] location = {1,2,1,2,1};
		int moves = rings(graph, location);
		System.out.println(moves);
	}

}
