/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anthonypoon.bigdataquestiona;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileAlreadyExistsException;


import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 *
 * @author ypoon
 */
public class Main {
    private static String outputStr = "";
    public static void main(String[] args) {
        boolean isOutputSet = false;
        try { 
            Configuration conf = new Configuration();
            FileSystem dfs = FileSystem.get(conf);
            outputStr = args[1];
            do {
                Path outputPath = new Path(outputStr);
                if (dfs.exists(outputPath)) {
                    Pattern pattern = Pattern.compile("^(.+)_(\\d+)$");
                    Matcher matcher = pattern.matcher(outputStr);
                    if (matcher.matches()) {
                        String numberStr = matcher.group(2);
                        Integer number = Integer.valueOf(numberStr);
                        number += 1;
                        outputStr = matcher.group(1) + "_" + number.toString();
                    } else {
                        outputStr = outputStr+"_0";
                    }
                } else {
                    isOutputSet = true;
                }
            } while (!isOutputSet);
            
            Job job = Job.getInstance(conf);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(DoubleWritable.class);
            job.setMapperClass(WordCountMapper.class);
            job.setReducerClass(WordCountReducer.class);
            FileInputFormat.setInputPaths(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(outputStr));
            if (job.waitForCompletion(true)) {
                // Hard coded file name for now. use MultipleOutputFormat to change file name later, or scan for valid file
                Path resultPath = new Path(outputStr+"/part-r-00000");
                Sorter sorter = new Sorter(resultPath, conf);
                PrintWriter topHundredwriter;
                PrintWriter allWriter;
                if(args.length > 2 && args[2] != null){
                    topHundredwriter = new PrintWriter(args[2] + "/A.txt", "UTF-8");
                    allWriter = new PrintWriter(args[2] + "/all.txt", "UTF-8");
                } else {                    
                    topHundredwriter = new PrintWriter("A.txt", "UTF-8");
                    allWriter = new PrintWriter("all.txt", "UTF-8");
                }                
                for (Movie movie : sorter.getTopList()) {
                    allWriter.println("\"" + movie.getName() + "\" " + movie.getScore());                    
                }
                allWriter.close();
                for (Movie movie : sorter.getTopList(100)) {
                    topHundredwriter.println("\"" + movie.getName() + "\" " + movie.getScore());                    
                }
                topHundredwriter.close();
                System.out.println("Task finished");
                System.exit(0);
            } else {
                System.exit(1);
            }
            //System.exit(job.waitForCompletion(true) ? 0 : 1);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
