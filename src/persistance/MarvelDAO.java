/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistance;

import exceptions.AppException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Enemy;
import model.Gem;
import model.Place;
import model.Ranking;
import model.Superhero;
import model.User;

/**
 *
 * @author Alex
 */
public class MarvelDAO {

    public MarvelDAO() {
    }

    Connection connection;

    /**
     * Query that checks if the superhero typed by the user exists or not
     *
     * @param superheroName Name of the superhero typed by the user
     * @return TRUE -> if exists // FALSE -> if not exists
     */
    public boolean existSuperhero(String superheroName) {
        try {
            Statement st = connection.createStatement();
            String query = "select * from superhero where name = '" + superheroName + "'";
            ResultSet rs = st.executeQuery(query);
            boolean exist = rs.next();
            rs.close();
            st.close();
            return exist;
        } catch (SQLException ex) {
            Logger.getLogger(MarvelDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Query that checks if the username typed by the user exists or not
     *
     * @param username Username typed by the user
     * @return TRUE -> if username not exists // FALSE -> if username exists
     */
    public boolean existUser(String username) {
        try {
            Statement st = connection.createStatement();
            String query = "select username from marvel.user where username = '" + username + "'";
            ResultSet rs = st.executeQuery(query);
            boolean exist = rs.next();
            rs.close();
            st.close();
            return exist;
        } catch (SQLException ex) {
            Logger.getLogger(MarvelDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Query that inserts a new user on database. It controls if a username
     * exists
     *
     * @param user User object as a parameter
     * @throws SQLException Database exception
     * @throws AppException App exception
     */
    public void insertUser(User user) throws SQLException, AppException {
        if (existUser(user.getUsername())) {
            throw new exceptions.AppException(AppException.ERROR_003_User_Exists);
        }
        try {
            PreparedStatement ps = connection.prepareStatement("insert into user values (?, ?, ?, ?, ?, ?)");
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setInt(3, user.getLevel());
            ps.setObject(4, user.getSuperhero().getName());
            ps.setObject(5, user.getPlace().getName());
            ps.setObject(6, user.getPoints());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            Logger.getLogger(MarvelDAO.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    /**
     * Query that selects all superheros' names from database
     * @return A set of superheros' names (string)
     * @throws SQLException 
     */
    public Set selectSuperheros() throws SQLException {
        Set<Superhero> superheros = new HashSet<>();
        try {
            Statement st = connection.createStatement();
            String select = "select name from superhero";
            ResultSet rs = st.executeQuery(select);
            while (rs.next()) {
                Superhero superhero = new Superhero();
                superhero.setName(rs.getString(1));
                superheros.add(superhero);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
        }
        return superheros;
    }

    /**
     * Query that checks if username and password are correct in database
     *
     * @param username Username typed by the user
     * @param pass Password typed by the user
     * @return TRUE -> if username and pass are correct // FALSE -> if one or
     * both inputs doesn't match on database
     * @throws java.sql.SQLException
     */
    public boolean checkUsername_and_Pass(String username, String pass) throws SQLException {
        try {
            Statement st = connection.createStatement();
            String query = "select username, pass from user where username = '" + username + "' and pass = '" + pass + "'";
            ResultSet rs = st.executeQuery(query);
            boolean exist = rs.next();
            rs.close();
            st.close();
            return exist;
        } catch (SQLException ex) {
            Logger.getLogger(MarvelDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Function that delets the current user who's logged in
     *
     * @param username Current user logged
     * @param password Current user's password
     * @return TRUE -> if succed deletion // FALSE -> if deletion goes wrong
     * @throws java.sql.SQLException
     */
    public boolean deleteUser(String username, String password) throws SQLException {
        try {
            PreparedStatement ps = connection.prepareStatement("delete from user where username = '" + username
                    + "' and pass = '" + password + "'");
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(MarvelDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Query that selects the name and description of the user's current place
     *
     * @param username Current username logged in
     * @return The selected place
     * @throws java.sql.SQLException
     */
    public Place selectShowPlaceUser(String username) throws SQLException {
        Place place = new Place();
        try {
            Statement st = connection.createStatement();
            String query = "select place.name, place.description from place inner join user on place.name = user.place where user.username = '" + username + "'";
            ResultSet rs = st.executeQuery(query);
            rs.next();
            place.setName(rs.getString(1));
            place.setDescription(rs.getString(2));
            rs.close();
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(MarvelDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return place;
    }

    /**
     * Query that selects the enemies who are in the user's current place
     *
     * @param username Current username logged in
     * @return A set of enemies
     * @throws java.sql.SQLException
     */
    public List selectShowEnemiesPlaceUser(String username) throws SQLException {
        List<Enemy> enemies = new ArrayList<>();
        try {
            Statement st = connection.createStatement();
            String query = "select enemy.* from place\n"
                    + "inner join enemy\n"
                    + "on place.name = enemy.place\n"
                    + "inner join user\n"
                    + "on place.name = user.place\n"
                    + "where user.username ='" + username + "'";
            ResultSet rs = st.executeQuery(query);
            //rs.next();
            while (rs.next()) {
                Enemy e = new Enemy();
                e.setName(rs.getString(1));
                e.setDebility(rs.getString(2));
                e.setLevel(rs.getInt(3));
                enemies.add(e);
            }
            rs.close();
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(MarvelDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return enemies;
    }

    /**
     * Query that selects all enemies which are in specified place
     * @param place Needed to get the enemies of that place
     * @return A list of enemies (Enemy)
     * @throws SQLException 
     */
    public List selectEnemiesByPlace(Place place) throws SQLException {
        List<Enemy> enemies = new ArrayList<>();
        try {
            Statement st = connection.createStatement();
            String query = "select enemy.name from place\n"
                    + "inner join enemy\n"
                    + "on place.name = enemy.place\n"
                    + "where enemy.place ='" + place.getName() + "'";
            ResultSet rs = st.executeQuery(query);
            //rs.next();
            while (rs.next()) {
                Enemy e = new Enemy();
                e.setName(rs.getString(1));
                enemies.add(e);
            }
            rs.close();
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(MarvelDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return enemies;
    }

    /**
     * Query that checks if in a specified place there's a enemy with that name
     * @param user Needed to get the current user's place
     * @param enemyName Enemy name typed by the user
     * @return TRUE -> if exists the name // FALSE -> if name doesn't exists
     * @throws SQLException 
     */
    public boolean checkEnemiesByPlaceByName(User user, String enemyName) throws SQLException {
        try {
            Statement st = connection.createStatement();
            String query = "select enemy.name from place\n"
                    + "inner join enemy\n"
                    + "on place.name = enemy.place\n"
                    + "where enemy.place ='" + user.getPlace().getName() + "' and enemy.name = '" + enemyName + "'";
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                st.executeQuery(query);
                rs.close();
                st.close();
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MarvelDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Query that extracts all enemy data needed to battle function
     * @param enemyName The enemy name typed by the user
     * @param username The user that wants to fight and is loged in
     * @return The enemy with all the data from the database
     * @throws SQLException 
     */
    public Enemy selectEnemyForBattle(String enemyName, String username) throws SQLException {
        Enemy e = new Enemy();
        Place p = new Place();
        try {
            Statement st = connection.createStatement();
            String query = "select enemy.* from enemy\n"
                    + "inner join user\n"
                    + "on enemy.place = user.place\n"
                    + "where enemy.name = '" + enemyName + "' and user.username = '" + username + "'";
            ResultSet rs = st.executeQuery(query);
            rs.next();
            e.setName(rs.getString(1));
            e.setDebility(rs.getString(2));
            e.setLevel(rs.getInt(3));
            p.setName(rs.getString(4));
            e.setPlace(p);
            rs.close();
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(MarvelDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return e;
    }

    /**
     * Query that selects the gems which are in the user's current place
     *
     * @param username Current username logged in
     * @return A set of gems
     * @throws java.sql.SQLException
     */
    public Set selectShowGemsPlaceUser(String username) throws SQLException {
        Set<Gem> gems = new HashSet<>();
        try {
            Statement st = connection.createStatement();
            String query = "select gem.name from gem\n"
                    + "join place\n"
                    + "on gem.place = place.name\n"
                    + "join user\n"
                    + "on gem.place = user.place\n"
                    + "where gem.owner is null and user.username = '" + username + "'";
            ResultSet rs = st.executeQuery(query);
            //rs.next();
            while (rs.next()) {
                Gem g = new Gem();
                g.setName(rs.getString(1));
                gems.add(g);
            }
            rs.close();
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(MarvelDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return gems;
    }

    /**
     * Query that selects all data of the user's current place
     *
     * @param username Current username logged in
     * @return The selected place on database
     * @throws java.sql.SQLException
     */
    public Place selectAllDataPlaceUser(String username) throws SQLException {
        Place place = new Place();
        String[] coordinates = new String[]{"", "", "", ""};
        try {
            Statement st = connection.createStatement();
            String query = "select place.* from place\n"
                    + "inner join user\n"
                    + "on place.name = user.place\n"
                    + "where user.username = '" + username + "'";
            ResultSet rs = st.executeQuery(query);
            rs.next();
            place.setName(rs.getString(1));
            place.setDescription(rs.getString(2));
            coordinates[0] = rs.getString(3);
            coordinates[1] = rs.getString(4);
            coordinates[2] = rs.getString(5);
            coordinates[3] = rs.getString(6);
            place.setCoordinates(coordinates);
            rs.close();
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(MarvelDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return place;
    }

    /**
     * Function that updates the place where the user moves on
     *
     * @param username Current username logged in
     * @param place User's current place
     * @throws java.sql.SQLException
     */
    public void updatePlace(String username, String place) throws SQLException {
        try {
            PreparedStatement ps = connection.prepareStatement("update user\n"
                    + "set user.place = '" + place + "'\n"
                    + "where user.username = '" + username + "';");
            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(MarvelDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Function to update the gem owner
     *
     * @param gemName Gem name
     * @param username Current username logged in
     * @param place Current user's place
     * @return TRUE -> for a succed update // FALSE -> if update goes wrong
     * @throws java.sql.SQLException
     */
    public boolean updateGemOwner(String gemName, String username, String place) throws SQLException {
        try {
            PreparedStatement ps = connection.prepareStatement("update gem\n"
                    + "set gem.owner = '" + username + "'\n"
                    + "where gem.user = '" + username + "'\n"
                    + "and gem.name = '" + gemName + "'\n"
                    + "and gem.place = '" + place + "'");
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(MarvelDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Query that sets all properties to a Ranking object to be printed 
     * @return The ranking list ready to be printed
     * @throws java.sql.SQLException
     */
    public List selectRanking() throws SQLException {
        List<Ranking> ranking = new ArrayList<>();
        try {
            Statement st = connection.createStatement();
            String query = "select gem.user as User, user.superhero as Superhero, user.level as Level, user.points as Points, count(gem.user) as Gemas from gem\n"
                    + "inner join user\n"
                    + "on gem.user = user.username\n"
                    + "where gem.user = gem.owner\n"
                    + "group by gem.user\n"
                    + "order by Gemas desc, Level desc, Points desc";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Ranking r = new Ranking();
                r.setUsername(rs.getString(1));
                r.setSuperhero(rs.getString(2));
                r.setLevel(rs.getInt(3));
                r.setPoints(rs.getInt(4));
                r.setGems(rs.getInt(5));
                ranking.add(r);
            }
            rs.close();
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(MarvelDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ranking;
    }

    /**
     * Query that gets a list of places ordered randomly
     * @return The random oredered list (used to set the gem place in register function)
     * @throws SQLException 
     */
    public List selectRandomPlaces() throws SQLException {
        connect();
        List<Place> places = new ArrayList<>();
        try {
            Statement st = connection.createStatement();
            String query = "select place.name from place\n"
                    + "where place.name != 'New York' order by rand() limit 6";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Place place = new Place();
                place.setName(rs.getString(1));
                places.add(place);
            }
        } catch (SQLException e) {
        }
        return places;
    }

    /**
     * Query that selects all the different types of gems
     * @return A list of each type of gem
     * @throws SQLException 
     */
    public List selectAllGems() throws SQLException {
        connect();
        List<Gem> gems = new ArrayList<>();
        try {
            Statement st = connection.createStatement();
            String query = "select gem.name from gem\n"
                    + "group by gem.name";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Gem gem = new Gem();
                gem.setName(rs.getString(1));
                gems.add(gem);
            }
        } catch (SQLException e) {
        }
        return gems;
    }

    /**
     * Query that inserts a new gem in player's game
     * @param gem The gem setted with all the properties
     * @throws SQLException
     * @throws AppException 
     */
    public void insertNewGem(Gem gem) throws SQLException, AppException {
        try {
            PreparedStatement ps = connection.prepareStatement("insert into gem values (?, ?, ?, ?)");
            ps.setString(1, gem.getName());
            ps.setString(2, gem.getUser().getUsername());
            if (gem.getOwner() == null) {
                ps.setNull(3, Types.VARCHAR);
            } else {
                ps.setString(3, gem.getOwner().getName());
            }
            ps.setString(4, gem.getPlace().getName());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            Logger.getLogger(MarvelDAO.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Query that collects the data neede to the battle function
     * @param username The user loged at the moment
     * @return A user with the parameters which are not used yet
     * @throws SQLException 
     */
    public User selectUser(String username) throws SQLException {
        User user = new User();
        Superhero superhero = new Superhero();
        try {
            Statement st = connection.createStatement();
            String query = "select user.level, user.points, user.superhero, superhero.superpower from user\n"
                    + "inner join superhero\n"
                    + "on user.superhero = superhero.name where user.username = '" + username + "'";
            ResultSet rs = st.executeQuery(query);
            rs.next();
            user.setLevel(rs.getInt(1));
            user.setPoints(rs.getInt(2));
            superhero.setName(rs.getString(3));
            superhero.setSuperpower(rs.getString(4));
            user.setSuperhero(superhero);
            rs.close();
            st.close();
        } catch (SQLException e) {
        }
        return user;
    }

    /**
     * Query that sets a randomnly a place for the enemy 
     * @param enemyName The enemy name
     * @throws SQLException 
     */
    public void updatePlaceEnemy(String enemyName) throws SQLException {
        try {
            PreparedStatement ps = connection.prepareStatement("update enemy set \n"
                    + "enemy.place = (select place.name from \n"
                    + "place order by rand() limit 1) \n"
                    + "where enemy.name = '" + enemyName + "'");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(MarvelDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Query that updates the gem's place with the same place of the enemy
     * @param place The place of the enemy
     * @param username The current username player in the game
     * @throws SQLException 
     */
    public void updatePlaceGemEnemy(String place, String username) throws SQLException {
        try {
            PreparedStatement ps = connection.prepareStatement("update gem\n"
                    + "set gem.place = '" + place + "'\n"
                    + "where gem.user = '" + username + "'");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(MarvelDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Query that updates the points earned in battle
     * @param username Username fighter
     * @param points The points earned in battle
     * @throws SQLException 
     */
    public void updatePoints(String username, int points) throws SQLException {
        try {
            PreparedStatement ps = connection.prepareStatement("update user\n"
                    + "set user.points = user.points + '" + (points) + "'\n"
                    + "where user.username = '" + username + "'");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(MarvelDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Query that updates the gem's owner to null
     * @param username
     * @param place
     * @throws SQLException 
     */
    public void updateGemOwnerNull(String username, String place) throws SQLException {
        try {
            PreparedStatement ps = connection.prepareStatement("update gem\n"
                    + "set gem.owner = null\n"
                    + "where gem.user = '" + username + "' and gem.place = '" + place + "'");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(MarvelDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Function to connect with the database
     *
     * @return 
     */
    public boolean connect()  {
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/marvel";
            String user = "root";
            String pass = "";
            connection = DriverManager.getConnection(url, user, pass);
            return true;
        } catch (SQLException e) {
            System.out.println("There's no connection from database");
        }
        return false;
    }

    /**
     * Function to disconnect with the database
     *
     * @throws SQLException
     */
    public void disconnect() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
