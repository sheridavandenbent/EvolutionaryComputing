myval=$1
proccount=$2

evalfunc=(BentCigarFunction SchaffersEvaluation KatsuuraEvaluation)
count=$myval

for am in ${alphamethod[*]}
do
  for alp in ${alpha[*]}
  do
    for cnt in `seq 1 30`
    do
    if (( (count % proccount) == 0)); then
      dateval=$(date +%Y%m%d%H%M%S)
      r=$RANDOM
      inputparameters=$am-$alp-$cnt-$dateval-$r
      echo $inputparameters
      echo "private static double BLX_ALPHA = $alp;" > player42.mid.java.mid
      echo "private static String ALPHA_METHOD =\"$am\";" >> player42.mid.java.mid
      cat player42.begin.java.begin > player42.java
      cat player42.mid.java.mid >> player42.java
      cat player42.end.java.end >> player42.java
      javac -cp contest.jar *.java
      jar cmf MainClass.txt submission.jar *.class
      for X in *.class; do
        if [ "$X" != "KatsuuraEvaluation.class" ] && [ "$X" != "SchaffersEvaluation.class" ] && [ "$X" != "BentCigarFunction.class" ]; then
          rm "$X"
        fi
      done
      for ef in ${evalfunc[*]}
      do
        score=`java -jar testrun.jar -submission=player42 -evaluation=$ef -seed=$r  2> "learnCurves/${ef}${inputparameters}.csv" | head -n1 | cut -d' ' -f2`
        echo "$am,$alp,$cnt,$r,$score" >> $ef-statistics.txt
      done
      python -c "import learn_curves; learn_curves.createLearnCurve(\"$inputparameters\")"
    fi
    count=$((count+1))
    done
  done
done
