/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Set;

/**
 *
 * @author Alex
 */
public class Place {

    private String name;
    private String description;
    private String[] coordinates = new String[3];
    private Set<Fighter> charactersMarvel;

    private Place(String name, String description, String[] coordinates) {
        this.name = name;
        this.description = description;
        this.coordinates = coordinates;
    }

    public Place(String name) {
        this.name = name;
    }

    public Place(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Place() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String[] coordinates) {
        this.coordinates = coordinates;
    }

    public Set<Fighter> getCharactersMarvel() {
        return charactersMarvel;
    }

    public void setCharactersMarvel(Set<Fighter> charactersMarvel) {
        this.charactersMarvel = charactersMarvel;
    }

    @Override
    public String toString() {
        return "Place{" + "name=" + name + ", description=" + description + ", coordinates=" + coordinates + ", charactersMarvel=" + charactersMarvel + '}';
    }

}
