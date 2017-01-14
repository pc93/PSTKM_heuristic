/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.pojo;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Czarnocki
 */
public class HeurisitcInput {
        private Map<Demand, List<PathWithEgdes>> demandPathsMap; // map demands : list of paths that realizes this demand
	private List<Edge> edges; // all edges in graph
	
        
        
	public Map<Demand, List<PathWithEgdes>> getDemandPathsMap() {
		return demandPathsMap;
	}
	public void setDemandPathsMap(Map<Demand, List<PathWithEgdes>> demandPathsMap) {
		this.demandPathsMap = demandPathsMap;
	}
	public List<Edge> getEdges() {
		return edges;
	}
	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}
	
	public Edge getEdgeByNodePair(int src, int dst) {
		for(Edge e : edges) {
			if(e.getStartNode() == src && e.getEndNode() == dst) {
				return e;
			} else if (e.getEndNode() == src && e.getStartNode() == dst) {
				return e;
			}
		}
		return null;
	}
	@Override
	public String toString() {
		return "HeuristicIput [demandPathsMap=" + demandPathsMap + ",\n-- edges="
				+ edges + "]";
	}
}
