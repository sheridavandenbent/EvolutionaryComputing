import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.util.Properties;
import java.util.*;

import java.lang.Math;

class Pair implements Comparable<Pair> {
  public final int index;
  public final int value;

  public Pair(int index, int value) {
    this.index = index;
    this.value = value;
  }

  @Override
  public int compareTo(Pair other) {
    return Integer.valueOf(this.value).compareTo(other.value);
  }
}

class PairD implements Comparable<PairD> {
  public final int index;
  public final double value;

  public PairD(int index, double value) {
    this.index = index;
    this.value = value;
  }

  @Override
  public int compareTo(PairD other) {
    return new Double(this.value).compareTo(other.value);
  }
}

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

  /* Check if array contains value */
  public boolean contains(int[] array, int val) {
    for(int i = 0; i < array.length; i++) {
      if(val == array[i]) return true;
    }
    return false;
  }

/* Method to pick m survivors out of a population of size m + l via round-robin
     population[m+l][NO_DIMENSIONS]
     returns double[m][NO_DIMENSIONS] */
  public double[][] round_robin(double[][] population, int m) {
    final int q = 10; /* As is typical */
    final int pop_size = population.length;

    /* Pre-compute fitness for every individual */
    double fit[] = new double[pop_size];
    for(int i = 0; i < pop_size; i++) {
      fit[i] = (double) evaluation_.evaluate(population[i]);
    }

    /* Create an array containing <index, wins> pairs.
      This way we can keep track of the original index after sorting. */
    Pair results[] = new Pair[pop_size];

    /* For every individual, pick 10 random opponents and count the number of wins */
    for(int i = 0; i < pop_size; i++) {
      int wins = 0;
      int prev_opps[] = new int[q];

      for(int j = 0; j < q; j++){
        int opp;

        /* Avoid picking yourself or previous opponents */
        do {
           opp = (int) (pop_size * Math.random());
        } while(opp == i || contains(prev_opps, opp));

        /* FIGHT! */
        if(fit[i] > fit[opp])
          wins++;

        /* Add to fought opponents */
        prev_opps[j] = opp;
      }
      results[i] = new Pair(i, wins);
    }

    /* Sort results */
    Arrays.sort(results);

    /* Select the m survivors and return them */
    double survivors[][] = new double[m][NO_DIMENSIONS];
    for(int i = 0; i < m; i++) {
      int index = results[i].index;
      System.arraycopy(population[index], 0, survivors[i], 0, NO_DIMENSIONS);
    }
    return survivors;
  }

  /* Method to pick m survivors out of a population of size l via (m,l)-selection
     population[m+l][NO_DIMENSIONS]
     returns double[m][NO_DIMENSIONS] 

     NB: l needs to be larger than m. This will throw OutOfBounds otherwise */
  public double[][] ml_selection(double[][] offspring, int m) {
    final int off_size = offspring.length;

    /* Compute fitness for every individual and add to results array*/
    double fit;
    PairD results[] = new PairD[off_size];

    for(int i = 0; i < off_size; i++) {
      fit = (double) evaluation_.evaluate(offspring[i]);
      results[i] = new PairD(i, fit);
    }

    /* Sort results */
    Arrays.sort(results);

    /* Select the m survivors and return them */
    double survivors[][] = new double[m][NO_DIMENSIONS];
    for(int i = 0; i < m; i++) {
      int index = results[i].index;
      System.arraycopy(offspring[index], 0, survivors[i], 0, NO_DIMENSIONS);
    }
    return survivors;
  }
  

	public void run()
	{
		// Run your algorithm here
        setSeed(5);
        int evals = 0;
        
        // init population
        /* All dummy values for now */
        double child[] = new double[NO_DIMENSIONS];  
        for(int i = 0; i< NO_DIMENSIONS; i++) {
          boolean neg = rnd_.nextDouble() > 0.5;
          child[i] = 5 * rnd_.nextDouble();
          if(neg) child[i] *= -1;
          System.out.print(child[i] + ", ");
        }

        double std_devs[] = new double[NO_DIMENSIONS];
        for(int i = 0; i< NO_DIMENSIONS; i++) {
          std_devs[i] = rnd_.nextDouble();
        }
        double std_dev_th = 0.1;

        // calculate fitness
        while (evals<evaluations_limit_) {
            // Select parents
            // Apply crossover / mutation operators
            /* Using uncorrelated mutation with n step sizes */   
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
