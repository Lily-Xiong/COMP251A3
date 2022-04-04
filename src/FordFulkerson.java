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

		boolean breakOuterLoop = false;
		while (!stack.isEmpty() && !breakOuterLoop) {
			int current = stack.pop();
			if (!isVisited[current]) {
				isVisited[current] = true;
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
				}
			}
		}


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


		WGraph newGraph = new WGraph();
		newGraph.setSource(source);
		newGraph.setDestination(destination);
		for (Edge e : graph.getEdges()) {
			int node1 = e.nodes[0];
			int node2 = e.nodes[1];
			Edge residualGraphBackwardEdge = residualGraph.getEdge(node2, node1);
			if (residualGraphBackwardEdge == null) {
				Edge newEdge = new Edge(node1, node2, 0);
				newGraph.addEdge(newEdge);
			} else{
				Edge newEdge = new Edge(node1, node2, residualGraph.getEdge(node2, node1).weight);
				newGraph.addEdge(newEdge);
			}
		}

		graph = new WGraph(newGraph);

		answer += maxFlow + "\n" + graph.toString();	
		return answer;
	}



	 public static void main(String[] args){
		String file = args[0];
		File f = new File(file);
		WGraph g = new WGraph(file);
		System.out.println(fordfulkerson(g));
	 }
}

