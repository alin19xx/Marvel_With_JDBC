/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import exceptions.AppException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Enemy;
import model.Gem;
import model.Place;
import model.Ranking;
import model.Superhero;
import model.User;
import persistance.MarvelDAO;

/**
 *
 * @author Alex
 */
public class Manager {

    MarvelDAO marvelDAO = new MarvelDAO();
    /**
     * Static objects to have more controll about their params
     */
    static User u = new User("", "");
    static Enemy e = new Enemy();

    /**
     * Function that registers a user checking if tha data typed by the username
     * is correct or not
     *
     * @param data Compossed by username, password and superhero
     * @throws SQLException Database exception
     * @throws AppException App exception
     */
    public void registerUser(String[] data) throws SQLException, AppException {
        marvelDAO.connect();
        try {
            if (!(marvelDAO.existUser(data[1]))) {
                if ((marvelDAO.existSuperhero(data[3]))) {
                    Superhero superhero = new Superhero(data[3]);
                    Place place = new Place("New York");
                    User user = new User(data[1], data[2], 1, superhero, place, 0);
                    marvelDAO.insertUser(user);
                    assignPlacesToGems(user);
                    System.out.println("User registered");
                } else {
                    throw new AppException(AppException.ERROR_004_Superhero_Name_No_Exists);
                }
            } else {
                throw new AppException(AppException.ERROR_003_User_Exists);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AppException appException) {
            System.out.println(appException.getMessage());
        }
    }

    /**
     * Function to set username and password to user's static object. It checks
     * the authentification from a database
     *
     * @param command Input type by the user
     * @return TRUE -> login successful // FALSE -> wrong authentification
     * @throws AppException
     * @throws SQLException
     */
    public boolean login(String[] command) throws AppException, SQLException {
        boolean statusLogin = false;
        try {
            marvelDAO.connect();
            if (marvelDAO.checkUsername_and_Pass(command[1], command[2])) {
                statusLogin = true;
                u.setUsername(command[1]);
                u.setPassword(command[2]);
                System.out.println("Welcome " + command[1] + " !!!\n ");
                return statusLogin;
            } else {
                throw new AppException(AppException.ERROR_005_Wrong_Username_Pass);
            }
        } catch (SQLException e) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, e);
        }
        return statusLogin;
    }

    /**
     * Function that shows all superheros from database. It contains a select
     * query from superhero's table
     *
     * @throws SQLException
     */
    public void showSuperheros() throws SQLException {
        Set<Superhero> superheros = new HashSet<>();
        try {
            marvelDAO.connect();
            superheros.addAll(marvelDAO.selectSuperheros());
            System.out.println("-SuperHeros-");
            superheros.forEach(System.out::println);
        } catch (SQLException e) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Function to delete the current loged user by password
     *
     * @param password The needed parameter to delete the user
     * @return TRUE -> success deletion // FALSE -> if deletion goes wrong
     * @throws exceptions.AppException
     * @throws java.sql.SQLException
     */
    public boolean deleteUserLoged(String password) throws AppException, SQLException {
        boolean deletion = false;
        try {
            marvelDAO.connect();
            if (password.equals(u.getPassword())) {
                if (marvelDAO.deleteUser(u.getUsername(), password)) {
                    deletion = true;
                    System.out.println("User deleted");
                    u.setUsername("");
                    u.setPassword("");
                    return deletion;
                } else {
                    throw new AppException(AppException.ERROR_010_Wrong_Pass_Deletion);
                }
            } else {
                throw new AppException(AppException.ERROR_005_Wrong_Username_Pass);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return deletion;
    }

    /**
     * Function that shows the name and description of the current user's place
     *
     * @param username Needed to select the right place. It's the loged username
     * @return It returns the place object
     * @throws java.sql.SQLException
     */
    public Place showCurrentPlaceUser(String username) throws SQLException {
        Place place = new Place();
        try {
            marvelDAO.connect();
            place = marvelDAO.selectShowPlaceUser(username);
            u.setPlace(place);
            System.out.println(place.getName() + "\n" + place.getDescription() + "\n---");
        } catch (SQLException ex) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return place;
    }

    /**
     * Function that shows all enemies of the user's current place
     *
     * @param username Needed to select the right place. It's the loged username
     * @throws SQLException
     */
    public void showCurrentEnemiesPlaceUser(String username) throws SQLException {
        try {
            marvelDAO.connect();
            Set<Enemy> enemies = new HashSet<>();
            enemies.addAll(marvelDAO.selectShowEnemiesPlaceUser(username));
            System.out.println("-Enemies-");
            if (enemies.isEmpty()) {
                System.out.println("There are no enemies in this place");
            } else {
                enemies.forEach(System.out::println);
                System.out.println("---");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Function that shows all gems which are in the user's current place
     *
     * @param username Needed to select the right place. It's the loged username
     * @return It returns a set of all the gems selected by a DAO function
     * @throws SQLException
     */
    public Set showCurrentGemsPlaceUser(String username) throws SQLException {
        Set<Gem> gems = null;
        try {
            marvelDAO.connect();
            gems = new HashSet<>();
            gems.addAll(marvelDAO.selectShowGemsPlaceUser(username));
            System.out.println("-Gems-");
            if (gems.isEmpty()) {
                System.out.println("There are no gems in this place");
            } else {
                gems.forEach(System.out::println);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return gems;
    }

    /**
     * Function that filters the gems which are in the user's current place and
     * checks it by a DAO function which give us those gems. If everything is
     * correct, it updates the owner of the specified gem
     *
     * @param gemName Needed to filter the gems
     * @return TRUE -> action to get is succeded // FALSE -> action goes wrong
     * @throws AppException
     * @throws SQLException
     */
    public boolean getGem(String gemName) throws AppException, SQLException {
        try {
            marvelDAO.connect();
            Set<Gem> gems = new HashSet<>();
            gems.addAll(marvelDAO.selectShowGemsPlaceUser(u.getUsername()));
            gems.stream()
                    .filter(a -> a.getName().equalsIgnoreCase(gemName))
                    .findFirst();
            if (!(gems.isEmpty())) {
                if (marvelDAO.updateGemOwner(gemName, u.getUsername(), u.getPlace().getName())) {
                    System.out.println("You have got the gem: " + gemName);
                } else {
                    throw new AppException(AppException.ERROR_013_General_Error);
                }
                return true;
            } else {
                throw new AppException(AppException.ERROR_008_Wrong_Gem_Name);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Function that prints the list extracted by a DAO query which gives the
     * current order of the ranking
     *
     * @throws SQLException
     */
    public void showRanking() throws SQLException {
        List<Ranking> ranking = new ArrayList<>();
        try {
            marvelDAO.connect();
            ranking.addAll(marvelDAO.selectRanking());
            System.out.println("-Ranking-");
            if (ranking.isEmpty()) {
                System.out.println("There are no users for the ranking (nobody has gems)");
            } else {
                ranking.forEach(System.out::println);
            }
        } catch (SQLException e) {
        }

    }

    /**
     * Function that prints the coordinates' letters if the current user's place
     * coordinates is null or not
     *
     * @param username
     * @throws java.sql.SQLException
     */
    public void showEnableCoordinates(String username) throws SQLException {
        Place place = new Place();
        try {
            marvelDAO.connect();
            place = marvelDAO.selectAllDataPlaceUser(username);
            String[] coordinates = place.getCoordinates();
            System.out.println("---");
            System.out.println("You can go");
            if (!(coordinates[0] == (null))) {
                System.out.print("N ");
            }
            if (!(coordinates[1] == (null))) {
                System.out.print("S ");
            }
            if (!(coordinates[2] == (null))) {
                System.out.print("E ");
            }
            if (!(coordinates[3] == (null))) {
                System.out.println("W ");

            }
        } catch (SQLException ex) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Function that checks if there is a user loged or not
     *
     * @return TRUE -> if user's properties are not empty // FALSE -> if there's
     * not a loged user
     * @throws AppException
     */
    public boolean checkLogedUser() throws AppException {
        boolean statusLogin = false;
        if (!((u.getUsername().equals("")) && (u.getPassword().equals("")))) {
            statusLogin = true;
            return statusLogin;
        } else {
            throw new AppException(AppException.ERROR_006_Not_Logged);
        }
    }

    /**
     * Function that checks if the user types nothing.
     *
     * @param command The command which is needed to check
     * @return TRUE -> if command's length = 0 // FALSE -> if command's length
     * != 0
     * @throws AppException
     */
    public boolean checkNullCommand(String[] command) throws AppException {
        boolean status = true;
        if (command.length == 0) {
            throw new AppException(AppException.ERROR_012_Not_Allowed_Null_Command);
        } else {
            status = false;
        }
        return status;
    }

    /**
     * Function that checks command by command the specified command length if
     * is correct or not
     *
     * @param command Needed to check the command's length
     * @return TRUE -> correct length // FALSE -> wrong length
     * @throws AppException
     */
    public boolean checkLengthCommand(String[] command) throws AppException {
        String letter = command[0].toUpperCase();
        switch (letter) {
            case "R":
                if (command.length == 4) {
                    return true;
                } else {
                    throw new exceptions.AppException(AppException.ERROR_002_Wrong_num_Args);
                }
            case "V":
                if (command.length == 1) {
                    return true;
                } else {
                    throw new exceptions.AppException(AppException.ERROR_002_Wrong_num_Args);
                }
            case "L":
                if (command.length == 3) {
                    return true;
                } else {
                    throw new exceptions.AppException(AppException.ERROR_002_Wrong_num_Args);
                }
            case "G":
                if (command.length == 3) {
                    return true;
                } else {
                    throw new exceptions.AppException(AppException.ERROR_002_Wrong_num_Args);
                }
            case "D":
                if (command.length == 2) {
                    return true;
                } else {
                    throw new exceptions.AppException(AppException.ERROR_002_Wrong_num_Args);
                }
            case "B":
                if (command.length == 3 || command.length == 2) {
                    return true;
                } else {
                    throw new exceptions.AppException(AppException.ERROR_002_Wrong_num_Args);
                }
            case "K":
                if (command.length == 1) {
                    return true;
                } else {
                    throw new exceptions.AppException(AppException.ERROR_002_Wrong_num_Args);
                }
            case "N":
                if (command.length == 1) {
                    return true;
                } else {
                    throw new exceptions.AppException(AppException.ERROR_002_Wrong_num_Args);
                }
            case "S":
                if (command.length == 1) {
                    return true;
                } else {
                    throw new exceptions.AppException(AppException.ERROR_002_Wrong_num_Args);
                }
            case "E":
                if (command.length == 1) {
                    return true;
                } else {
                    throw new exceptions.AppException(AppException.ERROR_002_Wrong_num_Args);
                }
            case "W":
                if (command.length == 1) {
                    return true;
                } else {
                    throw new exceptions.AppException(AppException.ERROR_002_Wrong_num_Args);
                }
            case " ":
                throw new exceptions.AppException(AppException.ERROR_002_Wrong_num_Args);
                

        }
        return false;
    }

    /**
     * Function that checks if the user types a null coordinate
     *
     * @param coordinate Letter typed by the user
     * @return TRUE -> if is not null // FALSE -> if coordinate is null
     * @throws AppException
     */
    public boolean checkNullCoordinates(String coordinate) throws AppException {
        if (coordinate == null) {
            throw new AppException(AppException.ERROR_007_Not_Allowed_Direction);
        } else {
            return true;
        }
    }

    /**
     * Function that allows the user move for all the game's map with specified
     * controls
     *
     * @param coordinate Letter typed by the user
     * @throws AppException
     * @throws java.sql.SQLException
     */
    public void move(String coordinate) throws AppException, SQLException {
        Place originPlace = marvelDAO.selectAllDataPlaceUser(u.getUsername());
        String[] coordinates = originPlace.getCoordinates();
        try {
            marvelDAO.connect();
            switch (coordinate.toUpperCase()) {
                case "N":
                    if ((checkNullCoordinates(coordinates[0]))) {
                        marvelDAO.updatePlace(u.getUsername(), coordinates[0]);
                        showAllInfoPlace();
                    }
                    break;
                case "S":
                    if ((checkNullCoordinates(coordinates[1]))) {
                        marvelDAO.updatePlace(u.getUsername(), coordinates[1]);
                        showAllInfoPlace();
                    }
                    break;
                case "E":
                    if ((checkNullCoordinates(coordinates[2]))) {
                        marvelDAO.updatePlace(u.getUsername(), coordinates[2]);
                        showAllInfoPlace();
                    }
                    break;
                case "W":
                    if ((checkNullCoordinates(coordinates[3]))) {
                        marvelDAO.updatePlace(u.getUsername(), coordinates[3]);
                        showAllInfoPlace();
                    }
                    break;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Function to assign random places for the different game gems
     *
     * @param user Needed to control the multi-player game
     * @throws SQLException
     * @throws AppException
     */
    public void assignPlacesToGems(User user) throws SQLException, AppException {
        List<Gem> gems = new ArrayList<>();
        List<Place> places = new ArrayList<>();
        List<Enemy> enemies = new ArrayList<>();
        //*** NECESARIO**//
        Place place;
        Gem gem = new Gem();
        Enemy enemy;
        int randomEnemy;
        try {
            marvelDAO.connect();
            places.addAll(marvelDAO.selectRandomPlaces());
            gems.addAll(marvelDAO.selectAllGems());
            enemies.addAll(marvelDAO.selectShowEnemiesPlaceUser(u.getUsername()));
            for (int i = 0; i < gems.size(); i++) {
                place = places.get(i);
                enemies = marvelDAO.selectEnemiesByPlace(place);
                randomEnemy = (int) (Math.random() * enemies.size() - 1);
                gem.setName(gems.get(i).getName());
                gem.setUser(user);
                gem.setPlace(place);
                if (enemies.isEmpty()) {
                    gem.setOwner(null);
                } else {
                    enemy = enemies.get(randomEnemy);
                    gem.setOwner(enemy);

                }
                marvelDAO.insertNewGem(gem);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Function that contains all battle rules. The values of each list are the
     * options which key wins to value
     *
     * @return The map to initilize in battle function
     */
    public Map battleRules() {
        String[] options = {"paper", "scissors", "rock", "lizard", "spock"};
        List<String> paperList = Arrays.asList("rock", "spock");
        List<String> scissorsList = Arrays.asList("paper", "lizard");
        List<String> rockList = Arrays.asList("lizard", "scissors");
        List<String> lizardList = Arrays.asList("spock", "paper");
        List<String> spockList = Arrays.asList("scissors", "rock");
        Map<String, List<String>> rules = new HashMap<>();
        rules.put(options[0], paperList);
        rules.put(options[1], scissorsList);
        rules.put(options[2], rockList);
        rules.put(options[3], lizardList);
        rules.put(options[4], spockList);
        return rules;
    }

    /**
     * Function that sets all current user's properties to use it in battle's
     * funciton
     *
     * @return The current user loged in
     */
    public User prepareUserForBattle() {
        Superhero superhero = new Superhero();
        try {
            marvelDAO.connect();
            u.setLevel(marvelDAO.selectUser(u.getUsername()).getLevel());
            u.setPoints(marvelDAO.selectUser(u.getUsername()).getPoints());
            superhero.setName(marvelDAO.selectUser(u.getUsername()).getSuperhero().getName());
            superhero.setSuperpower(marvelDAO.selectUser(u.getUsername()).getSuperhero().getSuperpower());
            u.setSuperhero(superhero);
        } catch (SQLException ex) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return u;
    }

    /**
     * Function that sets all enemy's properties that typed the loged user
     *
     * @param command The command introduced by the user
     * @return TRUE -> enemy name typed by user is right // FALSE -> if enemy
     * name doesn't exist in that place
     * @throws SQLException
     */
    public boolean prepareEnemyForBattle(String command) throws SQLException {
        String[] enemyName = command.split(" ");
        String name;
        name = enemyName[1];
        try {
            marvelDAO.connect();
            if (marvelDAO.checkEnemiesByPlaceByName(u, name)) {
                e.setName(marvelDAO.selectEnemyForBattle(name, u.getUsername()).getName());
                e.setDebility(marvelDAO.selectEnemyForBattle(name, u.getUsername()).getDebility());
                e.setLevel(marvelDAO.selectEnemyForBattle(name, u.getUsername()).getLevel());
                e.setPlace(marvelDAO.selectEnemyForBattle(name, u.getUsername()).getPlace());
                return true;
            } else {
                throw new exceptions.AppException(AppException.ERROR_009_No_Enemy_Name_Place);
            }

        } catch (SQLException | AppException ex) {
        }
        return false;
    }

    /**
     * Function that checks if the superpower of the user's superhero is equal
     * to enemy's debility
     *
     * @return TRUE -> superpower = debility // FALSE -> superpower != debility
     */
    public boolean checkDebilitiesVSSuperpower() {
        if (u.getSuperhero().getSuperpower().equalsIgnoreCase(e.getDebility())) {
            System.out.println("You have an extra attack.");
            return true;
        } else {
            System.out.println("You haven't an extra attack");
        }
        return false;
    }

    /**
     * Function that return one of the options which are in option array by
     * random number
     *
     * @return The name of the option which is in the random position of the
     * array
     */
    public String getResultTurn() {
        String[] options = {"paper", "scissors", "rock", "lizard", "spock"};
        String result = "";
        Map<String, List<String>> rules = new HashMap<>();
        rules.putAll(battleRules());
        int rand = (int) (Math.random() * options.length - 1);
        result = options[rand];
        return result;
    }

    /**
     * Function that gets both results and compares the key. After this, it
     * iterates the list values of the key (userTurn)
     *
     * @param rules Needed to get the battle status of who wins
     * @return 1 -> User wins // -1 -> Enemy wins // 0 -> tie, nobody wins
     */
    public int getResultBattle(Map<String, List<String>> rules) {
        int result = 0;
        String userTurn = getResultTurn();
        String enemyTurn = getResultTurn();
        for (Map.Entry<String, List<String>> entry : rules.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();
            if (userTurn.equals(key)) {
                System.out.println(u.getUsername() + " ATTACK: " + userTurn);
                System.out.println(e.getName() + " ATTACK: " + enemyTurn);
                if (values.contains(enemyTurn)) {
                    System.out.println(u.getUsername() + " WINS");
                    result = 1;
                } else if (userTurn.equalsIgnoreCase(enemyTurn)) {
                    System.out.println("TIE! NO ONE WINS");
                    result = 0;
                } else {
                    System.out.println(e.getName() + " WINS");
                    result = -1;
                }
            }
        }
        return result;
    }

    /**
     * The battle function recopiles all the data given by other function and
     * interprets the result
     *
     * @param command Command typed by the user
     * @throws AppException
     */
    public void battle(String command) throws AppException {
        int atkU;
        int atkE = e.getLevel();
        int winU = 0;
        int winE = 0;
        int numTurns = 0;
        try {
            marvelDAO.connect();
            prepareUserForBattle();
            if (prepareEnemyForBattle(command)) {
                Map<String, List<String>> rules = battleRules();

                System.out.println("-FIGHT BEGINS-");
                //atkU = (checkDebilitiesVSSuperpower()) ? u.getLevel() + 1: u.getLevel();
                if (checkDebilitiesVSSuperpower()) {
                    atkU = u.getLevel() + 1;
                } else {
                    atkU = u.getLevel();
                }
                numTurns = (atkU < atkE) ? atkU : atkE;
                for (int i = 0; i <= numTurns; i++) {
                    System.out.println("You have " + atkU + " attaks\n" + e.getName() + " has " + atkE + " attacks");
                    atkU--;
                    atkE--;
                    if (getResultBattle(rules) == 1) {
                        winU++;
                    } else if (getResultBattle(rules) == -1) {
                        winE++;
                    }
                }
                System.out.println("-Fight Finished!-");
                System.out.println(u.getUsername() + " " + winU + " wins. " + e.getName() + " " + winE + " wins");
                if (winU > winE) {
                    int points = 5;
                    System.out.println(u.getUsername() + " wins the battle");
                    System.out.println("The enemy has lost their gems");
                    //Mostrar puntos
                    marvelDAO.updatePoints(u.getUsername(), points);
                    if (!(showCurrentGemsPlaceUser(u.getUsername()).isEmpty())) {
                        for (int i = 0; i < showCurrentGemsPlaceUser(u.getUsername()).size(); i++) {
                            marvelDAO.updateGemOwnerNull(u.getUsername(), u.getPlace().getName());
                        }
                    }
                    marvelDAO.updatePlaceEnemy(e.getName());
                    System.out.println(e.getName() + " has disappeared");
                } else if (winU < winE) {
                    int points = -2;
                    System.out.println(e.getName() + " wins the battle");
                    System.out.println("The enemy has stolen your gems");
                    marvelDAO.updatePoints(u.getUsername(), points);
                    marvelDAO.updatePlaceEnemy(e.getName());
                    marvelDAO.updatePlaceGemEnemy(e.getPlace().getName(), u.getUsername());
                    System.out.println(e.getName() + " has disappeared");
                } else {
                    System.out.println("No one has won");
                    marvelDAO.updatePlaceEnemy(e.getName());
                    System.out.println(e.getName() + " has disappeared");
                }
            } else {
                System.out.println("Here there is no enemy with that name");
                throw new exceptions.AppException(AppException.ERROR_009_No_Enemy_Name_Place);
            }

        } catch (SQLException | AppException ex) {
        }
    }

    /**
     * Function that shows all the user's place data by joining all place
     * funtions
     *
     * @throws SQLException
     */
    public void showAllInfoPlace() throws SQLException {
        try {
            String username = u.getUsername();
            showCurrentPlaceUser(username);
            showCurrentEnemiesPlaceUser(username);
            showCurrentGemsPlaceUser(username);
            showEnableCoordinates(username);
        } catch (SQLException ex) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void aLinuPresentFromWindowsUser() {
        System.out.println(" a8888b.\n"
                + "             d888888b.\n"
                + "             8P\"YP\"Y88\n"
                + "             8|o||o|88\n"
                + "             8'    .88\n"
                + "             8`._.' Y8.\n"
                + "            d/      `8b.\n"
                + "          .dP   .     Y8b.\n"
                + "         d8:'   \"   `::88b.\n"
                + "        d8\"           `Y88b\n"
                + "       :8P     '       :888\n"
                + "        8a.    :      _a88P\n"
                + "      ._/\"Yaa_ :    .| 88P|\n"
                + " jgs  \\    YP\"      `| 8P  `.\n"
                + " a:f  /     \\._____.d|    .'\n"
                + "      `--..__)888888P`._.'");
    }
}
