#Running on Hadoop 2.7.4

#environment variables
export JAVA_HOME=/usr/java/default
export PATH=${JAVA_HOME}/bin:${PATH}
export HADOOP_CLASSPATH=${JAVA_HOME}/lib/tools.jar

#compile java code
hadoop com.sun.tools.javac.Main 
jar cf [JAR_NAME].jar [JAVA_CLASS_NAME]*.class

#execution
hadoop jar [JAR_NAME].jar [JAVA_CLASS_NAME] [INPUT] [OUTPUT]

Java class name could be: WordCount
Input could be: /user/joe/wordcount/input
Output could be: /user/joe/wordcount/output


