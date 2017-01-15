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
                /*
                //get config for demand d
                PathsConfiguration config = null;
                for (PathsConfiguration x : pathsConfigs)
                    if (x.getDemand().getId() == d.getId())
                        config = x;
                
                //get random index of config.demandPathsCombinations list
                int range = config.getDemandPathsCombinations().size() - 1; //10-1 range:[0-9]
                int idx = rand.nextInt(range); //10-1 [range: 0-9]
                System.out.println("range: " + range + "  idx: " + idx);
                List<Integer> combination = config.getDemandPathsCombinations().get(idx);
                System.out.println("Choosen combination: " + combination.toString());
                
                //check solution for random combination
                int realizedDemandPart = 0;
                if (combination.size() == 1)
                {
                    PathWithEgdes path = getPathWithID(mapCopy.get(d), combination.get(0)); //combination.get(): correct range [0;3] 
                    System.out.println("Demand: " + d.getValue() + ", path id: " + path.getIndex());
                    System.out.println("p: " + path.toString());
                    for (Edge e : path.getEdges()) //solution without disaggregation
                        e.setLoad(e.getCurrentLoad() + d.getValue());
                    path.setMinFreeLoad(); //refresh
                }
                if (combination.size() == 2)
                {
                    PathWithEgdes path1 = getPathWithID(mapCopy.get(d), combination.get(0));
                    PathWithEgdes path2 = getPathWithID(mapCopy.get(d), combination.get(1));
                    
                    System.out.println("DEZAGREGUJ NA 2");
                    System.out.println("Demand: " + d.getValue());
                    System.out.println("p1: " + path1.toString());
                    System.out.println("p2: " + path2.toString());
                }
                */
                int demandToRealize = d.getValue();
                while (demandToRealize > 0)
                {
                    int idx = rand.nextInt(Config.PATHS-1)+1; //range[0,3] -> [1,4]
                    PathWithEgdes path = getPathWithID(mapCopy.get(d), idx);

                    int free = path.getMinFreeLoad();
                    System.out.println("path: " + path.getIndex() + ", free: " + free);

                    if (free >= demandToRealize)
                    {
                        for (Edge e : path.getEdges())
                            e.setLoad(e.getCurrentLoad() + demandToRealize);
                        demandToRealize = 0;
                    }
                    
                    if (free < demandToRealize)
                    {
                        if (free == 0 && demandToRealize >= Config.MODULARITY)
                        {
                            for (Edge e : path.getEdges())
                                    e.setLoad(e.getCurrentLoad() + Config.MODULARITY);
                            demandToRealize -= Config.MODULARITY;
                        }
                        else if (free == 0 && demandToRealize < Config.MODULARITY)
                        {
                            for (Edge e : path.getEdges())
                                e.setLoad(e.getCurrentLoad() + demandToRealize);
                            demandToRealize = 0;
                        }
                        else if (free != 0 && demandToRealize >= Config.MODULARITY)
                        {
                            //...
                        }
                        else if (free != 0 && demandToRealize < Config.MODULARITY)
                        {
                            //...
                        }
                        

                    }
                    path.setMinFreeLoad(); //refresh
                    System.out.println("Demand: " + d.getValue() + ", path id: " + path.getIndex());
                }
                
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
    
    public PathWithEgdes getPathWithID(List<PathWithEgdes> paths, int id)
    {
        for (PathWithEgdes path : paths)
        {
            if (path.getIndex() == id)
                return path;
        }
        return null;
    }
    
    
}
