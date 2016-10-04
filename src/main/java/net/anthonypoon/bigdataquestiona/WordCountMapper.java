package net.anthonypoon.bigdataquestiona;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapreduce.Mapper;

/**
 *
 * @author ypoon
 */
public class WordCountMapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // value is line streamed from file
        String line = value.toString();
        Pattern pattern = Pattern.compile("^\\d+,\\d+,[\\d\\.]+,\\d+$");
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
            String[] strArray = line.split(",");
            Text keyout = new Text();
            keyout.set(strArray[1]);
            context.write(keyout, new DoubleWritable(Double.valueOf(strArray[2])));
        } else {
            System.out.println("Skipping line: " + line);
        }
    }
    
}
