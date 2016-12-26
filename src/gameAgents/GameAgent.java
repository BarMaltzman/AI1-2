package gameAgents;
import normalAgents.Agent;
import graphs.Graph;
import graphs.Vertex;



public abstract class GameAgent extends Agent {
	
	protected GameAgent otherAgent;
	protected Vertex nextMove;
	public GameAgent(int id,GameAgent otherAgent) {
		super(id);
		this.otherAgent = otherAgent;
	}

	@Override
	public abstract String getName();
	public abstract boolean isTerminal(Graph graph);
	public Agent getOtherAgent() {
		return otherAgent;
	}

	public void setOtherAgent(GameAgent otherAgent) {
		this.otherAgent = otherAgent;
	}
	
	

}
