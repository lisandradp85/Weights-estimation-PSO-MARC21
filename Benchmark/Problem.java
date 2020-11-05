//
// Special Session on Real-Parameter Optimization at CEC-05
// Edinburgh, UK, 2-5 Sept. 2005
//
// Organizers:
//	Prof. Kalyanmoy Deb
//		deb@iitk.ac.in
//		http://www.iitk.ac.in/kangal/deb.htm
//	A/Prof. P. N. Suganthan
//		epnsugan@ntu.edu.sg
//		http://www.ntu.edu.sg/home/EPNSugan
//
// Java version of the test functions
//
// Matlab reference code
//	http://www.ntu.edu.sg/home/EPNSugan
//
// Java version developer:
//	Assistant Prof. Ying-ping Chen
//		Department of Computer Science
//		National Chiao Tung University
//		HsinChu City, Taiwan
//		ypchen@csie.nctu.edu.tw
//		http://www.csie.nctu.edu.tw/~ypchen/
//
// Typical use of the test functions in the benchmark:
//
//		// Create a benchmark object
// 		benchmark theBenchmark = new benchmark();
//		// Use the factory function call to create a test function object
//		//		test function 3 with 50 getDimension
//		//		the object class is "Problem"
//		Problem aTestFunc = theBenchmark.testFunctionFactory(3, 50);
//		// Invoke the function with x
//		double result = aTestFunc.f(x);
//
// Version 0.90
//		Currently, this version cannot handle any numbers of dimensions.
//		It cannot generate the shifted global optima and rotation matrices
//		that are not provided with the Matlab reference code.
//		It can handle all cases whose data files are provided with
//		the Matlab reference code.
// Version 0.91
//		Revised according to the Matlab reference code and the PDF document
//		dated March 8, 2005.
//
package Benchmark;

import PSO_Algorithms.Particle;

public abstract class Problem {

    protected int m_dimension;
    protected double m_bias;
    protected String m_description;
    protected double min_bound;
    protected double max_bound;

    // Function body to be defined in the child classes
    public abstract double f(double[] x);

    public Particle getParticleRandom() {
        double[] newValues = new double[m_dimension];
        for (int i = 0; i < m_dimension; i++) {
            newValues[i] = Math.random();
        }
        return new Particle(newValues).normalize();

//
//        // get valid bounds
//        double a = min_bound;
//        double b = max_bound;
//
//        // create empty particle
//        double[] newValues = new double[m_dimension];
//        double suma = es[i];
//        }
//        newValues[m_dimension - 1] = b;
//        suma += newValues[m_dimension - 1];
//        // assign values          
//        return new Particle(newValues);0;
//        for (int i = 0; i < m_dimension - 1; i++) {
//            newValues[i] = Math.random() * (b - a) + a;
//            b -= newValues[i];
//            suma += newValues[i];
//        }
//        newValues[m_dimension - 1] = b;
//        suma += newValues[m_dimension - 1];
//        // assign values          
//        return new Particle(newValues);
    }

    // Property functions common for all child classes
    public static double[] getSolution(Particle gbest) {
        return gbest.getGenome();
    }

    public int getDimension() {
        return (m_dimension);
    }

    public double getBias() {
        return (m_bias);
    }

    public String getDescription() {
        return (m_description);
    }

    public double getMin_bound() {
        return min_bound;
    }

    public double getMax_bound() {
        return max_bound;
    }

    public boolean isBetter(double currentEvaluation, double bestEvaluation) {
        return currentEvaluation < bestEvaluation;
    }

}
