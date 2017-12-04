#!/bin/bash
hadoop com.sun.tools.javac.Main PictureCount.java -d . ../../hashes/*.java 
jar cf wc.jar *.class
hadoop fs -rm -r /user/johannes/output
hadoop jar wc.jar PictureCount /user/johannes/input /user/johannes/wcoutput
