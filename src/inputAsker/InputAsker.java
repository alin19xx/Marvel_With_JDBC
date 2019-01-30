/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inputAsker;

import controller.Manager;
import exceptions.AppException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import persistance.MarvelDAO;

/**
 *
 * @author Alex
 */
public class InputAsker {

    Manager manager = new Manager();
    MarvelDAO marvelDAO = new MarvelDAO();
   

    public InputAsker() {
    }

    /**
     * The main function that collects the commands which user types
     */
    public void userAction() {
        String letter = "";
        do {
            try {
                BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
                String cadena = teclado.readLine().toLowerCase();
                String[] command = cadena.split(" ");
                if(command.length == 0){
                    throw new exceptions.AppException(AppException.ERROR_001_Wrong_Command);
                }
                letter = command[0].toUpperCase();
                switch (letter) {
                    case "R":
                        if (manager.checkLengthCommand(command)) {
                            manager.registerUser(command);
                        }
                        break;
                    case "V":
                        if (manager.checkLengthCommand(command)) {
                            manager.showSuperheros();
                        }
                        break;
                    case "L":
                        if (manager.checkLengthCommand(command)) {
                            if (manager.login(command)) {
                                manager.showAllInfoPlace();
                            }
                        }
                        break;
                    case "G":
                        if (manager.checkLengthCommand(command)) {
                            if (manager.checkLogedUser()) {
                                String gemName = command[1].concat(" ").concat(command[2]);
                                manager.getGem(gemName);
                            }
                        }
                        break;
                    case "D":
                        if (manager.checkLengthCommand(command)) {
                            if (manager.checkLogedUser()) {
                                manager.deleteUserLoged(command[1]);
                            }
                        }
                        break;
                    case "B":
                        if (manager.checkLengthCommand(command)) {
                            if (manager.checkLogedUser()) {
                                manager.battle(cadena);
                            }
                        }
                        break;
                    case "K":
                        if (manager.checkLengthCommand(command)) {
                            manager.showRanking();
                        }
                        break;
                    case "X":
                        System.out.println("See you soon");
                        manager.aLinuPresentFromWindowsUser();
                        break;
                    case "N":
                    case "S":
                    case "E":
                    case "W":
                        if (manager.checkLengthCommand(command)) {
                            if (manager.checkLogedUser()) {
                                System.out.println("Moving to... " + command[0].toUpperCase());
                                manager.move(command[0]);
                            }
                        }
                        break;
                    default:
                        throw new exceptions.AppException(AppException.ERROR_001_Wrong_Command);
                }
            } catch (AppException appException) {
                System.out.println(appException.getMessage());
            } catch (IOException ex) {
                System.out.println("Input/Output error ocurred");
            } catch (SQLException ex) {
                System.out.println("There's no connection from the database");
            }
        } while (!letter.equals("X"));
    }
}
