/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;

/**
 *
 * @author Alex
 */
public class Fighter {
    private String name;
    private int level;
    private Place place;
    private List<Gem> gems;

    public Fighter(String name, int level, Place place, List<Gem> gems) {
        this.name = name;
        this.level = level;
        this.place = place;
        this.gems = gems;
    }

    public Fighter(String name) {
        this.name = name;
    }

    public Fighter() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public List<Gem> getGems() {
        return gems;
    }

    public void setGems(List<Gem> gems) {
        this.gems = gems;
    }

    @Override
    public String toString() {
        return "Fighter{" + "name=" + name + ", level=" + level + ", place=" + place + ", gems=" + gems + '}';
    }

   
    
    
}
