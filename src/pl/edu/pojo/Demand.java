package pl.edu.pojo;

public class Demand {
	
	private int srcNode;
	private int dstNode;
	private int value;
	private int id;

	public Demand(int id, int srcNode, int dstNode, int value) {
		this.id = id;
		this.srcNode = srcNode;
		this.dstNode = dstNode;
		this.value = value;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSrcNode() {
		return srcNode;
	}

	public void setSrcNode(int srcNode) {
		this.srcNode = srcNode;
	}

	public int getDstNode() {
		return dstNode;
	}

	public void setDstNode(int dstNode) {
		this.dstNode = dstNode;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "\nDemand [srcNode=" + srcNode + ", dstNode=" + dstNode
				+ ", value=" + value + "]";
	}
	
	
	

}
