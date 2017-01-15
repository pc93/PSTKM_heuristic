package pstkm_heuristic;

import java.util.HashMap;
import java.util.Map;

import pl.edu.pojo.Demand;
import pl.edu.pojo.PathWithEgdes;

public class Result {
	
	private int initModules;
	private int finalModules;
	private Map<Demand, Map<PathWithEgdes, Integer>> result;
	
	public Result(int initModules) {
		this.initModules = initModules;
		result = new HashMap<Demand, Map<PathWithEgdes,Integer>>();
	}
	
	public void update(Demand d, PathWithEgdes path, Integer demandRealizedOnPath) {
		Map<PathWithEgdes, Integer> demandsRealizedByPaths = new HashMap<PathWithEgdes, Integer>();
		if(result.containsKey(d)) {
			Integer newDemand = 0;
			demandsRealizedByPaths = result.get(d);
			if(demandsRealizedByPaths.containsKey(path)) {
				newDemand = demandsRealizedByPaths.get(path) + demandRealizedOnPath;
				demandsRealizedByPaths.put(path, newDemand);
			} else {
				demandsRealizedByPaths.put(path, demandRealizedOnPath);
			}
		} else {
			demandsRealizedByPaths = new HashMap<PathWithEgdes, Integer>();
			demandsRealizedByPaths.put(path, demandRealizedOnPath);
		}
		
		result.put(d, demandsRealizedByPaths);
	}
	
	public void updateFinalModules(int finalModules) {
		this.finalModules = finalModules;
	}
	
	public int getFinalModules() {
		return finalModules;
	}
	
	public void printResult() {
		System.out.println("\n******* Final results ******* ");
		System.out.println("Initial modules: " + initModules);
		System.out.println("Final modules:  " + finalModules);
		int ss = 2*(finalModules - initModules);
		System.out.println("Site-Surveys needed: " + ss);
		System.out.println(result.toString());
	}

}
