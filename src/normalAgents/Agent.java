package normalAgents;
import graphs.Graph;
import graphs.Vertex;

/**
 * This abstract class represents an agent acting on the graph.
 * @author Or and Bar
 *
 */

public abstract class Agent {
	private double score;
	private int id;
	
	public Agent(int id){
		this.setId(id);
		score = 0;
	}
	/**
	 * 
	 * @return the score of the current agent.
	 */
	public double getScore(){
		return score;
	}
	/**
	 * add score to the current score of the agent.
	 * @param score to add.
	 */
	public void addScore(double score){
		this.score += score;
	}
	/**
	 * 
	 * @param graph the current state of the world
	 * @return if the agent is finished.
	 */
	public boolean finished(Graph graph){
		return graph.getAgentCurrentVertex(this).getId() == graph.getAgentGoalVertex(this).getId();
	}
	/**
	 * 
	 * @return the name of the agent.
	 */
	public abstract String getName();
	/**
	 * a method that returns an action(destination vertex) on a given state of the world.
	 * @param graph state of the world.
	 * @return	the action to be performed.
	 */
	public abstract Vertex observeWorld(Graph graph);
	@Override
	public String toString(){
		return this.getName()+getId();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
