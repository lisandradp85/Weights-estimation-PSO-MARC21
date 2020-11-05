/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Benchmark;

import PSO_Algorithms.Particle;
import java.util.Arrays;
import static psoformarc21.PSOforMARC21.out;

/**
 *
 * @author Joker
 */
public class WeightOfTheFieldsInMarc21Via4b extends Problem {

    private int[] numberFieldForLevel;

    /**
     *
     * @param m_dimension
     */
    public WeightOfTheFieldsInMarc21Via4b() {
        super();
        this.m_bias = 0.9;
        this.min_bound = 0.0;
        this.max_bound = 1.0;
        this.m_dimension = 274;
        this.m_description = "Pesos de los campos para MARC 21";
        numberFieldForLevel = new int[5];
        numberFieldForLevel[0] = 1;
        numberFieldForLevel[1] = 3;
        numberFieldForLevel[2] = 5;
        numberFieldForLevel[3] = 7;
        numberFieldForLevel[4] = 258;

    }

    public WeightOfTheFieldsInMarc21Via4b(int[] numberFieldForLevel) {
        super();
        this.m_dimension = 0;
        this.m_description = "Pesos de los campos para MARC21";
        this.m_bias = 0.9;
        this.min_bound = 0.0;
        this.max_bound = 1.0;
        this.numberFieldForLevel = numberFieldForLevel;
        for (int c : numberFieldForLevel) {
            this.m_dimension += c;
        }

    }

    public void toInterpretSolution(Particle s) {
        double[] x = s.getGenome();
        int pos = 0;
        int cant = 0;
        double res = 0;
//        System.out.println("\n");
        if (out != null) {
            out.append("\n");;
        }
        for (int n = 0; n < numberFieldForLevel.length; n++) {
            cant = numberFieldForLevel[n];
//            System.out.print("\t Nivel " + (n + 1));
            if (out != null) {
                out.append("\n\t Nivel " + (n + 1));
            }
            for (int i = 0; i < cant; i++) {
                res += x[pos++];
            }
//            System.out.println("  Suma " + res);
            if (out != null) {
                out.append("  Suma " + res);
            }
        }
//        System.out.println("\n");
        if (out != null) {
            out.append("\n");;
        }
    }

    @Override
    public double f(double[] x) {
        double res = 0;
        int pos = 0;
        int cant = 0;
        double w = 0;
        double[] arrRes = new double[m_dimension];
        for (int n = 0; n < numberFieldForLevel.length; n++) {
            cant = numberFieldForLevel[n];
            arrRes[n] = 0.0;
//            w = numberFieldForLevel.length - n;
              w = (numberFieldForLevel.length + 1) - n;
              // Abajo esta como se hacia antes con el exponente -- potencia la funcion de calidad
           // w = Math.pow((numberFieldForLevel.length - n),numberFieldForLevel[(numberFieldForLevel.length - 1) - n]);
//            for (int p = n + 1; p < numberFieldForLevel.length; p++) {
//                w *= numberFieldForLevel[p];
//            }
            for (int i = 0; i < cant; i++) {
                arrRes[n] += x[pos++];
            }
            arrRes[n] *= w;
            res += arrRes[n];
        }
        return res;
    }

    @Override
    public Particle getParticleRandom() {
        Particle particleRandom = super.getParticleRandom();
        particleRandom.quickSort2();
//        double[] genome = particleRandom.getGenome();
//        Arrays.sort(genome);
//        double[] newG = new double[genome.length];
//        for (int i = 0; i < genome.length; i++) {
//            newG[i] = genome[(genome.length - 1) - i];
//        }
//        particleRandom.setGenome(newG);
        return particleRandom;
    }

    @Override
    public boolean isBetter(double currentEvaluation, double bestEvaluation) {
        return currentEvaluation > bestEvaluation;
    }

}
