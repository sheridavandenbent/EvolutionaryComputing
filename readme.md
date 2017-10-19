To run:

javac -cp contest.jar player15.java 
jar cmf MainClass.txt submission.jar player15.class Pair.class PairD.class

java -Dvar1=1 -jar testrun.jar -submission=player15 -evaluation=BentCigarFunction -seed=1
java -jar testrun.jar -submission=player15 -evaluation=KatsuuraEvaluation -seed=1
java -jar testrun.jar -submission=player15 -evaluation=SchaffersEvaluation -seed=1
