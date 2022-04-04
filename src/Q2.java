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

		Queue<Integer> qEarth = new LinkedList<Integer>();
		Queue<Integer> qAsgard = new LinkedList<Integer>();
		ArrayList<Integer> EarthSources = allZeroDegreeNodeByPlanet(inDegree, location, EARTH);
		ArrayList<Integer> AsgardSources = allZeroDegreeNodeByPlanet(inDegree, location, ASGARD);
		// find source with degree 0 for earth, there might not be a source
//		Integer sourceEarth = zeroDegreeNodeByPlanet(inDegree, location, EARTH);
//		Integer sourceAsgard = zeroDegreeNodeByPlanet(inDegree, location, ASGARD);
		if(EarthSources.size() != 0){
			qEarth.addAll(EarthSources);
			if(AsgardSources.size() != 0){
				qAsgard.addAll(AsgardSources);
			}
			transferFromEarth = topologicalSortHelper(graph, inDegree, location, EARTH, ASGARD, qEarth, qAsgard);
			System.out.println("transferFromEarth " + transferFromEarth);
		}

		for(int i =0; i < inDegree.length; i++){
			inDegree[i] = 0;
		}

		inDegreeCalculation(graph, inDegree);

		if(AsgardSources.size() != 0){
			qAsgard.addAll(AsgardSources);
			if(EarthSources.size() != 0){
				qEarth.addAll(EarthSources);
			}
			transferFromAsgard = topologicalSortHelper(graph, inDegree, location, ASGARD, EARTH, qAsgard, qEarth);
			System.out.println("transferFromAsgard " + transferFromAsgard);
		}

		if(transferFromEarth < transferFromAsgard){
			return transferFromEarth;
		}
		return transferFromAsgard;

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
//				System.out.println("neighbor" + neighbour + "inDegree" + inDegree[neighbour]);
				// same planet and in degree == 0;
				if(inDegree[neighbour] == 0){

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
				inDegrees[temp.get(j)] ++;
			}
		}
	}
	public static ArrayList<Integer> allZeroDegreeNodeByPlanet(int[] inDegree, int[] location, int planetNum){
		// initialize to -1
		ArrayList<Integer> allZeroDegreeNodes = new ArrayList<>();
		for(int i = 0; i < inDegree.length; i++){
			if(location[i] == planetNum && inDegree[i] == 0){
				allZeroDegreeNodes.add(i);
			}
		}
		return  allZeroDegreeNodes;
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
////		int[] location = {1,2,1,2,1};
//		int[] location = {2,1,2,1,2};
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

//		Hashtable<Integer, ArrayList<Integer>> graph3 = new Hashtable<>();
//		ArrayList<Integer> key0 = new ArrayList<>();
//		ArrayList<Integer> key1 = new ArrayList<>();
//		ArrayList<Integer> key2 = new ArrayList<>();
//		ArrayList<Integer> key3 = new ArrayList<>();
//		ArrayList<Integer> key4 = new ArrayList<>();
//
//		key0.add(1);
//		key0.add(3);
//		key0.add(4);
//		key0.add(2);
//		key2.add(3);
//		key2.add(1);
//		key2.add(4);
//		key3.add(1);
//		key4.add(1);
//		key4.add(3);
//
//		graph3.put(0, key0);
//		graph3.put(1, key1);
//		graph3.put(2, key2);
//		graph3.put(3, key3);
//		graph3.put(4, key4);
//
//		int[] location = {2,2,2,1,2};
//		int moves = rings(graph3, location);
//		System.out.println(moves);

		Hashtable<Integer, ArrayList<Integer>> graph4 = new Hashtable<>();
		ArrayList<Integer> key0 = new ArrayList<>();
		ArrayList<Integer> key1 = new ArrayList<>();
		ArrayList<Integer> key2 = new ArrayList<>();
		ArrayList<Integer> key3 = new ArrayList<>();
		ArrayList<Integer> key4 = new ArrayList<>();
		ArrayList<Integer> key5 = new ArrayList<>();
		ArrayList<Integer> key6 = new ArrayList<>();
		ArrayList<Integer> key7 = new ArrayList<>();

		key0.add(3);
		key0.add(4);
		key1.add(5);
		key2.add(5);
		key2.add(6);
		key3.add(7);
		key4.add(6);

		graph4.put(0, key0);
		graph4.put(1, key1);
		graph4.put(2, key2);
		graph4.put(3, key3);
		graph4.put(4, key4);
		graph4.put(5, key5);
		graph4.put(6, key6);
		graph4.put(7, key7);

		int[] location = {1,1,2,1,2,1,2,2};
		int moves = rings(graph4, location);
		System.out.println("total transfers " +moves);

//		Hashtable<Integer, ArrayList<Integer>> graph5 = new Hashtable<>();
//		ArrayList<Integer> key0 = new ArrayList<>();
//		ArrayList<Integer> key1 = new ArrayList<>();
//		ArrayList<Integer> key2 = new ArrayList<>();
//		ArrayList<Integer> key3 = new ArrayList<>();
//		ArrayList<Integer> key4 = new ArrayList<>();
//		ArrayList<Integer> key5 = new ArrayList<>();
//		ArrayList<Integer> key6 = new ArrayList<>();
//
//		key0.add(2);
//		key0.add(3);
//		key1.add(2);
//		key1.add(3);
//		key3.add(4);
//		key4.add(6);
//		key5.add(3);
//		key5.add(4);
//
//		graph5.put(0, key0);
//		graph5.put(1, key1);
//		graph5.put(2, key2);
//		graph5.put(3, key3);
//		graph5.put(4, key4);
//		graph5.put(5, key5);
//		graph5.put(6, key6);
//
//		int[] location = {2,1,2,2,1,1,2};
//		int moves = rings(graph5, location);
//		System.out.println(moves);
	}

}
