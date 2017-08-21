package graphs;

public class ProbabilityVertex extends Vertex {
	private double probability;
	public ProbabilityVertex(int id, double probability){
		super(id);
		this.probability=probability;
	}
}
