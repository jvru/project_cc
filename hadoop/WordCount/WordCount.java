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

//import org.apache.hadoop.mapreduce.Counter;
//import org.apache.hadoop.mapreduce.Counters;
//import org.apache.hadoop.mapreduce.CounterGroup;

public class WordCount {

  public static class Text extends org.apache.hadoop.io.Text {
    private static Simhash shInstance = new Simhash(new BinaryWordSeg());

    @Override
    public int hashCode() {
//        return standard();
//        return murmur();
//        return md5();
//        return sha256();
//        return whirlpool();
//        return crc32();
//        return xor();
        return simHash();
    }

    private int simHash() {
        return (int) shInstance.simhash32(toString());
    }

    private int xor() {
        return Xor.xor(toString().getBytes());
    }

    private int crc32() {
        return Crc.crc32(toString().getBytes());
    }

    private int standard() {
        return super.hashCode();
    }

    private int whirlpool() {
        byte[] bytes = toString().getBytes();
        bytes = Whirlpool.whirlpool(bytes, 0, bytes.length);
        return first32Bit(bytes);  
    }

    private int murmur() {
        return MurmurHash.hash32(toString());
    }

    private int sha256() {
        String bytes = Sha.sha256(toString());
        return first32Bit(bytes.getBytes());
    }

    private int md5() {
        byte[] bytes = MD5.computeMD5(toString().getBytes());
        return first32Bit(bytes);
    }

    private int first32Bit(byte[] bytes) {
        int result = 0;
        result = bytes[0];
        result <<= 4;
        result += bytes[1];
        result <<= 4;
        result += bytes[2];
        result <<= 4;
        result += bytes[3];
        return result;
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

//  public static class CustomPartitioner extends Partitioner<Text, IntWritable> {
//    public int getPartition(Text key, IntWritable value, int numReduceTasks) {
//      int hCode = 0;
//
//      if(numReduceTasks==0)
//          return 0;
//      return hCode % numReduceTasks;
//    }
//  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "word count");
    job.setJarByClass(WordCount.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
//    job.setPartitionerClass(CustomPartitioner.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);

    job.setNumReduceTasks(5);

    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    long start = System.currentTimeMillis();
    job.waitForCompletion(true);
    long end = System.currentTimeMillis();

    System.out.println("Job start: " + start);
    System.out.println("Job end: " + end);
    System.out.println("Duration: " + (end - start));

/*    Counters counters = job.getCounters();

    for (CounterGroup group : counters) {
      System.out.println("* Counter Group: " + group.getDisplayName() + " (" + group.getName() + ")");
      System.out.println("  number of counters in this group: " + group.size());
      for (Counter counter : group) {
        System.out.println("  - " + counter.getDisplayName() + ": " + counter.getName() + ": "+counter.getValue());
      }
    }*/
  }
}
