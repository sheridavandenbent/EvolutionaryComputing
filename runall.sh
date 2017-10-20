#!/bin/sh

javac -cp contest.jar player15.java 
jar cmf MainClass.txt submission.jar player15.class Pair.class PairD.class

for PARENTSELECT_STYLE in 1 2
do
    for REPRODUCE_STYLE in 1 2
    do
        for SURVIVAL_STYLE in 1 2
        do
            for q in 5 10 20 50 100 200
            do
                for pop_size in 10 50 100 250 500 1000
                do
                    for std_dev in 0.2 0.4 0.6 0.8 1.0 1.2 1.4 1.6 1.8 2
                    do
                        for tournament_size in 5 10 20 40 80
                        do
                            for prob_pick_best in 0.1 0.2 0.3 0.4 0.5 0.6 0.7 0.8 0.9
                            do
                                java -DPARENTSELECT_STYLE="$PARENTSELECT_STYLE" -DREPRODUCE_STYLE="$REPRODUCE_STYLE" -DSURVIVAL_STYLE="$SURVIVAL_STYLE" -Dq="$q" -Dpop_size="$pop_size" -Dstd_dev_th="$std_dev_th" -Dtournament_size="$tournament_size" -Dprob_pick_best="$prob_pick_best" -jar testrun.jar -submission=player15 -evaluation=BentCigarFunction -seed=1
                            done
                        done
                    done
                done
            done
        done
    done
done