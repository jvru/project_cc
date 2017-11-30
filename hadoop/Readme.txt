#Tested on Hadoop 2.7.4

##################### Building and Executing the File #############################

#select hash function (in word count)
in WordCount/WordCount.java
modify the selectedHash variable to the corresponding value of the HashType enum
the possible values can be seen in the HashType.java file of the hashes folder

#compile java code
hadoop com.sun.tools.javac.Main [JAVA_CLASS_NAME].java
jar cf [JAR_NAME].jar [JAVA_CLASS_NAME]*.class

#execution
hadoop jar [JAR_NAME].jar [JAVA_CLASS_NAME] [INPUT] [OUTPUT]

Java class name could be: WordCount
Input could be: /user/joe/wordcount/input
Output could be: /user/joe/wordcount/output



##################### Additional Information #############################

#environment variables
export JAVA_HOME=/usr/java/default
export HADOOP_CLASSPATH=${JAVA_HOME}/lib/tools.jar
export HADOOP_HOME=[PATH_TO_HADOOP]/hadoop-2.7.4
export HADOOP_CONF_DIR=${HADOOP_HOME}/etc/hadoop

export PATH=${JAVA_HOME}/bin:${PATH}
export PATH=${PATH}:${HADOOP_HOME}/bin
