/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package psoformarc21;

import Benchmark.WeightOfTheFieldsInMarc21Via4b;
import GUI.Visual;
import PSO_Algorithms.LSVN;
import PSO_Algorithms.PSO;
import PSO_Algorithms.Particle;
import javax.swing.JTextArea;

/**
 *
 * @author Joker
 */
public class PSOforMARC21 {

    public static JTextArea out;
//    public static ArrayList<Particle> lbest;

    // validate PSO algorithm
    public static Particle runAlgorithm(int number_particles, int number_generations, int problem, int number_tests) {
//        lbest = new ArrayList<>();
//        double error = 0.0;
//        double averageError = 0.0;
//        double maxError = Double.NEGATIVE_INFINITY;
//        double minError = Double.POSITIVE_INFINITY;
//        double bias;

        PSO pso = new LSVN(number_particles, number_generations, problem);
        Particle gbest = pso.getParticleRandom();
        pso.computeFitnessValue(gbest);
        int dimension = pso.getTest().getDimension();

        Particle prom = new Particle(dimension);
        int i;
        for (i = 0; i < number_tests; i++) {
            synchronized (Visual.run) {
                if (!Visual.run) {
                    break;
                }
            }
//            System.out.println("test # " + i);
            if (out != null) {
                out.append("\n  Prueba Independiente # " + (i+1));
            }
            Particle best = pso.runPSOAlgorithm();

            prom = prom.add(best);

//            System.out.println("\t\tMejor de esta prueba " + best);
//            if (out != null) {
//                out.append("\n\t\tMejor de esta prueba " + best);
//            }
//            bias = pso.getBias();
//            averageError += (best.getEvaluation() - bias) / number_tests;
////            System.out.println((best.getEvaluation() - bias));
//            if ((error = (best.getEvaluation() - bias)) < minError) {
//                minError = error;
//            }
//
//            if ((error = (best.getEvaluation() - bias)) > maxError) {
//                maxError = error;
//            }
            if (pso.isBetter(best.getEvaluation(), gbest.getEvaluation())) {
                gbest = best;
            }
            synchronized (Visual.run) {
                if (!Visual.run) {
                    break;
                }
            }
            if (i + 1 < number_tests) {

                pso.reset();
            }

        }

        prom.mpy(1.0 / ((double) i));

//        System.out.println("Problem: " + problem);
//        System.out.println("Minimun Error: " + minError);
//        System.out.println("Maximun Error: " + maxError);
//        System.out.println("Average Error*: " + averageError);
//        System.out.println("-------------------------------------");
        WeightOfTheFieldsInMarc21Via4b test = (WeightOfTheFieldsInMarc21Via4b) pso.getTest();
        System.out.println("\tMejor Solución de todas las pruebas realizadas");
        if (out != null) {
            out.append("\n  Mejor Solución de todas las pruebas realizadas");
        }
        test.toInterpretSolution(gbest);

        System.out.println("\tSolución Promedio de todas las pruebas realizadas");
        if (out != null) {
            out.append("\n  Solución Promedio de todas las pruebas realizadas");
        }
        test.toInterpretSolution(prom.normalize());

        
       
        return gbest;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Algorithm: LSVN");
        //runAlgorithm(number_particles,  number_generations,   problem    ,number_tests)
        Particle gbest = runAlgorithm(100, 1000, 5, 1);
        System.out.println("Mejor de todos " + gbest);
        double[] genome = gbest.getGenome();
        for (int i = 0; i < genome.length; i++) {
            System.out.println(i + "  " + genome[i]);

        }
//        System.out.println(new Particle(WeightOfTheFieldsInMarc21Via4a.getSolution(gbest)).normalize().toString());
    }
}
