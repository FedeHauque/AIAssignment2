/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uniquePackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.IntStream;
import javafx.util.Pair;

/**
 *
 * @author dertas
 */
public class MainClass {
    
    private static int[][] city_positions = {{20, 20}, {20, 40}, {20, 160}, {40, 120}, {60, 20}, {60, 80}, {60, 200}, {80, 180}, {100, 40}, {100, 120}, {100, 160}, {120, 80}, {140, 140}, {140, 180}, {160, 20}, {180, 60}, {180, 100}, {180, 200}, {200, 40}, {200, 160}};
    private static String[] generic_names = {"A", "B", "C", "D", "E", "F", "G", "H", "I" , "J", "K", "L", "M", "N", "O", "P", "Q", "R" , "S" , "T"};
    private static int[][] uk_distances = {{0, 172, 145, 607, 329, 72, 312, 120}, {172, 0, 192, 494, 209, 158, 216, 92}, {145, 192, 0, 490, 237, 75, 205, 100}, {607, 494, 490, 0, 286, 545, 296, 489}, {329, 209, 237, 286, 0, 421, 49, 208}, {72, 158, 75, 545, 421, 0, 249, 75}, {312, 216, 205, 296, 49, 249, 0, 194}, {120, 92, 100, 489, 208, 75, 194, 0}};
    private static String[] city_names = {"Brighton", "Bristol", "Cambridge", "Glasgow", "Liverpool", "London", "Manchester", "Oxford"};
    private static ArrayList<ArrayList<Double>> dist = new ArrayList(); 
    private static ArrayList<String> names = new ArrayList<String>();
    
    private static void load_data(boolean b) {
        if(b){
            for(int i=0; i<uk_distances.length; i++){
                ArrayList<Double> arr = new ArrayList<Double>();
                for(int j=0; j<uk_distances.length; j++){
                    arr.add(new Double(uk_distances[i][j]));
                }
                dist.add(arr);
            }
            for(int i=0; i<city_names.length; i++){
                names.add(city_names[i]);
            }
        }else{
            for(int i=0; i<city_positions.length; i++){
                ArrayList<Double> arr = new ArrayList<Double>();
                for(int j=0; j<city_positions.length; j++){
                    arr.add(new Double(calculate_diustance(city_positions[i], city_positions[j])));
                }
                dist.add(arr);
            }
            for(int i=0; i<generic_names.length; i++){
                names.add(generic_names[i]);
            }
        }
    }
    
    private static double calculate_diustance(int[] city_position1, int[] city_position2) {
        double x1 = city_position1[0];
        double y1 = city_position1[1];
        double x2 = city_position2[0];
        double y2 = city_position2[1];
        return Math.sqrt(((x1-x2)*(x1-x2))+((y1-y2)*(y1-y2)));
    }
    
    public static void main(String[] args) {
        boolean solve_uk_cities = false;
        load_data(solve_uk_cities);
        System.out.println(dist.size() + " " + dist);
        System.out.println(names.size() + " " +names);
        int k = 1000;
        ArrayList<ArrayList<Integer>> population = initialization(k);
        ArrayList<ArrayList<Integer>> individuals = population;
        double best_performance = 0;
        ArrayList<Integer> best_path = new ArrayList<Integer>();
        int better_not_changed = 0;
        while(better_not_changed < 20){
            individuals = get_individuals(population, k);
            individuals = order(individuals);
            print_path(individuals);
            if(fitness(individuals.get(0)) > best_performance){
                best_performance = fitness(individuals.get(0)); //reassigning best  fitness
                better_not_changed = 0;
                best_path = individuals.get(0);
            }else{
                better_not_changed++;
            }
            //print_path(individuals);
            ArrayList<ArrayList<Integer>> crossed_over = new ArrayList<>();
            for(int i = 1; i<k; i= i+2){
                Pair<ArrayList<Integer>,ArrayList<Integer>> a = cross_over(individuals.get(i-1), individuals.get(i));
                crossed_over.add(a.getKey());
                crossed_over.add(a.getValue());
            }
            
            ArrayList<ArrayList<Integer>> mutated = new ArrayList<>();
            for(int i = 0; i<k; i++){
                ArrayList<Integer> a = mutate(crossed_over.get(i));
                mutated.add(a);
            }
            population.addAll(mutated);  
            eliminate_equals(population);
            
        }
        System.out.print("Best path: ");
        for(int j=0; j<names.size(); j++){
                System.out.print(names.get(best_path.get(j)) + " ");
        }
        System.out.println();
        if(!solve_uk_cities){
            DrawFrame df = new DrawFrame(city_positions, best_path);
            df.setVisible(true);
        }
    }
    
    private static double fitness(ArrayList<Integer> path) {
        double sum = 0;
        for(int i=1; i<names.size(); i++){
            sum += dist.get(path.get(i-1)).get(path.get(i));
        }
        return (5600 - sum);
    }

    private static ArrayList<ArrayList<Integer>> initialization(int k) {
       ArrayList<ArrayList<Integer>> ret = new ArrayList<>();
       Boolean[] alr = new Boolean[names.size()];
       for(int i=0; i<k; i++){
           ArrayList<Integer> ass = new ArrayList<Integer>();
           Arrays.fill(alr, Boolean.FALSE);
           for(int j=0; j<names.size(); j++){
               int ran = (int)(Math.random()*names.size());
               while(alr[ran]){
                   ran = (int)(Math.random()*names.size());
                }
                ass.add(ran);
                alr[ran] = true;
           }
           ret.add(ass);
       }
       return ret;
    }

    private static Pair<ArrayList<Integer>,ArrayList<Integer>> cross_over(ArrayList<Integer> par1, ArrayList<Integer> par2) {
        int[] encoded_offspring1 = new int[names.size()];
        int[] encoded_offspring2 = new int[names.size()];
        ArrayList<Integer> encoded_par1 = encode(par1);
        ArrayList<Integer> encoded_par2 = encode(par2);
        //int ran = (int)(Math.random()*names.size()); //doing selection and crossover by selecting a random value
        int ran = 6; //doing the selection and crossover by fixing a integer value
        for(int i = 0; i<names.size(); i++){
            if (i<ran){
                encoded_offspring1[i] = encoded_par1.get(i);
                encoded_offspring2[i] = encoded_par2.get(i);
            }else{
                encoded_offspring1[i] = encoded_par2.get(i);
                encoded_offspring2[i] = encoded_par1.get(i);
            }
        }
        ArrayList<Integer> offspring1 = decode(encoded_offspring1);
        ArrayList<Integer> offspring2 = decode(encoded_offspring2);
        Pair a = new Pair(offspring1, offspring2);
        return a;
    }

    private static ArrayList<ArrayList<Integer>> order(ArrayList<ArrayList<Integer>> individuals) {
        Collections.sort(individuals, new Comparator<ArrayList<Integer>>() {
			public int compare(ArrayList<Integer> o1, ArrayList<Integer> o2) {
				return (int) (fitness(o2) - fitness(o1));
			}
		});
        return individuals;
    }

    private static void print_path(ArrayList<ArrayList<Integer>> paths) {
        for(ArrayList<Integer> i : paths){
            for(int j=0; j<names.size(); j++){
                System.out.print(names.get(i.get(j)) + " ");
            }
            System.out.println(" Fitness: " + fitness(i));
        }
        System.out.println("\n");
    }

    private static ArrayList<Integer> encode(ArrayList<Integer> par1) {
        ArrayList<Integer> positions = consecutives(names.size());
        int[] encoded = new int[names.size()];
        for(int i=0; i<par1.size(); i++){
            encoded[i] = positions.indexOf(par1.get(i));
            positions.remove(par1.get(i));
        }
        ArrayList<Integer> encodedArray = new ArrayList<Integer>();
        for(int i=0; i<par1.size(); i++){
            encodedArray.add(encoded[i]);
        }
    return encodedArray;
    }

    private static ArrayList<Integer> decode(int[] encoded_offspring1) {
        ArrayList<Integer> positions = consecutives(names.size());
        int[] decoded = new int[names.size()];
        for(int i=0; i<encoded_offspring1.length; i++){
            decoded[i] = positions.get(encoded_offspring1[i]);
            positions.remove(positions.get(encoded_offspring1[i]));
        }
        ArrayList<Integer> decodedArray = new ArrayList<Integer>();
        for(int i=0; i<encoded_offspring1.length; i++){
            decodedArray.add(decoded[i]);
        }
        return decodedArray;
    }

    private static ArrayList<ArrayList<Integer>> get_individuals(ArrayList<ArrayList<Integer>> population, int k) {
        population = order(population);
        ArrayList<ArrayList<Integer>> individuals = new ArrayList<>();
        for(int i =0; i<k; i++){
            individuals.add(population.get(i));
        }
        return individuals;
    }

    private static ArrayList<Integer> consecutives(int size) {
        ArrayList<Integer> arr = new ArrayList<Integer>();
        for(int i=0; i<size; i++){
            arr.add(new Integer(i));
        }
        return arr;
    }

    private static ArrayList<Integer> mutate(ArrayList<Integer> ind) {
        //int ran = (int)(Math.random()*names.size());
        //int ran2 = (int)(Math.random()*names.size());
        int ran = 3;
        int ran2= 7;
        while(ran==ran2){
            ran2 = (int)(Math.random()*names.size());
        }
        int aux = ind.get(ran2);
        ind.set(ran2, ind.get(ran));
        ind.set(ran, aux);
        return ind;
    }

    private static void eliminate_equals(ArrayList<ArrayList<Integer>> population) {
        ArrayList<Integer> delete = new ArrayList<Integer>();
        for(int i=0; i<population.size(); i++){
            for(int j=i+1; j<population.get(i).size(); j++){
                if(equal(population.get(i), population.get(j))){
                    delete.add(i);
                }
            }
        }
        for(int i=0; i<delete.size(); i++){
            int a = delete.get(i).intValue();
            population.remove(a);
        }
    }

    private static boolean equal(ArrayList<Integer> get, ArrayList<Integer> get0) {
        boolean equal =true;
        for(int i=0; i<get.size(); i++){
            if(get.get(i) != get0.get(i)){
                equal = false;
            }
        }
        return equal;
    }
    
}
