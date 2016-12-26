package graphs;

public class Edge {
	private Vertex v1,v2;
	private double weight;
	public Edge(Vertex v1, Vertex v2, double weight) {
		this.v1 = v1;
		this.v2 = v2;
		this.weight = weight;
	}
	@Override
	public String toString() {
		return "E# "+ v1.getId()+" "+v2.getId()+" "+weight   ;
	}
	public Vertex getV1() {
		return v1; 
	}
	public void setV1(Vertex v1) {
		this.v1 = v1;
	}
	public Vertex getV2() {
		return v2;
	}
	public void setV2(Vertex v2) {
		this.v2 = v2;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
}
