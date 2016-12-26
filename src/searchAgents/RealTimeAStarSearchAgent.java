package searchAgents;
import graphs.Graph;
import graphs.Heuristic;
import graphs.Pair;
import graphs.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import normalAgents.Agent;


public class RealTimeAStarSearchAgent extends SearchAgent {
	private int limit;
	public RealTimeAStarSearchAgent(int id, Heuristic heuristic,int limit,Agent otherAgent) {
		super(id,heuristic,null,otherAgent);
		this.limit = limit;
		this.setStratagy( (Pair<List<Integer>, Graph> o1,Pair<List<Integer>, Graph> o2)-> 
		{
			Graph graph1 = o1.getRight();
			List<Integer> path1 = o1.getLeft();
			Graph graph2 = o2.getRight();
			List<Integer> path2 = o2.getLeft();
			double g1 = graph1.calculatePathWeight(path1);
			double h1 = heuristic.evaluate(graph1,this);
			double g2 = graph2.calculatePathWeight(path2);
			double h2 = heuristic.evaluate(graph2,this);
			return (int)((h1+g1) - (h2+g2));});
	}
	public RealTimeAStarSearchAgent(int id, Heuristic heuristic,int limit){
		this(id,heuristic,limit,null);
	}
	public Vertex observeWorld(Graph graph) {
		Queue<Pair<List<Integer>,Graph>> fringe = new PriorityQueue<Pair<List<Integer>,Graph>>(getStratagy());
		List<Integer> path = new ArrayList<Integer>();
		path.add(graph.getAgentCurrentVertex(this).getId());
		fringe.offer(new Pair<List<Integer>,Graph>(path,graph));
		vertexNumberPath = treeSearch(fringe,limit,otherAgent);
		vertexNumberPath.remove(0);
		closedList.clear();
		if(vertexNumberPath.size() > 0)
			return graph.getVertixes().get(vertexNumberPath.remove(0));
		
		return null;
	}

	@Override
	public String getName() {
		return "Real Time A* Search Agent";
	}

}
