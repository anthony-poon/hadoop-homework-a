package net.anthonypoon.bigdataquestiona;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 *
 * @author ypoon
 */
public class WordCountReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {
    private DoubleWritable result = new DoubleWritable();
    @Override
    protected void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
        BigDecimal sum = BigDecimal.ZERO;
        sum.setScale(2, RoundingMode.HALF_UP);
        int count = 0;
        for (DoubleWritable val: values) {
            count++;
            sum = sum.add(new BigDecimal(val.get()));
        }
        result.set(sum.divide(new BigDecimal(count), 2, RoundingMode.HALF_UP).doubleValue());
        context.write(key, result);
    }
    
}
