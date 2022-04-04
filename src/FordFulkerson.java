import java.util.*;
import java.io.File;

public class FordFulkerson {

	public static ArrayList<Integer> pathDFS(Integer source, Integer destination, WGraph graph) {

		ArrayList<Integer> path = new ArrayList<Integer>();
		/* YOUR CODE GOES HERE*/

		ArrayList<Edge> edges = graph.getEdges();
		Stack<Integer> stack = new Stack<>();
		boolean[] isVisited = new boolean[graph.getNbNodes()];
		int[] parent = new int[graph.getNbNodes()];
		for (int i = 0; i < parent.length; i++) {
			parent[i] = -1;
		}

		stack.push(source);
		// todo: maybe terminate early if adjNode is destination
		boolean breakOuterLoop = false;
		while (!stack.isEmpty() && !breakOuterLoop) {
			int current = stack.pop();
			if (!isVisited[current]) {
				isVisited[current] = true;
//				visit(current);
				for (Edge edge : edges) {
					if (edge.nodes[0] == current && edge.weight > 0) {
						int adjNode = edge.nodes[1];
						if (!isVisited[adjNode]) {
							if (adjNode == destination) {
								breakOuterLoop = true;
							}
							stack.push(adjNode);
							parent[adjNode] = current;
						}
					}
//					if (edge.nodes[1] == current && edge.weight > 0) {
//						int adjNode = edge.nodes[0];
//						if (!isVisited[adjNode]) {
//							if (adjNode == destination) {
//								breakOuterLoop = true;
//							}
//							stack.push(adjNode);
//							parent[adjNode] = current;
//						}
//					}
				}
			}
		}

			// in the beginning residual graph = graph itself
//		WGraph residual = new WGraph(graph);
//		Stack<Integer> stack = new Stack<>();
//		stack.push(source);
//		HashMap<Edge, Boolean> visited= new HashMap<>();
//		for(Edge e : graph.getEdges()){
//			if (visited.get(e) == null){
//				pathDFSHelper(source, destination, graph, residual, visited, e, stack);
//			}
//
//		}

		if (parent[destination] == -1) {
			return path;
		} else {
			int currentNode = destination;
			while (currentNode != source) {
				path.add(currentNode);
				currentNode = parent[currentNode];
			}
			path.add(source);
			Collections.reverse(path);
		}
		return path;
	}

//	public static ArrayList<Integer> pathDFSHelper(Integer source, Integer destination, WGraph graph, WGraph residual,
//												   HashMap<Edge, Boolean> visited, Edge e, Stack<Integer> stack){
//		visited.put(e, true);
//		Integer p = stack.pop();
//		while(!stack.isEmpty()){
//			for(Edge edge: graph.listOfEdgesSorted()){
//				if(visited.get(edge) == null && residual.getEdge(p, )){
//
//				}
//			}
//		}
//
//
//	}


	public static String fordfulkerson( WGraph graph){
		String answer="";
		int maxFlow = 0;

		WGraph residualGraph = new WGraph(graph);
		int source = graph.getSource();
		int destination = graph.getDestination();
		
		/* YOUR CODE GOES HERE		*/
		while (true) {
			ArrayList<Integer> path = pathDFS(source, destination, residualGraph);
			if (path.size() == 0) {
				break;
			}

			int pathFlow = Integer.MAX_VALUE;
			for (int i = 0; i < path.size() - 1; i++) {
				Integer node1 = path.get(i);
				Integer node2 = path.get(i + 1);
				Integer weight = residualGraph.getEdge(node1, node2).weight;
				pathFlow = Math.min(pathFlow, weight);
			}
			for (int i = 0; i < path.size() - 1; i++) {
				Integer node1 = path.get(i);
				Integer node2 = path.get(i + 1);

				Edge edgeForward = residualGraph.getEdge(node1, node2);
				edgeForward.weight = edgeForward.weight - pathFlow;

				Edge edgeBackward = residualGraph.getEdge(node2, node1);
				if (edgeBackward == null) {
					Edge newEdge = new Edge(node2, node1, pathFlow);
					residualGraph.addEdge(newEdge);
				} else {
					edgeBackward.weight = edgeBackward.weight + pathFlow;
				}
			}
			maxFlow = maxFlow + pathFlow;
		}

//		System.out.println("ck1");
//		System.out.println(graph);
//		System.out.println();
//		System.out.println(residualGraph);
//		System.out.println("ck2");
//		ArrayList<Edge> allForwardEdges = graph.getEdges();

		WGraph newGraph = new WGraph();
		newGraph.setSource(source);
		newGraph.setDestination(destination);
		for (Edge e : graph.getEdges()) {
			int node1 = e.nodes[0];
			int node2 = e.nodes[1];
			Edge residualGraphEdge = residualGraph.getEdge(node2, node1);
			if (residualGraphEdge == null){
				Edge newEdge = new Edge(node1, node2, 0);
				newGraph.addEdge(newEdge);
			} else{
				Edge newEdge = new Edge(node1, node2, residualGraph.getEdge(node2, node1).weight);
				newGraph.addEdge(newEdge);
			}
		}
//		for (Edge edge : residualGraph.getEdges()) {
//			int node1 = edge.nodes[0];
//			int node2 = edge.nodes[1];
//			if (graph.getEdge(node1, node2) == null) {
//				residualGraph.getEdges().remove(edge);
//			}
//		}

		graph = new WGraph(newGraph);

		answer += maxFlow + "\n" + graph.toString();	
		return answer;
	}

	// todo: remove this method
	public void updateEdge(Edge e, Integer newWeight) throws RuntimeException{
		e.weight = newWeight;
	}


	 public static void main(String[] args){
//		String file = args[0];
//		File f = new File(file);
//		WGraph g = new WGraph(file);
//		 String fordfulkerson = fordfulkerson(g);
//		 System.out.println(fordfulkerson);
		 WGraph g = new WGraph();
		 g.setSource(0);
		 g.setDestination(9);
		 Edge[] edges = new Edge[] {
				 new Edge(0, 1, 10),
				 new Edge(0, 2, 5),
				 new Edge(2, 3, 5),
				 new Edge(1, 3, 10),
				 new Edge(3, 4, 5),
				 new Edge(4, 5, 10),
				 new Edge(4, 6, 5),
				 new Edge(6, 7, 5),
				 new Edge(6, 8, 10),
				 new Edge(8, 9, 10),
		 };
		 Arrays.stream(edges).forEach(e->g.addEdge(e));
		 String result = FordFulkerson.fordfulkerson(g);
		 System.out.println(result);
	 }
}

