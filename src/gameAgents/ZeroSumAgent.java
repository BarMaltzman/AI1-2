package gameAgents;
import graphs.Graph;
import graphs.Vertex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import normalAgents.Agent;


public class ZeroSumAgent extends GameAgent {
	private int globalL;
	public ZeroSumAgent(int id,GameAgent otherAgent,int l) {
		super(id,otherAgent);
		this.globalL =l;
	}

	@Override
	public String getName() {
		
		return "Zero-Sum";
	}

	@Override
	public Vertex observeWorld(Graph graph) {
		int value = maxValue(graph,0,-1 ,1,globalL);
		globalL--;
		if(value==-1)
			return null;
		return nextMove;
	}
	
	public int maxValue(Graph graph,int depth, int alpha, int beta,int l){
		if(isTerminal(graph) || l==0)
			return utility(graph);
		int value = -1;
		
		Vertex current = graph.getAgentCurrentVertex(this);
		
		List<Vertex> neighbors = current.getNeighbors();
		if (depth > 9){
			return cutoffValue(graph,current);
		}

		Vertex best = null;
		for(Vertex v: neighbors){
			Graph newGraph = new Graph(graph);
			
			if(newGraph.moveAgent(this, v.getId())){	
				
				int newValue = minValue(newGraph,depth+1,alpha,beta,l-1);
				if(depth==0){
					depth++;
					depth--;
				}
				if(newValue >= value){
					value = newValue;
					if(newValue > alpha)
						alpha = newValue;
					best = v;
					if(value == 1 || (beta <= value))
						break;
				}
			}


		}
		if(depth==0)
			nextMove = best;
		return value;
		
	}
	
	public int minValue(Graph graph,int depth, int alpha, int beta,int l){
		if(isTerminal(graph) || l==0)
			return utility(graph);
		int value = 1;
		Vertex current = graph.getAgentCurrentVertex(getOtherAgent());
		if (depth > 15){
			return cutoffValue(graph,current);
		}
		List<Vertex> neighbors = current.getNeighbors();
		for(Vertex v: neighbors){
			Graph newGraph = new Graph(graph);
			if(newGraph.moveAgent(getOtherAgent(), v.getId())){
				int newValue = maxValue(newGraph,depth+1, alpha, beta,l);
				if(newValue < value){
					value = newValue;
					if(newValue < beta)
						beta = newValue;
				}
				if(value == -1 || alpha >= value)
					break;
			}
		}
		return value;
		
	}
	
	public int cutoffValue(Graph graph, Vertex current){
		// cut-off
		Vertex agent1Dest = graph.getAgentGoalVertex(this);
		Vertex agent2Current = graph.getAgentCurrentVertex(otherAgent);
		Vertex agent2Dest = graph.getAgentGoalVertex(otherAgent);
		Map<Vertex,Double> dist = new HashMap<Vertex,Double>();
		Map<Vertex,Vertex> pred = new HashMap<Vertex,Vertex>();
		Graph.dijkstra(graph, current, dist, pred, this,true,true);
		double agent1_dist = dist.get(agent1Dest);
		dist.clear();
		pred.clear();
		Graph.dijkstra(graph, agent2Current, dist, pred, otherAgent,true,true);
		double agent2_dist = dist.get(agent2Dest);
		if(agent1_dist < agent2_dist)
			return 1;
		else
			return -1;
		
	}
	
	public int utility(Graph graph){
		if(this.finished(graph))
			return 1;
		else if(otherAgent.finished(graph))
			return -1;
		else
			return 0;
	}
	
	public boolean isTerminal(Graph graph){
		for(Agent agent : graph.getAgents())
			if (agent.finished(graph))
				return true;
		return false;
	}


	

}

