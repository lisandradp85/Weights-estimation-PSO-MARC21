package PSO_Algorithms;

/*
 * PSOLearning.java
 *
 * Algoritmo Pso con Variante de Restriccion y Algoritmos geneticos para inicializar
 * heuristicamente la bandada. Algoritmo PSOLearning utilizado:
 *   Vi= cr*(wk*Vi + c1*rand()*(Xpbesti-Xi) + c2*rand()*(Xgbest-Xi))
 *   Xi= Xi + Vi
 *
 */
import Benchmark.Problem;
import Benchmark.WeightOfTheFieldsInMarc21Via4b;
import GUI.GProgressBar;
import GUI.Visual;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author gnapoles
 */
public abstract class PSO {

    private static HashMap<String, Object> bundle = new HashMap<>();

    public static double getMin_bound() {
        return min_bound;
    }

    public static double getMax_bound() {
        return max_bound;
    }

    public static void putParameter(String key, Object value) {
        bundle.put(key, value);
    }

    public static Object getParameters(String key) {
        return bundle.get(key);
    }
    /**
     * algorithm structures
     */
    protected Particle xgbest = null;
    protected ArrayList<Particle> xpbest = null;
    protected ArrayList<Particle> swarm = null;
    protected ArrayList<Particle> speed = null;
    /**
     * control variables
     */
    protected int k = 0;
    private int dimension = 0;
    protected int number_particles = 0;
    protected int number_generations = 0;
    /**
     * Function for evaluation
     */
    protected Problem test = null;
    /**
     * extremos del intervalo
     */
    protected static double min_bound = Double.MIN_VALUE;
    protected static double max_bound = Double.MAX_VALUE;
    protected double bias = 0;
    /**
     * PSO parameters
     */
    protected double c1 = 2, c2 = 2, wk = 0.72984;

    // constructor
    public PSO(int particles, int generations, int problem) {
        switch (problem) {
            case 5:
                test = new WeightOfTheFieldsInMarc21Via4b();
                break;
            case 6:
                test = new WeightOfTheFieldsInMarc21Via4b((int[]) bundle.get("arrOfLevels"));
                break;
            default:
                throw new RuntimeException("Problema no definido");

        }

        bias = test.getBias();
        min_bound = test.getMin_bound();
        max_bound = test.getMax_bound();
        dimension = test.getDimension();
        number_particles = particles;
        number_generations = generations;

        reset();
    }

    public boolean isBetter(double a, double b) {
        return test.isBetter(a, b);
    }

    public double getBias() {
        return bias;
    }

    public Problem getTest() {
        return test;
    }

    // heuristic averageEvaluation
    public double computeFitnessValue(Particle p) {
        double eval = test.f(p.getGenome());
        p.setEvaluation(eval);
        return eval;
    }

    // run search process
    public abstract Particle runPSOAlgorithm();

    // a diversity measure type (1)
    protected double computeSwarmRadius() {
        double dist = 0.0;
        for (int i = 0; i < number_particles; i++) {
            Particle xi = swarm.get(i);
            dist += xgbest.getEucledeanDistance(xi);
        }
        return dist / (double) number_particles;
    }

    // a diversity measure type (2)
    protected double computeTotalDiversity() {

        double s;
        Particle ave = new Particle(dimension);
        for (int i = 0; i < dimension; i++) {
            s = 0;
            for (int j = 0; j < number_particles; j++) {
                s += swarm.get(j).getGenome()[i];
            }
            ave.getGenome()[i] = s / (double) number_particles;
        }

        double diference = 0;
        double distance = 0;
        for (int i = 0; i < number_particles; i++) {
            for (int j = 0; j < dimension; j++) {
                diference += Math.pow(swarm.get(i).getGenome()[j] - ave.getGenome()[j], 2);
            }
            distance += Math.sqrt(diference);
        }
        return distance / (double) number_particles;
    }

    public int getDimension() {
        return dimension;
    }

    public void reset() {
//        System.out.println("Reseteo Iniciado");
        long Start = System.currentTimeMillis();
        xgbest = test.getParticleRandom();;
        computeFitnessValue(xgbest);
        this.xpbest = new ArrayList<>(number_particles);
        this.swarm = new ArrayList<>(number_particles);
        this.speed = new ArrayList<>(number_particles);

        for (int iter = 0; iter < this.number_particles; iter++) {
//            System.out.print("X"+iter);
            Particle xi = getParticleRandom();
//            System.out.print("V"+iter);
            Particle vi = getParticleRandom();

            if (computeFitnessValue(xi) < xgbest.getEvaluation()) {
                xgbest = xi.clone();
            }

            xpbest.add(xi.clone());
            swarm.add(xi);
            speed.add(vi);
        }

        synchronized (Visual.b) {
            GProgressBar pb = (GProgressBar) PSO.getParameters("pb");
            Visual.SumTime += ((long) ((System.currentTimeMillis() - Start)));
            Visual.time = (long) ((Visual.SumTime / (long) (pb.getValue() + 1)) * (pb.getMaxValue() - pb.getValue()));

        }
//        System.out.println("Reseteo Finalizado"); 
    }

    public Particle getParticleRandom() {
        return test.getParticleRandom();
    }

}
