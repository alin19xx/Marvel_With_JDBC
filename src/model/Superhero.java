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
public class Superhero extends Fighter {

    private String superpower;

    public Superhero(String superpower, String name, int level, Place place, List<Gem> gems) {
        super(name, level, place, gems);
        this.superpower = superpower;
    }

    public Superhero(String name) {
        super(name);
    }

    public Superhero() {

    }

    public String getSuperpower() {
        return superpower;
    }

    public void setSuperpower(String superpower) {
        this.superpower = superpower;
    }

    @Override
    public String toString() {
        return super.getName();
    }

}
