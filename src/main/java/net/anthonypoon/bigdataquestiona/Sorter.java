/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anthonypoon.bigdataquestiona;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 *
 * @author ypoon
 */
public class Sorter {
    private BufferedReader reader;
    private MovieLibrary library;
    private List<Movie> topList = new ArrayList();
    public Sorter(Path resultPath, Configuration conf) throws IOException {
        FileSystem fs = FileSystem.get(conf);
        reader = new BufferedReader(new InputStreamReader(fs.open(resultPath)));
        library = new MovieLibrary(new Path("movies_input.csv"), conf);
        String line;
        while ((line = reader.readLine()) != null) {
            Pattern pattern = Pattern.compile("^(\\d+)[\\s\\t]+([\\d\\.]+)$");
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                Movie movie = library.getMovieById(Integer.valueOf(matcher.group(1)));
                if (movie != null) {
                    movie.setScore(Double.valueOf(matcher.group(2)));
                    topList.add(movie);
                }
            } else {
                System.out.println("Disregarding: " + line);
            }
        }
        Comparator comparator = new Comparator<Movie>() {
            @Override
            public int compare(Movie o1, Movie o2) {
                if (Math.abs(o1.getScore() - o2.getScore()) < 0.001) {
                    return 0;
                }
                if (o1.getScore() > o2.getScore()){
                    return -1;
                } else {
                    return 1;
                }
                
                
            }
        };
        Collections.sort(topList, comparator);
    }
    
    public List<Movie> getTopList() throws IOException {        
        return topList;
    }
    
    public List<Movie> getTopList(Integer count) throws IOException {
        return topList.subList(0, 100);
    }
}
