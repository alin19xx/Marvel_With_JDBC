/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marstucom;

import inputAsker.InputAsker;
import persistance.MarvelDAO;

/**
 *
 * @author Alex
 */
public class Marstucom {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MarvelDAO marvelDAO = new MarvelDAO();
        System.out.println("Connecting to database...");
        //Checking the connection
        if (marvelDAO.connect()) {
            InputAsker ia = new InputAsker();
            ia.userAction();
        }

    }

}
