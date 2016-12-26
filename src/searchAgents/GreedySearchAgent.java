package searchAgents;
import graphs.Graph;
import graphs.Heuristic;
import graphs.Pair;

import java.util.List;

import normalAgents.Agent;

public class GreedySearchAgent extends SearchAgent {
	public GreedySearchAgent(int id, Heuristic heuristic,Agent otherAgent){
		super(id,heuristic,null,otherAgent);
		this.setStratagy( (Pair<List<Integer>, Graph> o1,Pair<List<Integer>, Graph> o2)-> 
		{return (int)(heuristic.evaluate(o1.getRight(),this) - heuristic.evaluate(o2.getRight(),this));});
	}
	public GreedySearchAgent(int id, Heuristic heuristic){
		this(id,heuristic,null);
	}
	@Override
	public String getName() {
		return "Greedy Search Agent";
	}
}
