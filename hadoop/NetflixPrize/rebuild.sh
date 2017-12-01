#!/bin/bash
hadoop com.sun.tools.javac.Main Text.java MyMapper.java MyReducer.java netflix1Driver.java -d . ../../hashes/*.java 
jar cf wc.jar *.class
hadoop fs -rm -r /user/johannes/output
hadoop jar wc.jar netflix1Driver /user/johannes/input/nfp /user/johannes/output
