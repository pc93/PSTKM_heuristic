/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pstkm_heuristic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Map;
import java.util.stream.IntStream;

import pl.edu.pojo.*;

public class SimulatedAnnealing {

	private HeuristicInput input;
	private Map<Demand, List<PathWithEgdes>> map;

	private double temperature;
	private double stopTemp;
	private double coolingRate;

	private Random rand = new Random();

	public SimulatedAnnealing(HeuristicInput input, double startTemp,
			double stopTemp, double coolingRate) {
		this.input = input;
		this.map = input.getDemandPathsMap();
		this.temperature = startTemp;
		this.stopTemp = stopTemp;
		this.coolingRate = coolingRate;
	}

	public void findSolution(String graphPath, String demandsPath) {
		
		System.out.println("Init modules: " + input.getNetworkModules());
		int bestSolution = 1000;
		int deltaDistance = 0;
		int distance = getGreedySolution(input);

		int greedy = distance;
		// Simulated Annealing solution
		int iterations = 0;
		Result result = null;
		while (temperature > stopTemp) {
			input = Parser.parse(graphPath, demandsPath, Config.PATHS);
			result = new Result(input.getNetworkModules());
			Map<Demand, List<PathWithEgdes>> mapCopy = input
					.getDemandPathsMap();
			List<Demand> demands = new ArrayList<Demand>(mapCopy.keySet());

			for (int i = 0; i < mapCopy.size(); i++) {
				Demand d = demands.get(rand.nextInt(demands.size()));
				int demandToRealize = d.getValue();
				while (demandToRealize > 0) {
					List<PathWithEgdes> paths = mapCopy.get(d);
					int idx = rand.nextInt(paths.size());
					PathWithEgdes path = paths.get(idx);

					int realizedDemand = realizeDemand(d, demandToRealize, path);
					result.update(d, path, realizedDemand);
					demandToRealize = demandToRealize - realizedDemand;
					System.out.println("Realized demand: " + realizedDemand);
					System.out.println("Demand to realize: " + demandToRealize);
				}
				demands.remove(demands.indexOf(d));
			}
			int tempResult = input.getNetworkModules();
			System.out.println(iterations + ": Random local solution: "
					+ tempResult + "\n");

			deltaDistance = tempResult - distance;
			if ((deltaDistance < 0)
					|| (distance > 0 && Math.exp(-deltaDistance / temperature) > rand
							.nextDouble())) {
				bestSolution = tempResult;
				distance = deltaDistance + distance;
			}
			result.updateFinalModules(bestSolution);
			temperature = temperature * coolingRate;
			iterations++;

		}
		System.out.println("\nNumber of iterations: " + iterations);
		System.out.println("Best solution: " + bestSolution);
		result.printResult();
		System.out.println("Greedy: " + greedy);

	}

	private int realizeDemand(Demand d, int demandToRealize, PathWithEgdes path) {

		int free = path.getMinFreeLoad();
		System.out.println("Demand: " + d.getValue() + ", path id: "
				+ path.getIndex() + ", free: " + free + ", toRealize: "
				+ demandToRealize);
		int demandRealized = 0;

		if (free >= demandToRealize) {
			for (Edge e : path.getEdges())
				e.setLoad(e.getCurrentLoad() + demandToRealize);
			demandRealized = demandToRealize;
		}

		if (free < demandToRealize) {
			if (free == 0 && demandToRealize >= Config.MODULARITY) {
				for (Edge e : path.getEdges())
					e.setLoad(e.getCurrentLoad() + Config.MODULARITY);
				demandRealized = Config.MODULARITY;
			} else if (free == 0 && demandToRealize < Config.MODULARITY) {
				for (Edge e : path.getEdges())
					e.setLoad(e.getCurrentLoad() + demandToRealize);
				demandRealized = demandToRealize;
			} else // (free != 0)
			{
				for (Edge e : path.getEdges())
					e.setLoad(e.getCurrentLoad() + free);
				demandRealized = free;
			}
		}

		return demandRealized;
	}

	public PathWithEgdes getPathWithID(List<PathWithEgdes> paths, int id) {
		for (PathWithEgdes path : paths) {
			if (path.getIndex() == id)
				return path;
		}
		return null;
	}

	public Demand getDemandWithID(List<Demand> demands, int id) {
		for (Demand d : demands) {
			if (d.getId() == id)
				return d;
		}
		return null;
	}

	public int getGreedySolution(HeuristicInput input) {
		
		Map<Demand, List<PathWithEgdes>> mapCopy = input
				.getDemandPathsMap();
		List<Demand> demands = new ArrayList<Demand>(mapCopy.keySet());

		for (int i = 0; i < mapCopy.size(); i++) {
			Demand d = demands.get(rand.nextInt(demands.size()));
			int demandToRealize = d.getValue();
			while (demandToRealize > 0) {
				List<PathWithEgdes> paths = mapCopy.get(d);
				int idx = rand.nextInt(paths.size());
				PathWithEgdes path = paths.get(idx);

				int realizedDemand = realizeDemand(d, demandToRealize, path);
				demandToRealize = demandToRealize - realizedDemand;
				System.out.println("Realized demand: " + realizedDemand);
				System.out.println("Demand to realize: " + demandToRealize);
			}
			demands.remove(demands.indexOf(d));
		}
		
//		Map<Demand, List<PathWithEgdes>> map = input.getDemandPathsMap();
//		for (Demand d : map.keySet()) {
//			// get shortest path for demand
//			PathWithEgdes shortest = null;
//			int min = 1000;
//			for (PathWithEgdes path : map.get(d)) {
//				if (min >= path.getEdges().size()) {
//					min = path.getEdges().size();
//					shortest = path;
//				}
//			}
//			for (Edge e : shortest.getEdges())
//				e.setLoad(e.getCurrentLoad() + d.getValue());
//		}

		return input.getNetworkModules();
	}

}
