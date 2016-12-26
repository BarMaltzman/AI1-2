package graphs;
import java.util.HashMap;
import java.util.Map;

import normalAgents.Agent;


public class DisjakstraHeuristic implements Heuristic {

	@Override
	public double evaluate(Graph graph,Agent agent) {
		Map<Vertex,Double> dist = new HashMap<Vertex,Double>();
		Graph.dijkstra(graph, graph.getAgentCurrentVertex(agent), dist, new HashMap<Vertex,Vertex>(), agent,true,false);
		return dist.get(graph.getAgentGoalVertex(agent));
	}

}
