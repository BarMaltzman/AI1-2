package graphs;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class Vertex {
	private int id;
	private ArrayList <Lock> locks;
	//private ArrayList <Edge> edges;
	private ArrayList <Key> keys;
	//private ArrayList <Agent> agents;
	private ArrayList <Vertex> neighbors;
	private Map <Integer,Double> keysProbs;
	
	public Vertex(int id){
		this.setLocks(new ArrayList<Lock>());
		//this.edges = new ArrayList<Edge>();
		this.setKeys(new ArrayList<Key>());
		//this.agents = new ArrayList<Agent>();
		this.neighbors = new ArrayList<Vertex>();
		this.neighbors.add(this);
		this.setId(id);
	}

	public boolean isLocked(){
		for(Lock lock : getLocks())
			if(lock.isLockingStatus())
				return true;
		return false;
	}
	
	public void addNeighbor(Vertex v){
		neighbors.add(0, v);
	}

	public void addKey(Key key) 
	{
		getKeys().add(key);
		// TODO Auto-generated method stub
		
	}

	public void addLock(Lock lock) {
		getLocks().add(lock);
		// TODO Auto-generated method stub
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString() {
		String res =  "#V " + id+" keys: ";
		for(Key k : getKeys())
		{
			res += k.getId() + " ";
		}
		res+= "locks: ";
		for(Lock l: getLocks())
		{
			if(l.isLockingStatus())
				res+= l.getId() +" ";
		}/*
		res+= "agents: ";
		for(Agent a: agents)
		{
			res+= a +" ";
		}*/
		return res;
	}

	public List<Lock> getLocks() {
		return locks;
	}
	/*
	public static void moveAgent(Vertex v1,Vertex v2,Agent agent){
		v1.agents.remove(agent);
		v2.agents.add(agent);
		for(Key key : v2.getKeys()){
			agent.addKey(key);
		}
		v2.getKeys().clear();
			
		agent.setCurrent(v2);
	}

	public void addAgent(Agent agent) {
		agents.add(agent);
		
	}
*/
	public List<Vertex> getNeighbors() {
		return neighbors;
	}

	public boolean canOpen(List<Key> keys) {
		
		for (Lock lock : getLocks()){
			if(lock.isLockingStatus()){
				int sum = 0;
				for(Key key : keys){
					if (key.getId() == lock.getId())
						sum++;
				}
				if(sum == 0)
					return false;
			}
		}
		return true;
	}

	public ArrayList <Key> getKeys() {
		return keys;
	}

	public void setKeys(ArrayList <Key> keys) {
		this.keys = keys;
	}

	public void setLocks(ArrayList <Lock> locks) {
		this.locks = locks;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		//result = prime * result + ((keys == null) ? 0 : keys.hashCode());
		//result = prime * result + ((locks == null) ? 0 : locks.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object other){
		if(other instanceof Vertex) {
			Vertex otherV = (Vertex) other;
			if(this.id==otherV.id){
				for(Lock l: locks)
					if(!otherV.locks.contains(l))
						return false;
				for(Key k: keys)
					if(!otherV.keys.contains(k))
						return false;
				return true;
			}
		return false;	
		}
		
		return false;
	}

	public void addKeyProbability(int keyId, double probability) {
		keysProbs.put(keyId, probability);
	}
	public double getKeyProbability(int keyId)
	{
		return (keysProbs.containsKey(keyId) ? keysProbs.get(keyId) : 0);
	}

}
