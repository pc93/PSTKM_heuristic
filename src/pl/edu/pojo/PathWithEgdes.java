package pl.edu.pojo;

import java.util.List;

public class PathWithEgdes {

	private int index;
	private List<Edge> edges;

	public PathWithEgdes(int index, List<Edge> edges) {
		super();
		this.index = index;
		this.edges = edges;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}

	@Override
	public String toString() {
		return "\n-- PathWithEgdes [index=" + index + ", edges=" + edges + "]";
	}
	
	

}
