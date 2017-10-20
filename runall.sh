#!/bin/sh

myval=$1
proccount=$2

evalfunc=(BentCigarFunction SchaffersEvaluation KatsuuraEvaluation)
count=$myval

javac -cp contest.jar player15*.java 
jar cmf MainClass.txt submission.jar player15*.class Pair.class PairD.class

for ef in ${evalfunc[*]}
do
    for PARENTSELECT_STYLE in 1 2
    do
        for REPRODUCE_STYLE in 1 2
        do
            for SURVIVAL_STYLE in 1 2
            do
                for q in 10
                do
                    for off_size in 50 100 250
                    do
                        for pop_size in 50 100 250
                        do
                            for std_dev_th in 0.2 0.6 1.0 1.4 1.8 2
                            do
                                for tournament_size in 10 20 40
                                do
                                    for prob_pick_best in 0.6 0.8 1.0
                                    do
                                        for i in `seq 24`
                                        do
                                            if (( (count % proccount) == 0)); then
                                                score=`java -DPARENTSELECT_STYLE="$PARENTSELECT_STYLE" -DREPRODUCE_STYLE="$REPRODUCE_STYLE" -DSURVIVAL_STYLE="$SURVIVAL_STYLE" -Dq="$q" -Dpop_size="$pop_size" -Doff_size="$off_size" -Dstd_dev_th="$std_dev_th" -Dtournament_size="$tournament_size" -Dprob_pick_best="$prob_pick_best" -jar testrun.jar -submission=player15 -evaluation=BentCigarFunction -seed=1 | grep Score | cut -d' ' -f 2`
                                                echo "$ef,$PARENTSELECT_STYLE,$REPRODUCE_STYLE,$SURVIVAL_STYLE,$pop_size,$std_dev_th,$tournament_size,$prob_pick_best,$off_size,$score" >> $ef-statistics.txt
                                            fi
                                            count=$((count+1))
                                        done
                                    done
                                done
                            done
                        done
                    done
                done
            done
        done
    done
done
