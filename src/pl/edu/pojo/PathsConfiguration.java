/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Czarnocki
 */
public class PathsConfiguration {
    private List<List<Integer>> demandPathsCombinations;
    private Demand demand;
    private List<PathWithEgdes> demandPaths;
    private Map<Demand, List<PathWithEgdes>> demandPathsMap;
    
    
    public PathsConfiguration(Map<Demand, List<PathWithEgdes>> demandPathsMap,
            Demand demand, int maxDisaggregation)
    {
        this.demand = demand;
        this.demandPathsMap = demandPathsMap;
        this.demandPaths = demandPathsMap.get(demand);
        demandPathsCombinations = new ArrayList<List<Integer>>();
        generateDisaggregationPaths(maxDisaggregation);
    }
    
    public void generateDisaggregationPaths(int maxDisaggregation)
    {
        int demandPathSize = demandPaths.size();
        int[] pathsIndexes = new int[demandPathSize];
        for (int i = 0; i < demandPathSize; i++)
            pathsIndexes[i] = demandPaths.get(i).getIndex(); //get Path -> get its index
        int startIndex = 0;
        for (int d = 1; d <= maxDisaggregation; d++) //na ile sciezek mozemy dezagregowac zapotrzebowanie
        {
            int newton = (int)Newton(pathsIndexes.length, d);
            System.out.println("\n---------------------------------------\nLiczba list: " + newton + ", pojemnosc list: " + d);
            for (int p = 0; p < newton; p++)
            {
                List<Integer> ints = permGen(new ArrayList<Integer>(), pathsIndexes, d, startIndex);
                while (ints.size() < d)
                {
                    startIndex += 1;
                    ints = permGen(new ArrayList<Integer>(), pathsIndexes, d, startIndex);
                }
                demandPathsCombinations.add(ints);
                System.out.print("\ncombi size: " + demandPathsCombinations.size());
                System.out.print(" recently element added: " + ints.toString() + "\n\n");
                
            }
            startIndex = 0;
        }
        
        //for(List<Integer> generated : demandPathsCombinations)
        //    System.out.println(generated.toString());
    }
    
    
    public List<Integer> permGen(List<Integer> curr, int[] intSet, int len, int startIndex)
    {
        for (int i = startIndex; i < intSet.length; i++)
        {
            //System.out.print("\n-- curr: " + curr.toString());
            List<Integer> temp = new ArrayList<Integer>(curr);
            if (temp.contains(intSet[i])) // zabezpieczenie przed (1,1) (2, 2)
                continue;
            temp.add(intSet[i]);
            
            System.out.print("         curr: " + curr.toString());
            System.out.print("         temp: " + temp.toString());
            
            if (temp.size() < len)
            {
                System.out.println(" -> generate next int (" + temp.size() + " < " + len + ")");
                curr = permGen(temp, intSet, len, startIndex);
                break;
            }
            else
            {
                if (!demandPathsCombinations.contains(temp))
                {
                    System.out.println("   -> return: " + temp.toString());
                    return temp;
                }
                else
                {
                    System.out.println("   -> continue: " + temp.toString());
                    continue;
                }
                    
            }               
            
        }
        
        return curr;
    }
    
    public List<List<Integer>> getDemandPathsCombinations()
    {
        return demandPathsCombinations;
    }
    public Demand getDemand()
    {
        return demand;
    }
    
    public static long Newton(int n, int k)
    {
        long  score = 1;      
        int i;
        for(i = 1; i <= k; i++) 
            score = score * ( n - i + 1 ) / i;      
        return score;   
    }
}
