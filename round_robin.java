/* SURVIVAL SELECTION (1)
Method to pick m survivors out of a population of size m + l via round-robin
population[m+l][NO_DIMENSIONS]
returns double[m][NO_DIMENSIONS] 

DEPRECATED BECAUSE OF OUR STD_DEV ARRAY
TOO BAD, IT WAS SUCH A COOL METHOD
*/
  // public double[][] round_robin(double[][] population, int m) {
  //   final int pop_size = population.length;

  //   /* Create an array containing <index, wins> pairs.
  //     This way we can keep track of the original index after sorting. */
  //   Pair results[] = new Pair[pop_size];

  //   /* For every individual, pick 10 random opponents and count the number of wins */
  //   for(int i = 0; i < pop_size; i++) {
  //     int wins = 0;
  //     int prev_opps[] = new int[q];

  //     for(int j = 0; j < q; j++){
  //       int opp;

  //       /* Avoid picking yourself or previous opponents */
  //       do {
  //          opp = (int) (pop_size * rnd_.nextDouble());
  //       } while(opp == i || contains(prev_opps, opp));

  //       /* FIGHT! */
  //       if(fitnesses[i] > fitnesses[opp])
  //         wins++;

  //       /* Add to fought opponents */
  //       prev_opps[j] = opp;
  //     }
  //     results[i] = new Pair(i, wins);
  //   }

  //   /* Sort results */
  //   Arrays.sort(results, Collections.reverseOrder());

  //   /* Select the m survivors and return them */
  //   double survivors[][] = new double[m][2 * NO_DIMENSIONS];
  //   double fit[] = new double[m];
  //   for(int i = 0; i < m; i++) {
  //     int index = results[i].index;
  //     System.arraycopy(population[index], 0, survivors[i], 0, 2 * NO_DIMENSIONS);
  //     fit[i] = fitnesses[index];
  //   }
  //   System.arraycopy(fit, 0, fitnesses, 0, m);
  //   return survivors;
  // }
