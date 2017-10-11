import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.util.Properties;
import java.util.*;

import java.lang.Math;

public class player15 implements ContestSubmission
{
	Random rnd_;
	ContestEvaluation evaluation_;
    private int evaluations_limit_;
  private static final int NO_DIMENSIONS = 10;

	public player15()
	{
		rnd_ = new Random();
	}
	
	public void setSeed(long seed)
	{
		// Set seed of algortihms random process
		rnd_.setSeed(seed);
	}

	public void setEvaluation(ContestEvaluation evaluation)
	{
		// Set evaluation problem used in the run
		evaluation_ = evaluation;
		
		// Get evaluation properties
		Properties props = evaluation.getProperties();

        // This can be used to check for properties
        // Enumeration keys = props.keys();
        // while (keys.hasMoreElements()) {
        //     String key = (String)keys.nextElement();
        //     String value = (String)props.get(key);
        //     System.out.println(key + ": " + value);
        // }

        // Get evaluation limit
        evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));
		// Property keys depend on specific evaluation
		// E.g. double param = Double.parseDouble(props.getProperty("property_name"));
        boolean isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
        boolean hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        boolean isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));

        /* PROPERTIES */
        //
        // BentCigar
        // Evaluations: 10000
        // Regular: false
        // Multimodal: false
        // Separable: false
        //
        // Katsuura
        // Evaluations: 1000000
        // Regular: false
        // Multimodal: true
        // Separable: false
        //
        // Schaffers
        // Evaluations: 100000
        // Regular: true
        // Multimodal: true
        // Separable: false

		// Do sth with property values, e.g. specify relevant settings of your algorithm
        if (isMultimodal) {
            // Do sth
        } else {
            // Do sth else
        }
    }
    
	public void run()
	{
		// Run your algorithm here
        setSeed(5);
        int evals = 0;
        System.out.println("heuuu");
        
        // init population
        /* All dummy values for now */
        double dummy_parent[] = new double[NO_DIMENSIONS];  
        for(int i = 0; i< NO_DIMENSIONS; i++) {
          dummy_parent[i] = rnd_.nextDouble();
        }

        double std_devs[] = new double[NO_DIMENSIONS];
        for(int i = 0; i< NO_DIMENSIONS; i++) {
          std_devs[i] = rnd_.nextDouble();
        }
        double std_dev_th = 0.1;

        // calculate fitness
        while (evals<3) {
            // Select parents
            // Apply crossover / mutation operators
            double child[] = new double[NO_DIMENSIONS];
            
            double t = 1. / Math.sqrt(2 * Math.sqrt((double)NO_DIMENSIONS));
            double tp = 1. / Math.sqrt((double) 2 * NO_DIMENSIONS);
            double new_dev;

            for(int i = 0; i< NO_DIMENSIONS; i++) {
              /* Update standard deviation */
              new_dev = std_devs[i] * Math.pow(Math.E, tp * Math.random() + t * Math.random());
              
              /* If smaller than threshold, set it to threshold */
              if(new_dev < std_dev_th) new_dev = std_dev_th;
              std_devs[i] = new_dev;

              /* Update chromosome value */
              child[i] += std_devs[i] * Math.random();
            }

            // Check fitness of unknown fuction
            Double fitness = (double) evaluation_.evaluate(child);
            evals++;
            // Select survivors
        }

	}
}
