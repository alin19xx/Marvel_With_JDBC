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
public class Enemy extends Fighter {

    private String debility;

    public Enemy(String debility, String name, int level, Place place, List<Gem> gems) {
        super(name, level, place, gems);
        this.debility = debility;
    }

    public Enemy() {

    }

    public String getDebility() {
        return debility;
    }

    public void setDebility(String debility) {
        this.debility = debility;
    }

    @Override
    public String toString() {
        return "Name: " + super.getName() + " - Debility: " + debility + " - Level: " + super.getLevel();
    }

}
