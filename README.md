# PageRank

 Implement the PageRank algorithm with Mapper-Reducer</br></br>

# How to run:
 hdfs dfs -rm -r /transition # remove /transition dir in HDFS，if it not exists，ignore the error info.</br>

 hdfs dfs -mkdir /transition # create /transition dir in HDFS</br>

 hdfs dfs -put transition.txt /transition # copy the transisiton.txt to hdfs /transition</br>

 hdfs dfs -rm -r /output* # remove /output dir，if it not exists，ignore the error info.</br>

 hdfs dfs -rm -r /pagerank* # remove /pagerank dir</br>

 hdfs dfs -mkdir /pagerank0 # create /pagerank0 dir</br>

 hdfs dfs -put prsmall.txt /pagerank0 #upload PR0</br>

 hadoop com.sun.tools.javac.Main *.java # compile java source code</br>

 jar cf pr.jar *.class # jar all the compiled class files</br>

 hadoop jar pr.jar Driver /transition /pagerank /output 1 #run jar</br>
 //args0: dir of transition.txt</br>
 //args1: dir of PageRank.txt</br>
 //args2: dir of unitMultiplication result</br>
 //args3: times of convergence（make sure the code run successfully when args3=1, then test args3=40）</br>

