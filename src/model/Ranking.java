/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Alex
 */
public class Ranking {
    private String username;
    private String superhero;
    private int level;
    private int points;
    private int gems;

    public Ranking(String username, String superhero, int level, int points, int gems) {
        this.username = username;
        this.superhero = superhero;
        this.level = level;
        this.points = points;
        this.gems = gems;
    }

    public Ranking() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSuperhero() {
        return superhero;
    }

    public void setSuperhero(String superhero) {
        this.superhero = superhero;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getGems() {
        return gems;
    }

    public void setGems(int gems) {
        this.gems = gems;
    }

    @Override
    public String toString() {
        return username + "\t" + superhero + "\t" + level + "\t" + points + "\t" + gems;
    }
    
    
}
