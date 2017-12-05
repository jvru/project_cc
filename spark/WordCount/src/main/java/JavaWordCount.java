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

import scala.Tuple2;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import java.io.Serializable;

public final class JavaWordCount {
  private static final Pattern SPACE = Pattern.compile(" ");

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
      //        return s.hashCode();-
    }

    @Override
    public String toString() {
        return s;
    }
  }
  // declare a new class for performance analysis
  public static class RecordCount
  {
    public static int[] partitionNo = {0,0,0,0,0}; // save number of records each partitioner receives
    public static int total = 0; // save total record number
  }

  public static void main(String[] args) throws Exception {

    if (args.length < 1) {
      System.err.println("Usage: JavaWordCount <file>");
      System.exit(1);
    }

    SparkSession spark = SparkSession
      .builder()
      .appName("JavaWordCount")
      .getOrCreate();

    JavaRDD<String> lines = spark.read().textFile(args[0]).javaRDD();

    JavaRDD<String> words = lines.flatMap(s -> Arrays.asList(SPACE.split(s)).iterator());

    //JavaPairRDD<String, Integer> ones = words.mapToPair(s -> new Tuple2<>(s, 1));
    JavaPairRDD<MyString, Integer> ones = words.mapToPair(s -> new Tuple2<>(new MyString(s), 1));

    JavaPairRDD<MyString, Integer> counts = ones.reduceByKey((i1, i2) -> i1 + i2);

    List<Tuple2<MyString, Integer>> output = counts.collect();
    for (Tuple2<?,?> tuple : output) {
      System.out.println(tuple._1() + ": " + tuple._2());
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
