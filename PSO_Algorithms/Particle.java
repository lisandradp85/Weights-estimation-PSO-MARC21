/*
 * Particula.java
 *
 * Created on 06 de marzo de 2010, 12:07 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package PSO_Algorithms;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author gnapoles
 */
public class Particle {

    /**
     * particle attributes
     */
    private int dimension = 0;
    private double[] genome = null;
    private double evaluation = Double.MAX_VALUE;

    // make empty particle
    public Particle(int dimension) {
        this.dimension = dimension;
        this.genome = new double[dimension];

        for (int i = 0; i < genome.length; i++) {
            genome[i] = 0;
        }
    }

    // make new particle
    public Particle(double[] values) {
        this.dimension = values.length;
        this.genome = new double[dimension];
        for (int i = 0; i < dimension; i++) {
            this.genome[i] = values[i];
        }
    }

    // make new particle
    public Particle(Particle p) {
        double pvalues[] = p.getGenome();
        this.dimension = pvalues.length;
        this.genome = new double[dimension];
        for (int i = 0; i < dimension; i++) {
            this.genome[i] = pvalues[i];
        }
    }

    // normalize particle Gonsalo
//    public Particle normalize() {
//        for (int i = 0; i < genome.length; i++) {
//            if (genome[i] < PSO.getMin_bound()) {
//                genome[i] = 2 * PSO.getMin_bound() - genome[i];
//            }
//            if (genome[i] > PSO.getMax_bound()) {
//                genome[i] = 2 * PSO.getMax_bound() - genome[i];
//            }
//        }
//        return this;
//    }
    // normalize particle Francisco
    public Particle normalize() {
        double suma = genome[0];
        double total = 0;
        int min = 0;//dimension 
        for (int i = 1; i < genome.length; i++) {
            if (genome[i] < genome[min]) {
                min = i;
            }
            suma += genome[i];
        }
//        quickSort2(); genome[min] <= 0
        if (genome[min] < 0) {
            double inc = -genome[min];//+ 0.00000000000000000000000000000000005;
            suma += genome.length * inc;
            for (int i = 0; i < genome.length; i++) {
                genome[i] += inc;//suma += 
            }
        }
        while (suma > 1) {
//            System.out.println("" + suma);
            total = suma;
            suma = 0;
            min = -1;
            double val = Double.MAX_VALUE;
            for (int i = 0; i < genome.length; i++) {
                genome[i] = genome[i] / total;
                if (genome[i] < val) {
                    min = i;
                    val = genome[i];
                }
                suma += genome[i];
            }

        }
        if (suma < 1) {//
            genome[min] += (1 - suma);
        }
        this.quickSort2();
        return this;
    }

    // p1+p2 without normalize
    public Particle add(Particle p2) {
        Particle particle = new Particle(dimension);
        for (int i = 0; i < dimension; i++) {
            particle.getGenome()[i] = genome[i] + p2.getGenome()[i];
        }
        return particle;
    }

    // p1-p2 without normalize
    public Particle sust(Particle p2) {
        Particle particle = new Particle(dimension);
        for (int i = 0; i < dimension; i++) {
            particle.getGenome()[i] = genome[i] - p2.getGenome()[i];
        }
        return particle;
    }

    // e*p2 without normalize
    public Particle mpy(double escalar) {
        Particle particle = new Particle(dimension);
        for (int i = 0; i < dimension; i++) {
            particle.getGenome()[i] = genome[i] * escalar;
        }
        return particle;
    }

    // clamp in range [-Vmax,Vmax]
    public Particle clampVelocity() {
        double range = PSO.getMax_bound() - PSO.getMin_bound();
        double Vmax = range * 0.5;

        for (int i = 0; i < genome.length; i++) {
            if (genome[i] < -Vmax) {
                genome[i] = -Vmax;
            }
            if (genome[i] > Vmax) {
                genome[i] = Vmax;
            }
        }
        
        return this;
    }

    // opposite particle
    public Particle opposite() {
        Particle opp = new Particle(dimension);
        for (int i = 0; i < genome.length; i++) {
            opp.getGenome()[i] = PSO.getMin_bound() + PSO.getMax_bound() - genome[i];
        }
        return opp;
    }

    // next Cauchy number
    public static double nextCauchy() {
        double temp = Math.tan(Math.PI * (Math.random() - 0.5));
        while (temp < -1 || temp > 1) {
            temp /= 10;
        }
        return (temp + 1) / 2;
    }

    // next Lévy number
    public static double nextLevy(double c, double alpha) {
        double u, v, t, s;
        u = Math.PI * (Math.random() - 0.5);

        do {
            v = -Math.log(Math.random());
        } while (v == 0);

        //General case
        t = Math.sin(alpha * u) / Math.pow(Math.cos(u), 1 / alpha);
        s = Math.pow(Math.cos((1 - alpha) * u) / v, (1 - alpha) / alpha);
        double temp = c * t * s;
        while (temp < -1 || temp > 1) {
            temp /= 10;
        }
        return (temp + 1) / 2;
    }

    // get the eucledean distance from current
    public double getEucledeanDistance(Particle p) {
        double distance = 0.0;
        for (int i = 0; i < genome.length; i++) {
            distance += Math.pow(genome[i] - p.getGenome()[i], 2);
        }
        return Math.sqrt(distance);
    }

    // get the cosine distance from current
    public double getCosineDistance(Particle p) {
        double distance = 0.0;
        for (int i = 0; i < genome.length; i++) {
            double temp1 = 0.0, temp2 = 0.0;
            for (int j = 0; j < genome.length; j++) {
                temp1 += Math.pow(genome[j], 2);
                temp2 += Math.pow(p.getGenome()[j], 2);
            }
            distance += (genome[i] * p.getGenome()[i]) / (Math.sqrt(temp1) * Math.sqrt(temp2));
        }
        return Math.sqrt(distance);
    }

    // get next random neighbor using an Uniform distribution
    public static Particle getNextNeighbor1(Particle seed, double chi) {

        // get valid bounds
        double a = PSO.getMin_bound();
        double b = PSO.getMax_bound();

        // create empty particle
        int dimension = seed.getDimension();
        double[] newValues = new double[dimension];
        Particle newRandParticle = new Particle(dimension);
        for (int i = 0; i < dimension; i++) {
            double a1 = seed.getGenome()[i] - chi * (seed.getGenome()[i] - a);
            double b1 = seed.getGenome()[i] + chi * (b - seed.getGenome()[i]);
            newValues[i] = Math.random() * (b1 - a1) + a1;
        }

        // assign values
        newRandParticle.setGenome(newValues);
        return newRandParticle;
    }

    // get next random neighbor using a Gaussian distribution
    public static Particle getNextNeighbor2(Particle seed, double chi) {

        // get valid bounds
        double a = PSO.getMin_bound();
        double b = PSO.getMax_bound();

        // create empty particle
        int dimension = seed.getDimension();
        Particle newRand = new Particle(dimension);
        for (int i = 0; i < dimension; i++) {
            double a1 = seed.getGenome()[i] - chi * (seed.getGenome()[i] - a);
            double b1 = seed.getGenome()[i] + chi * (b - seed.getGenome()[i]);
            newRand.getGenome()[i] = new Random().nextGaussian() * (b1 - a1) + a1;
        }

        // assign values
        return newRand;
    }

    // get next random neighbor using a Cauchy distribution
    public static Particle getNextNeighbor3(Particle seed, double chi) {

        // get valid bounds
        double a = PSO.getMin_bound();
        double b = PSO.getMax_bound();

        // create empty particle
        int dimension = seed.getDimension();
        Particle newRand = new Particle(dimension);
        for (int i = 0; i < dimension; i++) {
            double a1 = seed.getGenome()[i] - chi * (seed.getGenome()[i] - a);
            double b1 = seed.getGenome()[i] + chi * (b - seed.getGenome()[i]);
            newRand.getGenome()[i] = nextCauchy() * (b1 - a1) + a1;
        }

        // assign values
        return newRand;
    }

    // get next random neighbor using a Lévy distribution
    public static Particle getNextNeighbor4(Particle seed, double chi) {

        // get valid bounds
        double a = PSO.getMin_bound();
        double b = PSO.getMax_bound();

        // create empty particle
        int dimension = seed.getDimension();
        Particle newRand = new Particle(dimension);
        for (int i = 0; i < dimension; i++) {
            double a1 = seed.getGenome()[i] - chi * (seed.getGenome()[i] - a);
            double b1 = seed.getGenome()[i] + chi * (b - seed.getGenome()[i]);
            newRand.getGenome()[i] = nextLevy(1, 0.8) * (b1 - a1) + a1;
        }

        return newRand.normalize();
    }

    @Override
    public Particle clone() {
        Particle clon = new Particle(this.dimension);
        double[] _values = new double[this.dimension];
        for (int i = 0; i < dimension; i++) {
            _values[i] = genome[i];
        }

        clon.dimension = this.dimension;
        clon.evaluation = this.evaluation;
        clon.genome = _values;
        return clon;
    }

    @Override
    public boolean equals(Object particle) {

        if (particle instanceof Particle) {
            Particle p = (Particle) particle;

            if (p.dimension != dimension) {
                return false;
            }

            for (int i = 0; i < genome.length; i++) {
                if (genome[i] != p.getGenome()[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + this.dimension;
        hash = 79 * hash + Arrays.hashCode(this.genome);
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.evaluation) ^ (Double.doubleToLongBits(this.evaluation) >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        String string = "[";
        double suma = 0;

        for (int i = 0; i < getGenome().length; i++) {
            string += getGenome()[i];
            suma += getGenome()[i];
            if (i != getGenome().length - 1) {
                string += ", ";
            }
        }
        return suma + "     " + string + "]";
    }

    // get particle genome
    public double[] getGenome() {
        return genome;
    }

    // get particle dimension
    public int getDimension() {
        return dimension;
    }

    // get particle evaluation
    public double getEvaluation() {
        return evaluation;
    }

    // set current array values
    public void setGenome(double[] values) {
        this.genome = values;
    }

    // set particle dimension
    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    // set particle evaluation
    public void setEvaluation(double evaluation) {
        this.evaluation = evaluation;
    }

    private void quicksort2(double[] a, int inicio, int fin) {   //pivote al inicio
        if (fin <= inicio) {
            return;
        } else {
            double temp;
            int i = inicio;
            int j = fin + 1;
            int pivote = inicio;
            while (i < j) {
                while ((i < fin) && a[++i] > (a[pivote]));
                while (a[--j] < (a[pivote]));
                if (i < j) {
                    temp = a[i];
                    a[i] = a[j];
                    a[j] = temp;
                }
            }
            temp = a[j];
            a[j] = a[pivote];
            a[pivote] = temp;
            quicksort2(a, inicio, j - 1);
            quicksort2(a, j + 1, fin);

        }
    }

    public void quickSort2() {
        quicksort2(genome, 0, genome.length - 1);
    }

}
