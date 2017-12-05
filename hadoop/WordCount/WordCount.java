import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
//import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount {

  public static class Text extends org.apache.hadoop.io.Text {
    //TODO to select a has, modify the selectedHash variable
    private static final HashType selectedHash = HashType.MD5;

    @Override
    public int hashCode() {
        return HashCode.getHash(toString().getBytes(), selectedHash);
    }
  }

    public static class TokenizerMapper extends Mapper<Object, org.apache.hadoop.io.Text, Text, IntWritable>{

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    public void map(Object key, org.apache.hadoop.io.Text value, Context context) throws IOException, InterruptedException {
      StringTokenizer itr = new StringTokenizer(value.toString());
      while (itr.hasMoreTokens()) {
        word.set(itr.nextToken());
        context.write(word, one);
      }
    }
  }

  public static class IntSumReducer extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();
    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      result.set(sum);
      context.write(key, result);
    }
  }

  // activate customized partitioner for performance analysis
  public static class CustomPartitioner extends Partitioner<Text, IntWritable> {
    public int getPartition(Text key, IntWritable value, int numReduceTasks) {
      // save hashcode for return
      int hcode = (key.hashCode() & Integer.MAX_VALUE) % numReduceTasks;
      // count the number of total records and the number of records each partitioner received
      RecordCount.total++;
      RecordCount.partitionNo[hcode]++;
      // return hash code
      return hcode;
    }
  }

  // declare a new class for performance analysis
  public static class RecordCount
  {
    public static int[] partitionNo = {0,0,0,0,0}; // save number of records each partitioner receives
    public static int total = 0; // save total record number
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();

    Job job = Job.getInstance(conf, "word count");
    job.setJarByClass(WordCount.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    // activate partitioner class
    job.setPartitionerClass(CustomPartitioner.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);

    job.setNumReduceTasks(5);

    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    long start = System.currentTimeMillis();
    job.waitForCompletion(true);
    long end = System.currentTimeMillis();

    // print total record number
    System.out.println("Total Records: " + RecordCount.total);
    //print each partitioner's received records
    for(int i=0;i<5;i++)
    {
      System.out.println("Records in Partition " + i + ": " + RecordCount.partitionNo[i] + " Percentage: "+ (double) RecordCount.partitionNo[i] / (double) RecordCount.total);
    }
    System.out.println("Job start: " + start);
    System.out.println("Job end: " + end);
    System.out.println("Duration: " + (end - start));
  }
}
