package PSO_Algorithms;

/*
 * RL_PSO.java
 *
 * Algoritmo Pso con Variante de Restriccion y Algoritmos geneticos para inicializar
 * heuristicamente la bandada. Algoritmo RL_PSO utilizado:
 *   Vi= cr*(wk*Vi + c1*rand()*(Xpbesti-Xi) + c2*rand()*(Xgbest-Xi))
 *   Xi= Xi + Vi
 *
 */
import Benchmark.WeightOfTheFieldsInMarc21Via4b;
import GUI.GProgressBar;
import GUI.Visual;
import static psoformarc21.PSOforMARC21.out;

/**
 *
 * @author gnapoles
 */
public class LSVN extends PSO {

    // swarm diversity threshold
    public static final double epsilon = 0.05;

    // number of number_generations without progress
    public static final double np_max = 50;

    // number of no_neighborhoods
    public static final int no_neighborhoods = 10;

    // learning factor
    private double l_factor = 0;

    // local search prob.
    private double ls_prob = 0.0;

    // constructor
    public LSVN(int number_particles, int number_generations, int problem) {
        super(number_particles, number_generations, problem);
        l_factor = 1 / (double) (number_generations * 2);
    }

    // number_particles are to close?
    private boolean isSwarmCompact() {
        double maxDist = computeTotalDiversity();
        return (maxDist / (getMax_bound() - getMin_bound()) < epsilon);
    }

    // disperse swarm in neighbor space
    private void reinvigorateSwarm(int neighborhoods) {
//        System.out.println("Revitalizando Enjambre");
        swarm.clear();
        speed.clear();

        int iter = 0;
        for (int j = 1; j <= neighborhoods; j++) {
            double n_factor = (double) j / (double) neighborhoods;
            for (int i = 0; i < (number_particles / neighborhoods); i++) {
                Particle a1 = Particle.getNextNeighbor4(xgbest, n_factor);
                Particle a2 = Particle.getNextNeighbor4(xgbest, n_factor);
//                System.out.print("V"+iter);
                Particle vi = getParticleRandom();
                Particle xi = xgbest.add(a1.sust(a2));

                for (int h = 0; h < getDimension(); h++) {
                    if (Math.random() < 0.5) {
                        xi.getGenome()[h] = xgbest.getGenome()[h];
                    }
                }

                swarm.add(iter, xi.normalize());
                speed.add(iter, vi);
                iter++;
            }
        }
//        System.out.println("NP "+iter);
        swarm.add(0, xgbest.clone());
        xpbest.add(0, xgbest.clone());
//        System.out.println("Revitalizacion de Enjambre Terminada");

    }

    // get new mutant using differential evolution
    public Particle getMutant(Particle xi) {
//        System.out.print(" Mutar");
        if (Math.random() < (1 - ls_prob)) {
//            System.out.println(" No P");
            return xi;
        }
//
//        int index1, index2, index3;
//        index1 = Math.abs(new Random().nextInt()) % number_particles;
//        while (true) {
//            index2 = Math.abs(new Random().nextInt()) % number_particles;
//            if (index1 != index2) {
//                break;
//            }
//        }
//        while (true) {
//            index3 = Math.abs(new Random().nextInt()) % number_particles;
//            if (index1 != index3 && index2 != index3) {
//                break;
//            }
//        }
//
//        Particle a1 = swarm.get(index1);
//        Particle a2 = swarm.get(index2);
//        Particle a3 = swarm.get(index3);
//        Particle mutant = a1.add(a2.sust(a3));
        Particle mutant = new Particle(getDimension());
        for (int j = 0; j < getDimension(); j++) {
//            if (Math.random() < 0.8) {
            mutant.getGenome()[j] = (Math.random() < 0.5) ? xi.getGenome()[j] : Math.random();//1;//
//            }
        }
        mutant.normalize();
        if (isBetter(computeFitnessValue(mutant), computeFitnessValue(xi))) {
            ls_prob += l_factor;
//            System.out.println(" Si");
            return mutant;

        }
        ls_prob -= l_factor;
//        System.out.println(" No F");
        return xi;
    }

    // run search process
    @Override
    public Particle runPSOAlgorithm() {

        int no_progress = 0;
        double previus = 0;
        k = 0;
        WeightOfTheFieldsInMarc21Via4b testW = (WeightOfTheFieldsInMarc21Via4b) test;
        GProgressBar pb = (GProgressBar) PSO.getParameters("pb");

//        int cont=1;
        long lStartTime = 0;
        while ((k++) < number_generations) {
//            if (cont++==1) {
            lStartTime = System.currentTimeMillis();
//            }
//            System.out.println("\tGenerations # " + k);

            // starting LSVN procedure
            previus = xgbest.getEvaluation();
            if (isSwarmCompact() || no_progress == np_max) {
                reinvigorateSwarm(no_neighborhoods);
                no_progress /= 2;
            }
            synchronized (Visual.run) {
                if (!Visual.run) {

                    break;
                }
            }
            // updating best records
            for (int i = 0; i < number_particles; i++) {
                Particle xi = swarm.get(i);
                Particle pi = xpbest.get(i);
                if (isBetter(computeFitnessValue(xi), pi.getEvaluation())) {
                    xpbest.set(i, xi.clone());
                    if (isBetter(xi.getEvaluation(), xgbest.getEvaluation())) {
                        xgbest = xi.clone();
                    }
                }
            }
//            System.out.println("\txgbest current " + xgbest);
//            if (out != null) {
//                out.append("\n\txgbest current " + xgbest);
//            }

//            System.out.println("Gbest");
//            if (out != null) {
//                out.append("\nGbest");
//            }
            if (out != null) {
                out.append("\n\tMejor de toda la Generación # " + k);;
            }
            testW.toInterpretSolution(xgbest);

            // updating non-progress states
            boolean reset = xgbest.getEvaluation() == previus;
            no_progress = (reset) ? no_progress + 1 : 0;
            synchronized (Visual.run) {
                if (!Visual.run) {

                    break;
                }
            }
            // updating particle's positions
            for (int i = 0; i < number_particles; i++) {
//                System.out.print("X"+i);
                Particle xi = swarm.get(i);
                Particle vi = speed.get(i);
                Particle pi = xpbest.get(i);

                Particle temp1 = pi.sust(xi).mpy(c1 * Math.random());
                Particle temp2 = xgbest.sust(xi).mpy(c2 * Math.random());
                vi = vi.mpy(0.5 + Math.random() / 2).add(temp1.add(temp2)).clampVelocity();//.clampVelocity();
                xi = getMutant(vi.add(xi).normalize());

//                System.out.println("X"+i+"  "+xi);
                speed.set(i, vi);
                swarm.set(i, xi);
            }
            if (pb != null) {
                pb.setValue(pb.getValue() + 1);//                
            }
            synchronized (Visual.run) {
                if (!Visual.run) {

                    break;
                }
            }

//             if (cont==2) {                
            synchronized (Visual.b) {
                Visual.SumTime += ((long) ((System.currentTimeMillis() - lStartTime)));
                Visual.time = (long) ((Visual.SumTime / (long) (pb.getValue() + 1)) * (pb.getMaxValue() - pb.getValue()));

            }
//            }else if (cont==5) {
//                cont=1;
//            }

        }
        return xgbest;
    }
}
