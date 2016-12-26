package gameAgents;
import graphs.Graph;
import graphs.Vertex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import normalAgents.Agent;


public class SemiCoopAgent extends GameAgent {
	
	private Vertex nextMove;
	private Agent[] agents;
	
	public SemiCoopAgent(int id, GameAgent otherAgent) {
		super(id,otherAgent);
		agents = new Agent[2];
		agents[0] = this;
		agents[1] = otherAgent;
	}
	
	
	public Vertex observeWorld(Graph graph) {
		int[] moves = {0,0};
		maxValue(graph,0,0,moves);
		return nextMove;
	}
	public int[] cutOff(Graph graph)
	{
		int [] ans= new int [2];  
		Vertex agent1Dest = graph.getAgentGoalVertex(this);
		Vertex agent1Current = graph.getAgentCurrentVertex(this);
		Vertex agent2Dest = graph.getAgentGoalVertex(getOtherAgent());
		Vertex agent2Current = graph.getAgentCurrentVertex(getOtherAgent());
		Map<Vertex,Double> dist = new HashMap<Vertex,Double>();
		Map<Vertex,Vertex> pred = new HashMap<Vertex,Vertex>();
		Graph.dijkstra(graph, agent1Current, dist, pred, this,true,true);
		ans[0] = -dist.get(agent1Dest).intValue();
		dist.clear();
		pred.clear();
		Graph.dijkstra(graph, agent2Current, dist, pred, this.getOtherAgent(),true,true);
		ans[1] = -dist.get(agent2Dest).intValue();
		return ans;
	}
	public int[] maxValue(Graph graph,int depth,int player,int[] moves){
		if(isTerminal(graph)){
			int[] ans = {-moves[0],-moves[1]};
			return ans;
		}
		if(depth > 6){
			int[] ans = cutOff(graph);
			ans[0] = ans[0] - moves[0];
			ans[1] = ans[1] - moves[1];
			return ans;
		}
		
		
		int[] value = {Integer.MIN_VALUE,Integer.MIN_VALUE};

		Vertex current = graph.getAgentCurrentVertex(agents[player]);
		List<Vertex> neighbors = current.getNeighbors();
	
		Vertex best = null;
		for(Vertex v: neighbors){
			Graph newGraph = new Graph(graph);
			
			if(newGraph.moveAgent(agents[player], v.getId())){	
				int[] newMoves = new int[2];
				newMoves[player] = moves[player]+1;
				newMoves[1-player] = moves[1-player];
				int[] newValue;
				
				if(!agents[1-player].finished(newGraph))
					newValue = maxValue(newGraph,depth+1,1-player,newMoves);
				else
					newValue = maxValue(newGraph,depth+1,player,newMoves);
				if(depth==0){
					depth++;
					depth--;
				}
				if(newValue[player] > value[player] || (newValue[player] == value[player] && newValue[1-player] > value[1-player])){
					value[0] = newValue[0];
					value[1] = newValue[1];
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
	public void setOtherAgent(GameAgent otherAgent) {
		this.otherAgent = otherAgent;
		agents[1] = otherAgent;
	}


	@Override
	public String getName() {
		return "SemiCoopAgent";
	}


}
