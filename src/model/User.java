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
public class User {

    private String username;
    private String password;
    private int level;
    private Superhero superhero;
    private Place place;
    private int points;

    public User(String name, String password, int level, Superhero superhero, Place place, int points) {
        this.username = name;
        this.password = password;
        this.level = level;
        this.superhero = superhero;
        this.place = place;
        this.points = points;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public User(){
        
    }
    
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getLevel() {
        return level;
    }

    public Superhero getSuperhero() {
        return superhero;
    }

    public Place getPlace() {
        return place;
    }

    public int getPoints() {
        return points;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setSuperhero(Superhero superhero) {
        this.superhero = superhero;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return username + " " + level + " " + points ;
    }

   

}
