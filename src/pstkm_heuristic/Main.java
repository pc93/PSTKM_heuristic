/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pstkm_heuristic;

import pl.edu.pojo.HeurisitcInput;

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
        int numberOfPaths = 2;
		                
        HeurisitcInput input = null;
        
        input = Parser.parse("resources/trivial.txt", "resources/demands_trivial.txt", numberOfPaths);
        
        System.out.println(input.toString());
        //model.createModel(input, numberOfPaths);
    }
    
}
