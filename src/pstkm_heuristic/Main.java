/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pstkm_heuristic;

import java.util.Scanner;
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

        HeuristicInput input = null;
        
        Integer scenario = printMenu();
        Config.PATHS = getNumOfPaths(scenario);
        Config.MODULARITY = setModularity();
        String graphPath = "";
        String demandsPath = "";
        
        if (scenario == 1)
        {
            graphPath = "resources/trivial.txt";
            demandsPath = "resources/demands_trivial.txt";
        }
	else if (scenario == 2)
        {
            graphPath = "resources/non-trivial_1.txt";
            demandsPath = "resources/demands_nontrivial1.txt";
        }
	else
        {
            graphPath = "resources/non-trivial_2.txt";
            demandsPath = "resources/demands_nontrivial2.txt";
        }
        
        input = Parser.parse(graphPath, demandsPath, Config.PATHS);
        System.out.println(input.toString());
        System.out.println("Network modules: " + input.getNetworkModules());
        
        SimulatedAnnealing sa = new SimulatedAnnealing(input, 1000, 0.1, 0.97);
        
        sa.findSolution(graphPath, demandsPath);
        
    }
    public static Integer printMenu()
        {
            String choice = "";
            do
            {
                System.out.println("*****************************************************************************");
                System.out.println("*** Type number to run specific scenario: ***");
                System.out.println("1. Trivial scenario");
                System.out.println("2. Non-trivial 1 scenario");
                System.out.println("3. Non-trivial 2 scenario");
                System.out.println("Type number of scenario: ");
                Scanner scan = new Scanner(System.in);
                choice = scan.nextLine();
            }
            while(!choice.equals("1") && !choice.equals("2") && !choice.equals("3"));

            return Integer.parseInt(choice);
        }
        
        public static Integer getNumOfPaths(int scenario)
        {
            String choice = "";
            System.out.println("*** Set number of paths for demands ***");
            if (scenario == 1)
                System.out.println("Trivial scenario - max: 2");
            else if (scenario == 2)
                System.out.println("Non-trivial 1 scenario - max: 4");
            else
                System.out.println("Non-trivial 2 scenario - max: 7");
            System.out.println("Type number of paths: ");
            Scanner scan = new Scanner(System.in);
            choice = scan.nextLine();
            
            return Integer.parseInt(choice); 
        }
        
        public static Integer setModularity()
        {
            String choice = "";
            System.out.println("*** Set modularity for network ***");
            System.out.println("Type modularity (number): ");
            Scanner scan = new Scanner(System.in);
            choice = scan.nextLine();
            
            return Integer.parseInt(choice);
        }
    
}
