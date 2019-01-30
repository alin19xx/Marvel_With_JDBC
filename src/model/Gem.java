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
public class Gem {

    private String name;
    private User user;
    private Fighter owner;
    private Place place;

    public Gem(String name, User user, Fighter owner, Place place) {
        this.name = name;
        this.user = user;
        this.owner = owner;
        this.place = place;
    }

    public Gem() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Fighter getOwner() {
        return owner;
    }

    public void setOwner(Fighter owner) {
        this.owner = owner;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    @Override
    public String toString() {
        return  name;
    }

}
