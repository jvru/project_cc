#!/bin/bash
javac Throughput.java ../hashes/*.java -d .
jar cf tp.jar *.class
