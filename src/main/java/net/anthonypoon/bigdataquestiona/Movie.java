/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anthonypoon.bigdataquestiona;

/**
 *
 * @author ypoon
 */
public class Movie {
    private int id;
    private double score;
    private String name;
    public Movie(int id, String name, double score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }
    
    public void setScore(double score) {
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public double getScore() {
        return score;
    }

    public String getName() {
        return name;
    }
    
    
}
