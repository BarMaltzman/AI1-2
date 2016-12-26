package normalAgents;
import graphs.Graph;
import graphs.Vertex;

import java.util.Scanner;


public class HumanAgent extends Agent {

	public HumanAgent(int id) {
		super(id);
	}

	@Override
	public Vertex observeWorld(Graph graph) {
		System.out.println(graph);
		System.out.println("type a destination verex id");
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		int vertexId = scanner.nextInt();
		Vertex destinationVertex = graph.getVertixes().get(vertexId);
		if (graph.getAgentCurrentVertex(this).getNeighbors().contains(destinationVertex))
			return destinationVertex;
		else
			return null;
	}

	@Override
	public String getName() {
		return "Human";
	}
	

	
}
