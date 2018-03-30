import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.chain.ChainMapper;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UnitMultiplication {

    public static class TransitionMapper extends Mapper<Object, Text, Text, Text> {

        @Override
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            //read input: relations.txt(transitionsmall.txt/transition.txt)
            //input format: fromPage\t toPage1, toPage2, toPage3
            //input example: 1\t2,8,27
            //output: build transition matrix unit -> fromPage\t toPage=probability
            //outputKey = fromId
            //outputValue = "toId = prob"

            String line = value.toString().trim();
            String[] fromTo = line.split("\t");

            if(fromTo.length == 1 || fromTo[1].trim().equals("")) {
                // It's better to log the dead end point scenario
                // logger.info("found dead end point : " fromId);
                return;
            }

            //1\t2,8,27
            String from = fromTo[0];
            String[] tos = fromTo[1].split(",");
            for (String to: tos) {
                // assume the probability is  1/n , n is the size of toList
                // generate the output From/To relations with probabilities
                context.write(new Text(from), new Text(to + "=" + (double)1/tos.length));
            }
        }
    }

    public static class PRMapper extends Mapper<Object, Text, Text, Text> {

        @Override
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            //read input: pr0.txt
            //input format: Page\t PageRank
            //input example: 1  0.25
            //output: write to reducer
            //outputKey = id
            //outputValue = pr0
            String[] pr = value.toString().trim().split("\t");
            context.write(new Text(pr[0]), new Text(pr[1]));
        }
    }

    public static class MultiplicationReducer extends Reducer<Text, Text, Text, Text> {


        @Override
        public void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {
            //input key = fromPage  value = <toPage=probability,..., pageRank>
            //key = fromId = 1
            //value = <2=1/3, 8=1/3, 27=1/3, 1>
            //pr0   pageRank  output from last iteration
            //Map<toId, prob>
            //iterate map -> pr0 * prob = outputValue(subPr) outputKey = toId
            List<String> transitionUnit = new ArrayList<String>();
            double prUnit = 0;
            for (Text value: values) {
                if(value.toString().contains("=")) {
                    transitionUnit.add(value.toString());
                }
                else {
                    prUnit = Double.parseDouble(value.toString());
                }
            }
            for (String unit: transitionUnit) {
                // Unit = "2=1/3"
                String outputKey = unit.split("=")[0];
                double relation = Double.parseDouble(unit.split("=")[1]);
                String outputValue = String.valueOf(relation * prUnit);
                // outputValue waiting to be calculate sum
                context.write(new Text(outputKey), new Text(outputValue));
            }
        }
    }

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        job.setJarByClass(UnitMultiplication.class);

        // different mappers with sequence relation
        // the output of mapper 1 is the input of mapper 2
        ChainMapper.addMapper(job, TransitionMapper.class, Object.class, Text.class, Text.class, Text.class, conf);
        ChainMapper.addMapper(job, PRMapper.class, Object.class, Text.class, Text.class, Text.class, conf);

        job.setReducerClass(MultiplicationReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // different mappers are parallel, their output without dependence relation
        MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class, TransitionMapper.class);
        MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class, PRMapper.class);

        FileOutputFormat.setOutputPath(job, new Path(args[2]));
        job.waitForCompletion(true);
    }

}
