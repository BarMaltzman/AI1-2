package searchAgents;
import graphs.Graph;
import graphs.Heuristic;
import graphs.Pair;

import java.util.List;

import normalAgents.Agent;

public class AStarSearchAgent extends SearchAgent {
	public AStarSearchAgent(int id, Heuristic heuristic,Agent otherAgent){
		super(id,heuristic,null,otherAgent);
		this.setStratagy( (Pair<List<Integer>, Graph> o1,Pair<List<Integer>, Graph> o2)-> 
		{
			Graph graph1 = o1.getRight();
			List<Integer> path1 = o1.getLeft();
			Graph graph2 = o2.getRight();
			List<Integer> path2 = o2.getLeft();
			double g1 = graph1.calculatePathWeight(path1);
			double g2 = graph2.calculatePathWeight(path2);
			return (int)((heuristic.evaluate(o1.getRight(),this)+g1) - (heuristic.evaluate(o2.getRight(),this)+g2));});
	}
	public AStarSearchAgent(int id, Heuristic heuristic){
		this(id,heuristic,null);
	}

	@Override
	public String getName() {
		return "A* Agent";
	}
}
