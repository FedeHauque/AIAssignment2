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
import javafx.util.Pair;

/**
 *
 * @author dertas
 */
public class MainClass {
    
    private static int[][] dist = {{0, 172, 145, 607, 329, 72, 312, 120}, {172, 0, 192, 494, 209, 158, 216, 92}, {145, 192, 0, 490, 237, 75, 205, 100}, {607, 494, 490, 0, 286, 545, 296, 489}, {329, 209, 237, 286, 0, 421, 49, 208}, {72, 158, 75, 545, 421, 0, 249, 75}, {312, 216, 205, 296, 49, 249, 0, 194}, {120, 92, 100, 489, 208, 75, 194, 0}};
    private static String[] names = {"Brighton", "Bristol", "Cambridge", "Glasgow", "Liverpool", "London", "Manchester", "Oxford"};
    
    public static void main(String[] args) {
        int k = 10;
        ArrayList<int[]> population = initialization(k);
        //NO OLVIDAR HACER SELECCION DE INDIVIDUALS ANTES DE HACER LA SEGUNDA ITERACIÓN
        ArrayList<int[]> individuals = population;
        int best_performance = 0;
        int[] best_path = new int[8];
        int better_not_changed = 0;
        while(better_not_changed < 10){
            individuals = get_individuals(population, k);
            individuals = order(individuals);
            if(fitness(individuals.get(0)) > best_performance){
                best_performance = fitness(individuals.get(0)); //reassigning best  fitness
                better_not_changed = 0;
                best_path = individuals.get(0);
            }else{
                better_not_changed++;
            }
            //print_path(individuals);
            ArrayList<int[]> crossed_over = new ArrayList<int[]>();
            for(int i = 1; i<k; i= i+2){
                Pair<int[],int[]> a = cross_over(individuals.get(i-1), individuals.get(i));
                crossed_over.add(a.getKey());
                crossed_over.add(a.getValue());
            }
            
            //MUTATION REMAININGmnb
            int mutate_over_1;
            
            print_path(crossed_over);
            System.out.println("\n");
            for(int i = 0; i<k; i++){
                int ran_swap_1 = (int)(Math.random()*8);
                int ran_swap_2 = (int)(Math.random()*8);
                mutate_over_1= crossed_over.get(i)[ran_swap_1];
                crossed_over.get(i)[ran_swap_1]= crossed_over.get(i)[ran_swap_2];
                crossed_over.get(i)[ran_swap_2]= mutate_over_1;
                
                
            }
            print_path(crossed_over);
            population.addAll(crossed_over);
            
        }
        System.out.println("Best path found: " + names[best_path[0]] + " " + names[best_path[1]] + " " + names[best_path[2]] + " " + names[best_path[3]] + " " + names[best_path[4]] + " " + names[best_path[5]] + " " + names[best_path[6]] + " " + names[best_path[7]]);
    }
    
    private static int fitness(int[] path) {
        int sum = 0;
        for(int i=1; i<8; i++){
            sum += dist[path[i-1]][path[i]];
        }
        return (5600 - sum);
    }

    private static ArrayList<int[]> initialization(int k) {
       ArrayList<int[]> ret = new ArrayList<int[]>();
       Boolean[] alr = new Boolean[8];
       for(int i=0; i<k; i++){
           int[] ass = new int[8];
           Arrays.fill(alr, Boolean.FALSE);
           for(int j=0; j<8; j++){
               int ran = (int)(Math.random()*8);
               while(alr[ran]){
                   ran = (int)(Math.random()*8);
                }
                ass[j] = ran;
                alr[ran] = true;
           }
           ret.add(ass);
       }
       return ret;
    }

    private static Pair cross_over(int[] par1, int[] par2) {
        int[] encoded_offspring1 = new int[8];
        int[] encoded_offspring2 = new int[8];
        int[] encoded_par1 = encode(par1);
        int[] encoded_par2 = encode(par2);
        int ran = (int)(Math.random()*8);
        for(int i = 0; i<8; i++){
            if (i<ran){
                encoded_offspring1[i] = encoded_par1[i];
                encoded_offspring2[i] = encoded_par2[i];
            }else{
                encoded_offspring1[i] = encoded_par2[i];
                encoded_offspring2[i] = encoded_par1[i];
            }
        }
        int[] offspring1 = decode(encoded_offspring1);
        int[] offspring2 = decode(encoded_offspring2);
        Pair a = new Pair(offspring1, offspring2);
        return a;
    }

    private static ArrayList<int[]> order(ArrayList<int[]> individuals) {
        Collections.sort(individuals, new Comparator<int[]>() {
			public int compare(int[] o1, int[] o2) {
				return fitness(o2) - fitness(o1);
			}
		});
        return individuals;
    }

    private static void print_path(ArrayList<int[]> paths) {
        for(int[] i : paths){
            System.out.println(names[i[0]] + " "  + names[i[1]]+ " " + names[i[2]] + " "+ names[i[3]] + " "+ names[i[4]] + " "+ names[i[5]] + " "+ names[i[6]] + " "+ names[i[7]] + ": " + fitness(i));
        }
        System.out.println("\n");
    }

    private static int[] encode(int[] par1) {
        ArrayList<Integer> positions = new ArrayList<Integer>(){{add(new Integer(0)); add(new Integer(1)); add(new Integer(2)); add(new Integer(3)); add(new Integer(4)); add(new Integer(5)); add(new Integer(6)); add(new Integer(7));}};
        int[] encoded = new int[8];
        for(int i=0; i<par1.length; i++){
            encoded[i] = positions.indexOf(new Integer(par1[i]));
            positions.remove(new Integer(par1[i]));
        }
    return encoded;
    }

    private static int[] decode(int[] encoded_offspring1) {
        ArrayList<Integer> positions = new ArrayList<Integer>(){{add(new Integer(0)); add(new Integer(1)); add(new Integer(2)); add(new Integer(3)); add(new Integer(4)); add(new Integer(5)); add(new Integer(6)); add(new Integer(7));}};
        int[] decoded = new int[8];
        for(int i=0; i<encoded_offspring1.length; i++){
            decoded[i] = positions.get(encoded_offspring1[i]);
            positions.remove(positions.get(encoded_offspring1[i]));
        }
        return decoded;
    }

    private static ArrayList<int[]> get_individuals(ArrayList<int[]> population, int k) {
        population = order(population);
        ArrayList<int[]> individuals = new ArrayList<>();
        for(int i =0; i<k; i++){
            individuals.add(population.get(i));
        }
        return individuals;
    }
    
}
