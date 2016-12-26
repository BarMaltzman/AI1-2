package searchAgents;
import graphs.Graph;
import graphs.Heuristic;
import graphs.Pair;
import graphs.Vertex;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import normalAgents.Agent;

public abstract class SearchAgent extends Agent {
	private Heuristic heuristic;
	protected boolean calculatedPath;
	protected List<Integer> vertexNumberPath;
	protected List<Graph> closedList;
	private int expansionSteps;
	protected Agent otherAgent;
	private Comparator<Pair<List<Integer>,Graph>> stratagy;
	public SearchAgent(int id, Heuristic heuristic,Comparator<Pair<List<Integer>,Graph>> stratagy,Agent otherAgent){
		super(id);
		this.setExpansionSteps(0);
		this.heuristic = heuristic;
		this.calculatedPath = false;
		this.vertexNumberPath = new ArrayList<Integer>();
		this.closedList = new ArrayList<Graph>();
		this.otherAgent = otherAgent;
		this.setStratagy(stratagy);
	}
	public SearchAgent(int id, Heuristic heuristic,Comparator<Pair<List<Integer>,Graph>> stratagy){
		this(id,heuristic,stratagy,null);
	}
	@Override
	public Vertex observeWorld(Graph graph) {
		if(!calculatedPath){
			Queue<Pair<List<Integer>,Graph>> fringe = new PriorityQueue<Pair<List<Integer>,Graph>>(getStratagy());
			List<Integer> path = new ArrayList<Integer>();
			Graph simulator_graph = new Graph(graph);
			path.add(simulator_graph.getAgentCurrentVertex(this).getId());
			fringe.offer(new Pair<List<Integer>,Graph>(path,simulator_graph));
			vertexNumberPath = treeSearch(fringe,otherAgent);
			vertexNumberPath.remove(0);
			calculatedPath = true;
		}
		if(vertexNumberPath.size() > 0)
			return graph.getVertixes().get(vertexNumberPath.remove(0));
		else
			return null;
	}
	public List<Integer> treeSearch(Queue<Pair<List<Integer>,Graph>> fringe,Agent otherAgent){
		return treeSearch(fringe,-1,otherAgent);
	}
	public List<Integer> treeSearch(Queue<Pair<List<Integer>,Graph>> fringe){
		return treeSearch(fringe,-1,null);
	}
	public List<Integer> treeSearch(Queue<Pair<List<Integer>,Graph>> fringe,int limit){
		return treeSearch(fringe,limit,null);
	}
	public List<Integer> treeSearch(Queue<Pair<List<Integer>,Graph>> fringe,int limit,Agent otherAgent){
		if(fringe.isEmpty())
			return null;
		Pair<List<Integer>,Graph> selectedNode = fringe.poll();
		Graph graph = selectedNode.getRight();
		List<Integer> path = selectedNode.getLeft();
		if(limit==0)
			return path;
		closedList.add(graph);
		if(this.finished(graph))
			return path;
		else{
			List<Pair<Vertex,Graph>> options = expend(graph);
			for (Pair<Vertex,Graph> option : options){
				List<Integer> newPath = new ArrayList<Integer>(path);
				newPath.add(option.getLeft().getId());
				fringe.add(new Pair<List<Integer>,Graph>(newPath,option.getRight()));
			}
			return treeSearch(fringe,limit-1,otherAgent);
		}

	}
	
	private List<Pair<Vertex,Graph>> expend(Graph graph){
		setExpansionSteps(getExpansionSteps() + 1);
		List<Pair<Vertex,Graph>> ans = new ArrayList<Pair<Vertex,Graph>>();
		for (Vertex v : graph.getAgentCurrentVertex(this).getNeighbors()){
			if(v.canOpen(graph.getAgentKeys(this))){
				Graph g = new Graph(graph);
				g.moveAgent(this, v.getId());
				if(otherAgent !=null && !otherAgent.finished(g)){
					Vertex action = otherAgent.observeWorld(g);
					g.moveAgent(otherAgent, action.getId());
				}
				if(!closedList.contains(g))
					ans.add(new Pair<Vertex,Graph>(v,g));
			}
		}
		return ans;
	}
	public Heuristic getHeuristic() {
		return heuristic;
	}
	public void setHeuristic(Heuristic heuristic) {
		this.heuristic = heuristic;
	}

	public Comparator<Pair<List<Integer>,Graph>> getStratagy() {
		return stratagy;
	}

	public void setStratagy(Comparator<Pair<List<Integer>,Graph>> stratagy) {
		this.stratagy = stratagy;
	}

	public int getExpansionSteps() {
		return expansionSteps;
	}

	public void setExpansionSteps(int expansionSteps) {
		this.expansionSteps = expansionSteps;
	}
}