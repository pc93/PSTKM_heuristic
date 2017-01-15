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
    private List<PathsConfiguration> pathsConfigs;
    
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
        this.pathsConfigs = new ArrayList<PathsConfiguration>();
        
        getPathConfigurations(2);
    }
    
    public void findSolution()
    {
        System.out.println("\nInit state: " + input.getNetworkModules());
        
        HeuristicInput inputCopy = Parser.parse("resources/non-trivial_1.txt", "resources/demands_nontrivial1.txt", Config.PATHS);
        Map<Demand, List<PathWithEgdes>> mapCopy = inputCopy.getDemandPathsMap();
        //Greedy solution, start point
        int greedySolution = getGreedySolution(inputCopy); //distance
        System.out.println("Greedy Solution: " + greedySolution + "\n");
        System.out.println("Init modules: " + input.getNetworkModules());
        
        double deltaDistance = 0;
        

        //Simulated Annealing solution
        int iterations = 0;
        int bestSolution = 10000000;
        while (temperature > stopTemp)
        {
            inputCopy = Parser.parse("resources/non-trivial_1.txt", "resources/demands_nontrivial1.txt", Config.PATHS); //code it to not do it like that but like below
            //inputCopy = new HeuristicInput(input.getEdges(), input.getDemandPathsMap()); //inputCopy should be a network before demands were realized, but each iteration increase it
            System.out.println(inputCopy.getNetworkModules()); 
            mapCopy = inputCopy.getDemandPathsMap();
            for (Demand d : mapCopy.keySet())
            {
                int idx = rand.nextInt(Config.PATHS);
                PathWithEgdes path = mapCopy.get(d).get(idx);
                
                
                
                System.out.println("Demand: " + d.getValue() + ", path id: " + path.getIndex());
                
                for (Edge e : path.getEdges()) //solution without disaggregation
                    e.setLoad(e.getCurrentLoad() + d.getValue());
                
            }
            temperature = temperature * coolingRate;
            iterations += 1;
            
            System.out.println(iterations + ": Random local solution: " + inputCopy.getNetworkModules() + "\n");
            if (inputCopy.getNetworkModules() < bestSolution)
                bestSolution = inputCopy.getNetworkModules();
        }
        System.out.println("\niterations: " + iterations);
        System.out.println("Best solution: " + bestSolution);
        
    }
    
    
    public int  getGreedySolution(HeuristicInput input)
    {
        Map<Demand, List<PathWithEgdes>> map = input.getDemandPathsMap();
        for (Demand d : map.keySet())
        {
            //get shortest path for demand
            PathWithEgdes shortest = null;
            int min = 1000;
            for (PathWithEgdes path : map.get(d))
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
        
        return input.getNetworkModules();
    }
    
    public void getPathConfigurations(int maxDisaggregation)
    {
        for (Demand d : map.keySet())
        {
            pathsConfigs.add(new PathsConfiguration(map, d, maxDisaggregation));
        }
        //test
        for (PathsConfiguration p : pathsConfigs)
        {
            System.out.println("Demand " + p.getDemand().getId() + ": Combinations of paths' indexes:");
            List<List<Integer>> listOfList = p.getDemandPathsCombinations();
            for (List<Integer> list : listOfList)
                System.out.print(list.toString() +  ", ");
            System.out.println();
        }
    } 
    
    
}
