/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import scala.Tuple2;

import com.google.common.collect.Iterables;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.sql.SparkSession;

import java.io.Serializable;

/**
 * Computes the PageRank of URLs from an input file. Input file should
 * be in format of:
 * URL         neighbor URL
 * URL         neighbor URL
 * URL         neighbor URL
 * ...
 * where URL and their neighbors are separated by space(s).
 *
 * This is an example implementation for learning how to use Spark. For more conventional use,
 * please refer to org.apache.spark.graphx.lib.PageRank
 *
 * Example Usage:
 * <pre>
 * bin/run-example JavaPageRank data/mllib/pagerank_data.txt 10
 * </pre>
 */
public final class JavaPageRank {
  private static final Pattern SPACES = Pattern.compile("\\s+");

  private static class MyString implements Serializable {
    //TODO to select a has, modify the selectedHash variable
    private static final HashType selectedHash = HashType.MD5;
    String s;

    public MyString(String s) {
      this.s = s;
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof MyString))
          return false;
        return s.equals(((MyString)other).s);
    }

    @Override
    public int hashCode() {
      // return HashCode.getHash(s.getBytes(), selectedHash);
      int hcode = HashCode.getHash(s.getBytes(), selectedHash);
      // count total record number
      RecordCount.total++;
      // get partition number
      int j = hcode % 5;
      // if number < 0, process
      if(j<0)
      {
          j+=5;
      }
      // count partition's record number
      RecordCount.partitionNo[j]++;
      // return hashcode
      return hcode;

//        return s.hashCode();
    }

    @Override
    public String toString() {
        return s;
    }
  }

  static void showWarning() {
    String warning = "WARN: This is a naive implementation of PageRank " +
            "and is given as an example! \n" +
            "Please use the PageRank implementation found in " +
            "org.apache.spark.graphx.lib.PageRank for more conventional use.";
    System.err.println(warning);
  }

// declare a new class for performance analysis
  public static class RecordCount
  {
    public static int[] partitionNo = {0,0,0,0,0}; // save number of records each partitioner receives
    public static int total = 0; // save total record number
  }

  private static class Sum implements Function2<Double, Double, Double> {
    @Override
    public Double call(Double a, Double b) {
      return a + b;
    }
  }

  public static void main(String[] args) throws Exception {
    if (args.length < 2) {
      System.err.println("Usage: JavaPageRank <file> <number_of_iterations>");
      System.exit(1);
    }

    showWarning();

    SparkSession spark = SparkSession
      .builder()
      .appName("JavaPageRank")
      .getOrCreate();

    // Loads in input file. It should be in format of:
    //     URL         neighbor URL
    //     URL         neighbor URL
    //     URL         neighbor URL
    //     ...
    JavaRDD<String> lines = spark.read().textFile(args[0]).javaRDD();

    // Loads all URLs from input file and initialize their neighbors.
    JavaPairRDD<MyString, Iterable<String>> links = lines.mapToPair(s -> {
      String[] parts = SPACES.split(s);
      return new Tuple2<>(new MyString(parts[0]), parts[1]);
    }).distinct().groupByKey().cache();

    // Loads all URLs with other URL(s) link to from input file and initialize ranks of them to one.
    JavaPairRDD<MyString, Double> ranks = links.mapValues(rs -> 1.0);

    // Calculates and updates URL ranks continuously using PageRank algorithm.
    for (int current = 0; current < Integer.parseInt(args[1]); current++) {
      // Calculates URL contributions to the rank of other URLs.
      JavaPairRDD<MyString, Double> contribs = links.join(ranks).values()
        .flatMapToPair(s -> {
          int urlCount = Iterables.size(s._1());
          List<Tuple2<MyString, Double>> results = new ArrayList<>();
          for (String n : s._1) {
            results.add(new Tuple2<>(new MyString(n), s._2() / urlCount));
          }
          return results.iterator();
        });

      // Re-calculates URL ranks based on neighbor contributions.
      ranks = contribs.reduceByKey(new Sum()).mapValues(sum -> 0.15 + sum * 0.85);
    }

    // Collects all URL ranks and dump them to console.
    List<Tuple2<MyString, Double>> output = ranks.collect();
    for (Tuple2<?,?> tuple : output) {
      System.out.println(tuple._1() + " has rank: " + tuple._2() + ".");
    }
    // print total record number
    System.out.println("Total Records: " + RecordCount.total);
    //print each partitioner's received records
    for(int i=0;i<5;i++)
    {
      System.out.println("Records in Partition " + i + ": " + RecordCount.partitionNo[i] + " Percentage: " + (double) RecordCount.partitionNo[i] / (double) RecordCount.total);
    }
    spark.stop();
  }
}
