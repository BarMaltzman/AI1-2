package graphs;
import normalAgents.Agent;

/**
 * 
 * @author Or and Bar
 * This interface represents a Heuristic that evaluates a state (graph) according to the
 * acting agent's current vertex location and goal vertex.
 */
public interface Heuristic {
	/**
	 * 
	 * @param graph - the current state.
	 * @param agent	- the agent needing the evaluate.
	 * @return	the evaluation according to the current state. 
	 */
	double evaluate(Graph graph,Agent agent);
}
