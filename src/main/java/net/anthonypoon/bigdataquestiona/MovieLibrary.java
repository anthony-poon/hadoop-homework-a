/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anthonypoon.bigdataquestiona;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 *
 * @author ypoon
 */
public class MovieLibrary {
    private Map<Integer, Movie> map = new HashMap();
    public MovieLibrary(Path path, Configuration conf) throws IOException {
        FileSystem fs = FileSystem.get(conf);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fs.open(path)));
        String line;
        Pattern pattern = Pattern.compile("^(\\d+),\"{0,1}(.+)\"{0,1},.+$");
        while ((line = reader.readLine()) != null) {            
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                map.put(Integer.valueOf(matcher.group(1)), new Movie(Integer.valueOf(matcher.group(1)), matcher.group(2), 0.0));
            } else {
                System.out.println("Disregarding line: " + line);
            }
            
        }
    }
    
    public Movie getMovieById(Integer id){
        if (map.get(id) != null) {
            return map.get(id);
        } else {
            System.out.println("Movie not found: " + id);
            return null;
        }
        
    }    
}
