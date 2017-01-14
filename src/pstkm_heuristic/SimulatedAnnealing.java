/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pstkm_heuristic;

import java.util.List;
import java.util.Random;
import java.util.Map;
import pl.edu.pojo.*;

public class SimulatedAnnealing {
    
    private HeuristicInput input;
    private Map<Demand, List<PathWithEgdes>> map;
    private double temperature;
    private double stopTemp;
    private double coolingRate;
    
    private Random rand = new Random();
    
    public SimulatedAnnealing(HeuristicInput input, double startTemp, double stopTemp, double coolingRate)
    {
        this.input = input;
        this.map = input.getDemandPathsMap();
        this.temperature = startTemp;
        this.stopTemp = stopTemp;
        this.coolingRate = coolingRate;
        
    }
    
    public void findSolution()
    {
        System.out.println("\nInit state: " + input.getNetworkModules());
        
        HeuristicInput inputCopy = new HeuristicInput(input.getEdges(), input.getDemandPathsMap());
        Map<Demand, List<PathWithEgdes>> mapCopy = inputCopy.getDemandPathsMap();
        //Greedy solution, start point
        for (Demand d : mapCopy.keySet())
        {
            //get shortest path for demand
            PathWithEgdes shortest = null;
            int min = 1000;
            for (PathWithEgdes path : mapCopy.get(d))
            {
                if (min >= path.getEdges().size())
                {
                    min = path.getEdges().size();
                    shortest = path;
                }
            }
            for (Edge e : shortest.getEdges())
                e.setLoad(e.getCurrentLoad() + d.getValue());
        }
        System.out.println("Greedy Solution: " + inputCopy.getNetworkModules());
        //Simulated Annealing solution
        int iterations = 0;
        int bestSolution = 10000000;
        while (temperature > stopTemp)
        {
            inputCopy = new HeuristicInput(input.getEdges(), input.getDemandPathsMap());
            mapCopy = inputCopy.getDemandPathsMap();
            for (Demand d : mapCopy.keySet())
            {
                int idx = rand.nextInt(Config.PATHS-1);
                PathWithEgdes path = mapCopy.get(d).get(idx);
                for (Edge e : path.getEdges()) //solution without disaggregation
                {
                    e.setLoad(e.getCurrentLoad() + d.getValue());
                }
            }
            temperature = temperature * coolingRate;
            iterations += 1;
            System.out.println(iterations + ": Random local solution: " + inputCopy.getNetworkModules());
            if (inputCopy.getNetworkModules() < bestSolution)
                bestSolution = inputCopy.getNetworkModules();
        }
        System.out.println("\niterations: " + iterations);
        System.out.println("Best solution: " + bestSolution);
        
        
    }
    
    
            
    
    
}
