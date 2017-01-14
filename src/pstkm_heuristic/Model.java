/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pstkm_heuristic;

import java.util.List;
import java.util.Map;
import pl.edu.pojo.Demand;
import pl.edu.pojo.Edge;
import pl.edu.pojo.PathWithEgdes;
import pl.edu.pojo.HeuristicInput;

/**
 *
 * @author Czarnocki
 */
public class Model {
        // params
	private int modularity;
	private int[] demands;
	private int[] initalLoad;
	private int[][][] delta_edp;
        
        public void createModel(HeuristicInput heuristicInput, int numberOfPaths) {

		try {
			Map<Demand, List<PathWithEgdes>> demandPathsMap = heuristicInput
					.getDemandPathsMap();

			int d_length = demandPathsMap.keySet().size();
			demands = new int[d_length];

			for (Demand d : demandPathsMap.keySet()) {
				demands[d.getId() - 1] = d.getValue();
			}

			int p_length = 0;
			for (Demand d : demandPathsMap.keySet()) {
				p_length = demandPathsMap.get(d).size();
				break;
			}
			List<Edge> E = heuristicInput.getEdges();
			int initial_modules = getInitialModules(E);
			// definicja deldta_edp
			delta_edp = new int[E.size()][d_length][p_length];
                        
                } catch (Exception ex) {
			System.out.println("Exception: " + ex);
		}
        }
        public int getInitialModules(List<Edge> edges) {
		int count = 0;
		for(Edge e : edges) {
			double dbl = (double) ((double) e.getCurrentLoad() / (double) modularity);
			count += (int) Math.ceil(dbl);
		}
		return count;
	}
        
        private boolean checkInPath(Edge e, PathWithEgdes p) {
		if (p.getEdges().contains(e))
			return true;
		return false;
	}
                     

}
