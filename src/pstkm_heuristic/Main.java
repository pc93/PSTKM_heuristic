/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pstkm_heuristic;

import pl.edu.pojo.HeuristicInput;
import pl.edu.pojo.Config;

/**
 *
 * @author Czarnocki
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Model model = new Model();
		                
        HeuristicInput input = null;
        
        input = Parser.parse("resources/non-trivial_1.txt", "resources/demands_nontrivial1.txt", Config.PATHS);
        //input = Parser.parse("resources/trivial.txt", "resources/demands_trivial.txt", numberOfPaths);
        
        System.out.println(input.toString());
        
        System.out.println("Network modules: " + input.getNetworkModules());
        
        SimulatedAnnealing sa = new SimulatedAnnealing(input, 1000, 0.1, 0.97);
        sa.findSolution();
        //model.createModel(input, numberOfPaths);
    }
    
}
