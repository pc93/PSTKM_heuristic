package pl.edu.pojo;

import java.util.List;

public class PathWithEgdes {

	private int index;
	private List<Edge> edges;
        private int minFreeLoad; //m = 10, path = (e1,e2), e1.load = 3, e2.load = 5 => maxFreeLoad = m - max(eX.load) = 10 - 5

	public PathWithEgdes(int index, List<Edge> edges) {
		super();
		this.index = index;
		this.edges = edges;
                this.minFreeLoad = 10;
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
        public int setMinFreeLoad()
        {
            int minFree = Config.MODULARITY;
            for (Edge e : this.edges)
            {
                int x = (int)(Math.ceil(e.getCurrentLoad() % Config.MODULARITY)) * Config.MODULARITY - e.getCurrentLoad();
                if (x < minFree)
                    minFree = x;
            }
            return minFree;
        }

	@Override
	public String toString() {
		return "\n-- PathWithEgdes [index=" + index + ", freeLoad=" + minFreeLoad + ", edges=" + edges + "]";
	}
	
	

}
