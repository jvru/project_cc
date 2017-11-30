#!/bin/bash
hadoop com.sun.tools.javac.Main WordCount.java -d . ../../hashes/*.java 
jar cf wc.jar *.class
hadoop fs -rm -r /user/johannes/output
hadoop jar wc.jar WordCount /user/johannes/input /user/johannes/wcoutput
