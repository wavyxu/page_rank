# PageRank
#
#
# Implement the PageRank algorithm with Mapper-Reducer
#
#
# How to run:
# hdfs dfs -rm -r /transition # remove /transition dir in HDFS，if it not exists，ignore the error info.

# hdfs dfs -mkdir /transition # create /transition dir in HDFS

# hdfs dfs -put transition.txt /transition # copy the transisiton.txt to hdfs /transition

# hdfs dfs -rm -r /output* # remove /output dir，if it not exists，ignore the error info.

# hdfs dfs -rm -r /pagerank* # remove /pagerank dir

# hdfs dfs -mkdir /pagerank0 # create /pagerank0 dir

# hdfs dfs -put prsmall.txt /pagerank0 #upload PR0

# hadoop com.sun.tools.javac.Main *.java # compile java source code

# jar cf pr.jar *.class # jar all the compiled class files

# hadoop jar pr.jar Driver /transition /pagerank /output 1 #run jar
# //args0: dir of transition.txt
# //args1: dir of PageRank.txt
# //args2: dir of unitMultiplication result
# //args3: times of convergence（make sure the code run successfully when args3=1, then test args3=40）

