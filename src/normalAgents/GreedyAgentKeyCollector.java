package normalAgents;
import graphs.Graph;
import graphs.Vertex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GreedyAgentKeyCollector extends Agent {

	public GreedyAgentKeyCollector(int id) {
		super(id);
		
	}

	@Override
	public Vertex observeWorld(Graph graph) {
		Map<Vertex,Double> dist = new HashMap<Vertex,Double>();
		Map<Vertex,Vertex> pred = new HashMap<Vertex,Vertex>();
		Graph.dijkstra(graph, graph.getAgentCurrentVertex(this),dist,pred,this,false,true);
		
		double min = Double.MAX_VALUE;
		Vertex dest = null;
		for(Vertex v : dist.keySet()){
			if(dist.get(v) < min && !v.getKeys().isEmpty()){
				min = dist.get(v);
				dest = v;
			}
		}
		
		//graph.setAgentGoalVertex(this,dest);
		Vertex currVertex = dest;
		if(currVertex != null){
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
		return "Greedy Key Collector";
	}
	@Override
	public boolean finished(Graph graph){
		List<Vertex> vertexs = graph.getVertixes();
		for (Vertex v: vertexs)
			if(!v.getKeys().isEmpty())
				return false;
		return true;
	}

}
