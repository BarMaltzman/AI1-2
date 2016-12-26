package graphs;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import normalAgents.Agent;


public class Graph {
	private ArrayList <Vertex> vertexes;
	private ArrayList <Edge> edges;
	private Map<Agent,Pair<Vertex,Vertex>> agentsLocations;
	private Map<Agent,List<Key>> agentsKeys;
	private int size;

	public Graph(int size) {
		this.size = size;
		vertexes = new ArrayList<Vertex>();
		for(int i=0 ; i < size ; i++)
			vertexes.add(i, new Vertex(i));
		edges = new ArrayList<Edge>();
		agentsLocations = new HashMap<Agent,Pair<Vertex,Vertex>>();
		agentsKeys = new HashMap<Agent,List<Key>>();
	}
	public Graph(Graph other){
		this.vertexes = new ArrayList<Vertex>();
		this.agentsLocations = new HashMap<Agent,Pair<Vertex,Vertex>>();
		this.agentsKeys = new HashMap<Agent,List<Key>>();
		this.size = other.size;
		for(int i=0 ; i < size ; i++){
			vertexes.add(i, new Vertex(i));
			for(Key key : other.vertexes.get(i).getKeys())
				vertexes.get(i).addKey(new Key(key));
			for(Lock lock : other.vertexes.get(i).getLocks())
				vertexes.get(i).addLock(new Lock(lock));
		}
		edges = new ArrayList<Edge>();
		for(Edge e : other.edges)
			this.addEdge(e.getV1().getId(),e.getV2().getId(), e.getWeight());
		for (Agent agent : other.agentsLocations.keySet()){
			Pair<Vertex,Vertex> location = other.agentsLocations.get(agent);
			agentsLocations.put(agent, new Pair<Vertex,Vertex>(vertexes.get(location.getLeft().getId()),vertexes.get(location.getRight().getId())));
			List<Key> newKeys = new ArrayList<Key>();
			for (Key key : other.agentsKeys.get(agent))
				newKeys.add(new Key(key));
			agentsKeys.put(agent, newKeys);
		}
	}
	@Override
	public String toString() {
		String res = "#V "+vertexes.size() +"\n";
		for (Edge e : edges)
			res += e +"\n";
		for (Vertex v : vertexes)
			res += v + "\n";

		for (Agent a : agentsLocations.keySet())
			res += a +" in vertex:"+ agentsLocations.get(a).getLeft().getId() + " Keys:"+agentsKeys.get(a)+"\n";

		return res;
	}
	public ArrayList<Vertex> getVertixes() {
		return vertexes;
	}
	public void setVertixes(ArrayList<Vertex> vertixes) {
		this.vertexes = vertixes;
	}
	public ArrayList<Edge> getEdges() {
		return edges;
	}
	public void setEdges(ArrayList<Edge> edges) {
		this.edges = edges;
	}
	// moves an agent from current vertex to destination. returns true if moved successfully.
	public boolean moveAgent(Agent a, int destinationNumber) {
		Vertex destination = null;
		for (Vertex v : vertexes)
			if(v.getId() == destinationNumber)
				destination = v;
		for (Lock lock : destination.getLocks())
			if(lock.isLockingStatus() && Key.containsKey(agentsKeys.get(a),lock.getId())){
				lock.setLockingStatus(false);
			}
		if(!destination.isLocked()){
			agentsLocations.get(a).setLeft(destination);
			agentsKeys.get(a).addAll(destination.getKeys());
			destination.getKeys().clear();
			return true;
		}
		return false;
	}

	public int size() {
		return vertexes.size();
	}
	public List<Vertex> getNeighbors(Vertex v) {
		return v.getNeighbors();
	}
	public Double getWeight(Vertex v1, Vertex v2) {
		for(Edge e: edges){
			if((e.getV1().equals(v1)&&e.getV2().equals(v2)||
					(e.getV1().equals(v2)&&e.getV2().equals(v1)))){
				return e.getWeight();
			}
		}
		if(v2 ==null || v1.getId() == v2.getId())
			return 1.0; // no-op
		else
			return  Double.MAX_VALUE;
	}
	public double getWeight(int id1, int id2) {
		Vertex v1 = null;
		Vertex v2 = null;
		for (Vertex v : vertexes){
			if(v.getId() == id1)
				v1 = v;
			else if(v.getId() == id2)
				v2 = v;
		}
		return getWeight(v1, v2);
	}
	public void addKeyToVertex(int currVertexId, Key key) {
		vertexes.get(currVertexId).addKey(key);	
	}
	public void addLockToVertex(int currVertexId, Lock lock) {
		vertexes.get(currVertexId).addLock(lock);
	}
	public void addEdge(int v1num, int v2num, double weight) {
		Vertex v1 = vertexes.get(v1num);
		Vertex v2 = vertexes.get(v2num);
		edges.add(new Edge(v1,v2,weight));
		v1.addNeighbor(v2);
		v2.addNeighbor(v1);
	}

	public void addAgent(Agent agent,Vertex current,Vertex goal) {
		agentsLocations.put(agent, new Pair<Vertex,Vertex>(current,goal));
		agentsKeys.put(agent,new ArrayList<Key>());
		agentsKeys.get(agent).addAll(current.getKeys());
		current.getKeys().clear();
	}
	public void addAgent(Agent agent,int currentId,int goalId) {
		addAgent(agent,vertexes.get(currentId),vertexes.get(goalId));
	}


	public static void dijkstra (Graph G, Vertex s,Map<Vertex,Double> dist,Map<Vertex,Vertex> pred,Agent agent){
		dijkstra(G, s,dist,pred,agent,true,true);
	}
	public static void dijkstra (Graph G, Vertex s,Map<Vertex,Double> dist,Map<Vertex,Vertex> pred,Agent agent,boolean considerWeights, boolean considerLcoks) {
		int size = G.size();
		List<Vertex> visited = new ArrayList<Vertex>();
		for (int i=0; i < size; i++) 
			dist.put(G.getVertixes().get(i),  Double.MAX_VALUE);
		dist.put(s,0.0);
		for (int i=0; i<size; i++) {
			Vertex next = minVertex(visited,dist);
			visited.add(next);

			// The shortest path to next is dist[next] and via pred[next].

			List<Vertex>  n = G.getNeighbors(next);
			for (int j=0; j<n.size(); j++) {
				Vertex v = n.get(j);
				double d = 0;
				/// to check locked path if has key
				if(v.isLocked() && considerLcoks && !v.canOpen(G.agentsKeys.get(agent)))
					d =  Double.MAX_VALUE;
				else{
					double d1 = dist.get(next);
					double d2 = 1;
					if(considerWeights)
						d2 = G.getWeight(next, v);
					d = d1+d2;
				}


				if (dist.get(v) > d) {
					dist.put(v, d);
					pred.put(v,next);
				}
			}
		}
	}

	private static Vertex minVertex (List<Vertex> visited,Map<Vertex,Double> dist) {
		Double x = Double.MAX_VALUE;
		Vertex y = null;   // graph not connected, or no unvisited vertices
		for (Vertex v: dist.keySet()) {
			if (!visited.contains(v) && dist.get(v) <= x) {y=v; x=dist.get(v);}
		}
		return y;
	}
	public Vertex getAgentCurrentVertex(Agent agent) {
		return agentsLocations.get(agent).getLeft();
	}
	public Vertex getAgentGoalVertex(Agent agent) {
		return agentsLocations.get(agent).getRight();
	}
	public void setAgentGoalVertex(Agent agent, Vertex dest) {
		agentsLocations.get(agent).setRight(dest);
	}
	public boolean hasAllAgentsFinished() {
		for (Agent a: agentsLocations.keySet())
			if(!a.finished(this))
				return false;
		return true;
	}
	public List<Agent> getUnfinishedAgents() {
		List<Agent> ans = new ArrayList<Agent>();
		for(Agent agent : agentsLocations.keySet())
			if(!agent.finished(this))
				ans.add(agent);	
		ans.sort((Agent a0,Agent a1) -> {return a0.getId() - a1.getId();});
		return ans;
	}
	public boolean equals(Object other) {
		if(other instanceof Graph){
			Graph otherGraph = (Graph) other;
			for(Vertex v: vertexes)
				if(!otherGraph.vertexes.contains(v))
					return false;
			for(Agent a: agentsLocations.keySet())
				if(!(agentsLocations.get(a).equals(otherGraph.agentsLocations.get(a))))
					return false;

			for(Agent a: agentsKeys.keySet())
				for(Key k: agentsKeys.get(a))
					if(!(otherGraph.agentsKeys.get(a).contains(k)))
						return false;
			return true;
		}
		return false;
	}
	public List<Key> getAgentKeys(Agent agent) {
		return agentsKeys.get(agent);
	}

	public double calculatePathWeight(List<Integer> path) {
		double ans = 0;
		for (int i = 0 ; i < path.size()-1 ; i++){
			ans += this.getWeight(path.get(i), path.get(i+1));
		}
		return ans;
	}
	public Set<Agent> getAgents() {
		return agentsKeys.keySet();
	}

}
