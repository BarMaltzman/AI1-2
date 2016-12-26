package gameAgents;
import graphs.Graph;
import graphs.Vertex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import normalAgents.Agent;


public class FullyCoopAgent extends GameAgent{
	public FullyCoopAgent(int id,FullyCoopAgent otherAgent) {
		super(id,otherAgent);
	}
	@Override
	public String getName() {
		return "FullyCoopAgent";
	}
	public double cutOff(Graph graph)
	{
		Vertex agent1Dest = graph.getAgentGoalVertex(this);
		Vertex agent1Current = graph.getAgentCurrentVertex(this);
		Vertex agent2Dest = graph.getAgentGoalVertex(getOtherAgent());
		Vertex agent2Current = graph.getAgentCurrentVertex(getOtherAgent());
		Map<Vertex,Double> dist = new HashMap<Vertex,Double>();
		Map<Vertex,Vertex> pred = new HashMap<Vertex,Vertex>();
		Graph.dijkstra(graph, agent1Current, dist, pred, this,false,false);
		double agent1_dist = dist.get(agent1Dest);
		dist.clear();
		pred.clear();
		Graph.dijkstra(graph, agent2Current, dist, pred, this.getOtherAgent(),false,false);
		double agent2_dist = dist.get(agent2Dest);
		if(agent1_dist < 0 || agent2_dist < 0)
			return agent1_dist;
			
		return -(agent1_dist+agent2_dist);
	}
	public Vertex observeWorld(Graph graph) {
		nextMove=null;
		maxValue(graph,0,0);
		return nextMove;
	}
	
	public int maxValue(Graph graph,int depth,int player){
		if(isTerminal(graph))
			return -depth;
		
		int value = Integer.MIN_VALUE;
		
		Agent curAgent = (player == 0)? this : otherAgent;
		Agent nexAgent = (player == 0)? otherAgent : this;
		
		Vertex current = graph.getAgentCurrentVertex(curAgent);
		List<Vertex> neighbors = current.getNeighbors();
		
		
		if(depth > 8){
			return (int)cutOff(graph) - depth;
		}
		Vertex best = null;
		for(Vertex v: neighbors){
			Graph newGraph = new Graph(graph);
			if(newGraph.moveAgent(curAgent, v.getId())){	
				int newValue;
				if(!nexAgent.finished(newGraph))
			
					newValue = maxValue(newGraph,depth+1,1-player);
				else
					newValue = maxValue(newGraph,depth+1,player);
				if(depth==0){
					depth++;
					depth--;
				}
				if(newValue >= value){
					value = newValue;
					best = v;
				}
			}


		}
		if(depth==0)
			nextMove = best;
		return value;
		
	}
	public boolean isTerminal(Graph graph) {
		return (this.finished(graph)&&this.getOtherAgent().finished(graph));
	}


	
}
