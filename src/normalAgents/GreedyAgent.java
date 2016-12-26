package normalAgents;

import graphs.Graph;
import graphs.Vertex;

import java.util.HashMap;
import java.util.Map;


public class GreedyAgent extends Agent {

    
    public GreedyAgent(int id) {
		super(id);
	}

	@Override
	public Vertex observeWorld(Graph graph) {
		Map <Vertex,Vertex> pred = new HashMap <Vertex,Vertex>();  // preceeding node in path
	    Map<Vertex,Double> dist = new HashMap<Vertex,Double>();
		Graph.dijkstra(graph, graph.getAgentCurrentVertex(this),dist,pred,this);
		if(dist.get(graph.getAgentGoalVertex(this)) < Double.MAX_VALUE){
			Vertex currVertex = graph.getAgentGoalVertex(this);
			Vertex predVertex = pred.get(currVertex);
			while(predVertex != graph.getAgentCurrentVertex(this)){
				Vertex temp = predVertex;
				predVertex = pred.get(predVertex);
				currVertex = temp;
			}
			return currVertex;
		}
			 
		return null;
	}


 
	@Override
	public String getName() {
		return "Greedy";
	}

}
