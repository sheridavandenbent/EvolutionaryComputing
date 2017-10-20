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
  int evals = 0;

  /* Globals */
  private static final int NO_DIMENSIONS = 10;
  private final double t = 1. / Math.sqrt(2 * Math.sqrt((double)NO_DIMENSIONS));
  private final double tp = 1. / Math.sqrt((double) 2 * NO_DIMENSIONS);
  private final double std_dev_mutation = Math.pow(Math.E, tp * Math.random() + t * Math.random());

  /* These should be changed when we actually test it to test all combinations */
  final int q = Integer.parseInt(System.getProperty("q")); /* As is typical */
  int pop_size = Integer.parseInt(System.getProperty("pop_size")); /* Or whatever pop_size should be */
  int off_size = Integer.parseInt(System.getProperty("off_size")); /* Or whatever pop_size should be */
  private static final double std_dev_th = Double.parseDouble(System.getProperty("std_dev_th"));
  private static final int PARENTSELECT_STYLE = Integer.parseInt(System.getProperty("PARENTSELECT_STYLE"));
  private static final int REPRODUCE_STYLE = Integer.parseInt(System.getProperty("REPRODUCE_STYLE"));
  private static final int SURVIVAL_STYLE = Integer.parseInt(System.getProperty("SURVIVAL_STYLE"));
  private static final int tournament_size = Integer.parseInt(System.getProperty("tournament_size"));
  private static final double prob_pick_best = Double.parseDouble(System.getProperty("prob_pick_best"));

  double fitnesses[] = new double[pop_size + off_size];

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
        // Evaluations are NOT the number of cycles,
        // but the number of times you are allowed to call evaluate()
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

/* SURVIVAL SELECTION (1)
Method to pick m survivors out of a population of size m + l via round-robin
population[m+l][NO_DIMENSIONS]
returns double[m][NO_DIMENSIONS] */
  public double[][] round_robin(double[][] population, int m) {
    final int pop_size = population.length;

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
           opp = (int) (pop_size * rnd_.nextDouble());
        } while(opp == i || contains(prev_opps, opp));

        /* FIGHT! */
        if(fitnesses[i] > fitnesses[opp])
          wins++;

        /* Add to fought opponents */
        prev_opps[j] = opp;
      }
      results[i] = new Pair(i, wins);
    }

    /* Sort results */
    Arrays.sort(results, Collections.reverseOrder());

    /* Select the m survivors and return them */
    double survivors[][] = new double[m][2 * NO_DIMENSIONS];
    double fit[] = new double[m];
    for(int i = 0; i < m; i++) {
      int index = results[i].index;
      System.arraycopy(population[index], 0, survivors[i], 0, 2 * NO_DIMENSIONS);
      fit[i] = fitnesses[index];
    }
    System.arraycopy(fit, 0, fitnesses, 0, m);
    return survivors;
  }

  /* SURVIVAL SELECTION (2)
  Method to pick m survivors out of a population of size l via (m,l)-selection
  offspring[m+l][NO_DIMENSIONS]
  returns double[m][NO_DIMENSIONS] 

     NB: l needs to be larger than m. This will throw OutOfBounds otherwise */
  public double[][] ml_selection(double[][] offspring, int m) {
    final int off_size = offspring.length;

    /* Compute fitness for every individual and add to results array*/
    double fitness;
    PairD results[] = new PairD[off_size];

    for(int i = 0; i < off_size; i++) {
      fitness = fitnesses[i];
      results[i] = new PairD(i, fitness);
    }

    /* Sort results */
    Arrays.sort(results, Collections.reverseOrder());

    /* Select the m survivors and return them */
    double survivors[][] = new double[m][2 * NO_DIMENSIONS];
    double fit[] = new double[m];
    for(int i = 0; i < m; i++) {
      int index = results[i].index;
      System.arraycopy(offspring[index], 0, survivors[i], 0, 2 * NO_DIMENSIONS);
      fit[i] = fitnesses[index];  
    }
    System.arraycopy(fit, 0, fitnesses, 0, m);
    return survivors;
  }

  /* RECOMBINATION (1)
  Possible to change to producing 2 children (only viable if a != 0.5)
  */
  public double[][] blend_crossover(double[] parent1, double[] parent2) { //double[][]
    double difference[] = new double[2 * NO_DIMENSIONS];  // double[][] children = new double[2][NO_DIMENSIONS];
    double a = 0.5;
    
    for (int i = 0; i < 2 * NO_DIMENSIONS; i++) {
      if (parent1[i] > parent2[i]) {
        difference[i] = parent1[i] - parent2[i];
      } else {
        difference[i] = parent2[i] - parent1[i];
      }
    }

    double u = rnd_.nextDouble();
    double gamma = (1 - 2 * a) * u - a;
    double children[][] = new double[2][2 * NO_DIMENSIONS];

    for (int i = 0; i < 2 * NO_DIMENSIONS; i++) {
      children[0][i] = (1 - gamma) * parent1[i] + gamma * parent2[i];
      if (children[0][i] < -5.0) children[0][i] = -5.0;
      if (children[0][i] > 5.0) children[0][i] = 5.0;
      children[1][i] = (1-gamma)*parent2[i] + gamma*parent1[i];
      if (children[1][i] < -5.0) children[1][i] = -5.0;
      if (children[1][i] > 5.0) children[1][i] = 5.0;

      if (i >= NO_DIMENSIONS) {
        if(children[0][i] < std_dev_th) children[0][i] = std_dev_th;
        if(children[1][i] < std_dev_th) children[1][i] = std_dev_th;
      }
    }

    return children;
  }

  /* RECOMBINATION (2)
  Produces a single child - may be changed to produce 2 different children if a != 0.5
  */
  public double[][] whole_arithmetic(double[] parent1, double[] parent2) { //double[][]
    double[][] children = new double[2][2 * NO_DIMENSIONS];   // double[][] children = new double[2][NO_DIMENSIONS];
    double a = 0.5;   // If changed, two children will have to be created
    double sum1 = 0;
    double sum2 = 0;

    for (int i = 0; i < 2 * NO_DIMENSIONS; i++) {
      sum1 = sum1 + parent1[i];
      sum2 = sum2 + parent2[i];
    }
    double weighted_sum_1 = sum1 / parent1.length;
    double weighted_sum_2 = sum2 / parent2.length;

    for (int i = 0; i < 2 * NO_DIMENSIONS; i++) {
      children[0][i] = a*weighted_sum_1 + (1-a)*weighted_sum_2;
      children[1][i] = a*weighted_sum_2 + (1-a)*weighted_sum_1;
      if (i >= NO_DIMENSIONS) {
        if(children[0][i] < std_dev_th) children[0][i] = std_dev_th;
        if(children[1][i] < std_dev_th) children[1][i] = std_dev_th;
      }
    }

    return children;
  }

  /* MUTATION
    Using uncorrelated mutation with n step sizes */     
  public void uncorrelated_mutation(double[] individual) {
    double new_dev;

    for(int i = 0; i< NO_DIMENSIONS; i++) {
      /* Update standard deviation */
      new_dev = individual[NO_DIMENSIONS + i] * std_dev_mutation;
      
      /* If smaller than threshold, set it to threshold */
      if(new_dev < std_dev_th) new_dev = std_dev_th;
      individual[NO_DIMENSIONS + i] = new_dev;

      /* Update chromosome value */
      individual[i] += individual[NO_DIMENSIONS + i] * rnd_.nextDouble();
      if (individual[i] < -5.0) individual[i] = -5.0;
      if (individual[i] > 5.0) individual[i] = 5.0;
    }
  }

  /* Initialize standard deviations for some individual */
  public void init_std_devs(double[] individual) {
    for(int i = 0; i < NO_DIMENSIONS; i++) {
      /* Using a random value between [0,2] for now */
      individual[NO_DIMENSIONS + i] = 2 * rnd_.nextDouble();
    } 
  }

  /* Initalizes all individuals in the population. Also calls init_std_devs() */
  public void init_population(double[][] population, int pop_size) {
    /* All dummy values for now */

    /* Init chromosomes between [-5,5] */
    for(int i = 0; i < pop_size; i++) {
      for(int j = 0; j < NO_DIMENSIONS; j++) {
        boolean neg = rnd_.nextDouble() > 0.5;
        population[i][j] = 5 * rnd_.nextDouble();
        if(neg) population[i][j] *= -1;
      }

      /* Init standard deviations */
      init_std_devs(population[i]);
    }
  }  

  /* Method to calculate the fitness for every individual in a population. 
    Returns the total number of evaluations up to now */
  public int calc_fitness(double[][] population, int evals, int init) {
    double to_eval[] = new double[NO_DIMENSIONS];
    if(init > 0) {
      /* Initialize initial initialization */
      for(int i = 0; i < pop_size; i++) {
        /* Evaluate only the chromosomes of the individual, not the accompanying std_devs */
        System.arraycopy(population[i], 0, to_eval, 0, NO_DIMENSIONS);

        fitnesses[i] = (double) evaluation_.evaluate(to_eval);
        evals++;
      }
    } else {
      /* New children */
      for(int i = pop_size; i < pop_size + off_size; i++) {
        /* Evaluate only the chromosomes of the individual, not the accompanying std_devs */
        System.arraycopy(population[i], 0, to_eval, 0, NO_DIMENSIONS);

        fitnesses[i] = (double) evaluation_.evaluate(to_eval);
        evals++;
      }
    }

    return evals;
  }

  /* PARENT SELECTION + CHILD MAKING */
  public void reproduce(double[][] population, int off_size) {
    for (int i = 0; i < off_size; i++) { /* we want to double the population */
      double[][] parents;
      if (PARENTSELECT_STYLE == 1) { // tournament
        parents = tournament(population, off_size);
      } else { // ranking
        parents = ranking(population, off_size);
      }
      // System.out.println("Parents length:" + parents[0].length + ", " + parents[1].length);
      double[][] children = new double[2][2 * NO_DIMENSIONS];
      if (REPRODUCE_STYLE == 1) {
        children = blend_crossover(parents[0], parents[1]);
      } else {
        children = whole_arithmetic(parents[0], parents[1]);
      }

      /* Mutation */
      uncorrelated_mutation(children[0]);
      uncorrelated_mutation(children[1]);

      // System.out.println("CHild length: " + child.length);
      population[off_size+i] = children[0];
      i++;
      population[off_size+i] = children[1];
    }
  }
  
  /* Helper functions because nobody likes Comparators */
  public int muhCompare(double[] o1, double[] o2) {
    return Double.valueOf(o1[1]).compareTo(Double.valueOf(o2[1]));
  }

  public void swap(double[] a, double[] b) {
    for(int i = 0; i < a.length; i++){
      double t = a[i];
      a[i] = b[i];
      b[i] = t;
    }
  }

  public void bubbleSort(double[][] a) {
    boolean s = false;
    do {
      s = false;
      for(int i = 1; i < a.length; i++) {
        if(muhCompare(a[i-1], a[i]) < 0) {
          swap(a[i-1], a[i]);
          s = true;
        }
      }
    } while(s);
  }

  public double[][] tournament(double[][] population, int pop_size) {

    ArrayList<Integer> arr = new ArrayList<Integer>(pop_size);
    for (int i = 0; i < pop_size; i++) {
        arr.add(i);
    }
    Collections.shuffle(arr);
    double[][] participants = new double[tournament_size][2];
    // pick the participants (random pick without replacement)
    int j = 0;
    for (int random_pick : arr) {
        participants[j][0] = random_pick;
        participants[j][1] = fitnesses[random_pick];
        j++;
        if (j == tournament_size) {
          break;
        }
    }

    bubbleSort(participants);

    double[][] parents = new double[2][NO_DIMENSIONS * 2];

    int parent1 = pick_on_probability(tournament_size);
    int parent2 = pick_on_probability(tournament_size, parent1);

    System.arraycopy(population[(int)participants[parent1][0]], 0, parents[0], 0, 2 * NO_DIMENSIONS);
    System.arraycopy(population[(int)participants[parent2][0]], 0, parents[1], 0, 2 * NO_DIMENSIONS);

    return parents;
  }

  public double[][] ranking(double[][] population, int pop_size) {
    bubbleSort(population);

    double[][] parents = new double[2][NO_DIMENSIONS * 2];

    int parent1 = pick_on_probability(pop_size);
    int parent2 = pick_on_probability(pop_size, parent1);


    System.arraycopy(population[parent1], 0, parents[0], 0, 2 * NO_DIMENSIONS);
    System.arraycopy(population[parent2], 0, parents[1], 0, 2 * NO_DIMENSIONS);

    return parents;
  }

  public int pick_on_probability(int pop_size) {
    return pick_on_probability(pop_size, -1);
  }

  public int pick_on_probability(int pop_size, int do_not_choose) {
    double p = Math.random();
    double cumulativeProbability = 0.0;
    int already_picked = 0;
    for (int i = 0; i < pop_size; i++) {
      if (i == do_not_choose && i != pop_size-1) {
        already_picked = 1;
      }
      cumulativeProbability += prob_pick_best*java.lang.Math.pow((1-prob_pick_best),i);
      if (p <= cumulativeProbability) {
          return i + already_picked;
      }
      already_picked = 0;
    }
    return pop_size-1;
  }

  public void run()
  {
    // Run your algorithm here
    setSeed(68); /* Or whatever this number should be */
    double population[][] = new double[pop_size + off_size][2 * NO_DIMENSIONS]; /* pop_size + off_size since children also have to fit */
    // Init population
    init_population(population, pop_size);

    // // Print the starting population
    // for(int i = 0; i < pop_size + off_size; i++) {
    //   for(int j = 0; j < NO_DIMENSIONS; j++) {
    //     System.out.print(population[i][j] + ", ");
    //   }
    //   System.out.println();
    // }
    
    // Calculate fitness
    evals = calc_fitness(population, evals, 0);
    
    while (evals<evaluations_limit_) {
        // Select parents and make some babyyyyyssss
        // Apply crossover / mutation operators
        reproduce(population, off_size);

        // Check fitness of unknown fuction
        evals = calc_fitness(population, evals, 1);

        // Select survivors
        double survivors[][] = new double[pop_size][2 * NO_DIMENSIONS];
        if (SURVIVAL_STYLE == 1) { // ml
          survivors = ml_selection(population, pop_size);
        } else if (SURVIVAL_STYLE == 2) { //round robin
          survivors = round_robin(population, pop_size);
        }

        for(int i = 0; i < pop_size; i++) {
          System.arraycopy(survivors[i], 0, population[i], 0, 2 * NO_DIMENSIONS);
        }
    }
  }
}